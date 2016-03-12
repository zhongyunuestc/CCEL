package EntityLinking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import Candidate.Candidate;
import Deal.PreDeal;
import EntityWindow.Rentity;
import PageRank.Graph;
import PageRank.MatrixGraph;
import PageRank.PageRank;
import PageRank.ToMatrix;
import Recognition.entityRecognize;
import Relate.MulRelate;
import Relate.OneMulRe;
import Relate.Retest;
import Similarity.MulSim;
import WikiDataBase.OperatorWiki;
/*
 * 在相关实体之间建立边时，相关实体需要满足距离限制的范围
 */
public class PREntityLinking {
   
	public HashMap<String,String>linking(String text,MaxentTagger tagger,Wikipedia wiki,AbstractSequenceClassifier<CoreLabel> classifier) throws InterruptedException{
		/*
		 * 基本配置
		 */
		HashMap<String,String>result=new HashMap<String,String>();
		String wikiurl="https://en.wikipedia.org/wiki/";
		PreDeal pd=new PreDeal();
	//	Rentity ren=new Rentity();
		ToMatrix tm=new ToMatrix();
		PageRank PR=new PageRank();
		//List<Double>filterlist=new ArrayList<Double>();
		
		/*
		 *实体识别
		 */
		entityRecognize er=new entityRecognize();
        List<String>entitylist=er.getMention(text,classifier);
        String retext=er.getText();//经过共指处理之后的文本
        
        /*
         * 候选获取以及候选实体相似度计算，该过程可以替换成其他过程(比如editdis,prior)
         */
        Candidate can=new Candidate();
        HashMap<String,List<String>>canlist=can.getAllCandidate(wiki, entitylist);
      //  System.out.println(can.getAllCandidate(wiki, entitylist));
        MulSim  ms=new MulSim(canlist,retext,tagger,wiki);
	    ms.sim();
	    HashMap<String,Double>canhash=ms.getsimilary();
	    HashMap<String,Double>cfhash=new HashMap<String,Double>();//存储过滤之后的候选实体,每个实体只保留前k个候选实体
	//    System.out.println(canhash);
        /*
         * 对候选实体进行过滤，只保留满足我们预设条件的候选,对于每个实体指称项只保持前面k个不为0的
         * 候选,限制图的大小，提高消歧的速率
         */
	//    System.out.println(entitylist);
	    for(int i=0;i<entitylist.size();i++){
	     // for(String cankey:canlist.keySet()){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:canhash.keySet()){
				 //获取实体指称项对应的候选实体的相似度
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
				 }
			 }//emd for
			//每个实体指称项只保留前面5个候选实体
			// System.out.println(phash);
			 if(phash.size()==1){
				  for(String key1:phash.keySet()){
					   if(phash.get(key1)==0.0){
						  result.put(mention, "NIL");
					  }
				  }//end for
			  }//end if
			 
			 HashMap<String,Double>rankhash=new HashMap<String,Double>();
			 HashMap<String,Double>rank=sort(phash);
			// System.out.println(rank);
			 for(String key1:rank.keySet()){
	            String men="<"+mention+","+key1+">";
				rankhash.put(men,rank.get(key1));
			 }
			 
			 /*
			  * 排完序后，候选实体集合为空
			  */
			 if(rankhash.size()==0){
				 result.put(mention, "NIL");
			 }
			 
			 cfhash.putAll(rankhash); 
		 }//end for
	//      System.out.println(cfhash);
	     
	    /*
	     * 实体相关图的构建,必须先满足预设的距离限制(两个实体在文本中的距离小于100个单词)
	     */
	    if(cfhash.size()>0){
	    	
	     List<String>graphnode=new ArrayList<String>();//图节点集合
	     List<String>backuplist=new ArrayList<String>();
	     List<String>backuplist1=new ArrayList<String>();
	     List<Double>Iconlist=new ArrayList<Double>();
	     List<Double>onelist=new ArrayList<Double>();
	     for(String key:cfhash.keySet()){
	    	 graphnode.add(key);
	    	 backuplist.add(key);
	    	 backuplist1.add(key);
	    	 Iconlist.add(cfhash.get(key));
	    	 onelist.add(1.0/cfhash.size()); //传统的pr 是平均偏好1/n
	     }
	     String[] nodearray = (String[])graphnode.toArray(new String[0]);
	     Graph graph = new MatrixGraph(nodearray);
	    
	     /*
	      * 计算节点间的相关度
	      */
	     HashMap<String, HashMap<String,Double>>cohash=new HashMap<String, HashMap<String,Double>>();
	     OneMulRe mr=new OneMulRe(backuplist,wiki,backuplist1);
    	 mr.relate();
    	 cohash=mr.getRelatity();
	     
	     for(String nodekey1:cohash.keySet()){
	    	HashMap<String,Double>inhash=cohash.get(nodekey1);
	    	// System.out.println("当前节点"+nodekey1);
	    	 for(String nodekey2:inhash.keySet()){
	    		 /*
	    		  * 满足的条件是不是同一实体指称项的候选，且之间不存在边,且满足文本的距离限制
	    		  */
	    		 if(!graph.exist(nodekey1, nodekey2)){
	    			 graph.addEdge(nodekey1, nodekey2, inhash.get(nodekey2));
	    		 }//end if
	    	 }//end for
	     }//end for
	   
	    /*
	     * 使用图上的随机游走算法
	     */
	        String regraph=graph.printGraph(); //先判断图不能为空
	  //   System.out.println(graphnode);
	    	List<List<Double>>nodelist=tm.listToDouble(nodearray, regraph);
	  	   //  System.out.println(nodelist);
	  	     //参数列表分别是权重矩阵，初始化矩阵U，初始化置信度，转移的参数alpha
	  	    List<Double> prlist=PR.calPageRank(nodelist, onelist,Iconlist,PR.ALPHA);
	  	    // PR.printVec(prlist);
	  	     //计算节点与整个输入文本的相关度
	  	   //  List<Double>relevance=PR.vectorMulMatrix(nodelist, prlist);
	  	   //  PR.printVec(relevance); 
	  	     
	          /*
	           * 实体链接,将文本中的实体指称项链接到知识库中
	           */
	  	  HashMap<String,Double>candite=new HashMap<String,Double>();
	  	   for(int i=0;i<prlist.size();i++){
	  	     String nodename=graphnode.get(i);
	  	    // double scon=Iconlist.get(i)+prlist.get(i);
	  	     double scon=prlist.get(i);
	  	     candite.put(nodename, scon);
	  	     }
	  //	     System.out.println(candite);
	  	   for(int i=0;i<entitylist.size();i++){
				 String mention=entitylist.get(i);
				 HashMap<String,Double>phash=new HashMap<String,Double>();
				 for(String key:candite.keySet()){
					 //获取实体指称项对应的候选实体的相似度
					 if(mention.equals(pd.getMen(key))){
						 phash.put(pd.getCan(key), candite.get(key));
					 }
				 }//emd for
				 
				 /*
				  * 如果最终所有候选实体由于在过滤时候被过滤掉了，则phash的长度为0
				  */
				 if(phash.size()==0){
					 result.put(mention, "NIL");
				 }
				 
				 /*
				  * 如果只有一个候选实体
				  */
				  if(phash.size()==1){
					  for(String key1:phash.keySet()){
						  if(phash.get(key1)!=0.0){
							  result.put(mention, wikiurl+key1);
						  }//end if 
						  else if(phash.get(key1)==0.0){
							  result.put(mention, "NIL");
						  }
					  }//end for
				  }//end if
				  
				  /*
				   * 如果有多个候选实体
				   */
				  if(phash.size()>1){
					  String ne="";
					   double max=0.0;
					  for(String key1:phash.keySet()){
						  if(phash.get(key1)>max){
							  max=phash.get(key1);
							  ne=key1;
						  }//end if
					  } //end for
					  result.put(mention, wikiurl+ne);
				  }//end if
			 }//end for
	     }//end if cfhash
	//    System.out.println(entitylist);
		return result;
	}
	
	//排序
	public HashMap<String,Double> sort(HashMap<String,Double>hash){
		HashMap<String,Double>subhash=new HashMap<String,Double>();
		List<String> keyList = new ArrayList<String>();  
		keyList.addAll(hash.keySet());  
		List<Double> valueList = new ArrayList<Double>();  
		valueList.addAll(hash.values());  
		if(hash.size()==1){
			if(valueList.get(0)>0.15)
			  subhash.put(keyList.get(0), valueList.get(0));
		}
		else if(hash.size()>1){
			for(int i=0; i<valueList.size(); i++)  
				   for(int j=i+1; j<valueList.size(); j++) {  
				      if(valueList.get(j)>valueList.get(i)) {  
				    	  Double tmp=valueList.get(j);
				          valueList.set(j, valueList.get(i));  
				          valueList.set(i, tmp);  
				          //同样调整对应的key值  
				          String kk=keyList.get(j);
				          keyList.set(j, keyList.get(i));  
				          keyList.set(i, kk);   
				      }  
			}
			if(valueList.size()>4){
				for (int i=0;i<4;i++) {
					if(valueList.get(i)>0.15){
						subhash.put(keyList.get(i), valueList.get(i));
					}
				}//end for
			}//end if
			else{
				 for (int i=0;i<valueList.size()-1;i++){
					 if(valueList.get(i)>0.15){
					   subhash.put(keyList.get(i), valueList.get(i));
					} 
				 }
				
			}//end for
		}//end else
		
    return subhash;
}	
	
	/*
	 * 排序
	 */
//	public HashMap<String,Double> sort(HashMap<String,Double>hash){
//		
//	  HashMap<String,Double>subhash=new HashMap<String,Double>();	  	  
//      List<Map.Entry<String, Double>> infoIds =new ArrayList<Map.Entry<String, Double>>(hash.entrySet());
//   
//      //调用集合排序的方法  
//     Collections.sort(infoIds, new Comparator<Map.Entry<String,Double>>() {  
//       public int compare(Map.Entry<String, Double> o1,  
//          Map.Entry<String, Double> o2) {  
//    	   if(o1.getValue()>o2.getValue()) return 1;
//    	   else if(o1.getValue()==o2.getValue()) return 0;
//    	   else return -1;
//    	 //  return o1.getValue()>=o2.getValue()?1:-1;
//     }  
//     });  
//     
//   if(infoIds.size()>4){//多余5个候选的只保留5个，少于5个的则全保留,外层循环k，内存循环k+1
//	  for (int i=infoIds.size()-1;i>infoIds.size()-4;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i);  
//		    if(ent.getValue()>0.15){ //该处的阀值可以重现设置
//		    	 subhash.put(ent.getKey(), ent.getValue());
//		    }
//		   
//		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
//	   }   
//     }else{
//	   for (int i=infoIds.size()-1;i>=0;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i); 
//		    if(ent.getValue()>0.15){ //该处的f阀值可以重现设置
//		    	subhash.put(ent.getKey(), ent.getValue());
//		    }
//		   
//		    //System.out.println(ent.getKey()+"="+ent.getValue());       
//	  }   
//    }
//    return subhash;
//}
	
	public static void main(String[] args) throws InterruptedException {
		long startTime=System.currentTimeMillis();  //获取开始时间
		  entityRecognize er=new entityRecognize();
		  OperatorWiki op=new OperatorWiki();
		   Wikipedia wiki=op.connect();
		  /*
		   * 载入词典
		   */
		  String taggerPath = "models/english-left3words-distsim.tagger";
		  String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
		  AbstractSequenceClassifier<CoreLabel> classifier=null;
		  try {
			  classifier = CRFClassifier.getClassifier(serializedClassifier);
		} catch (ClassCastException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		  MaxentTagger tagger = new MaxentTagger(taggerPath);
		  try {
		         // read file content from file
		         FileReader reader = new FileReader("Data/text.txt");
		         BufferedReader br = new BufferedReader(reader);
		         FileWriter writer = new FileWriter("Data/BasicPR.txt");
		         BufferedWriter bw = new BufferedWriter(writer);
		         String str=null;
		         int i=1295;
		         while((str=br.readLine())!=null) {
		          System.out.println("当前文本:"+i);
		          PREntityLinking pren=new PREntityLinking();
		           String text=str; 
		           StringBuffer sb= new StringBuffer("");
		           List<String>list=er.getMention(text,classifier);
		           if(list.size()==0) continue;//里面没有候选
		           HashMap<String,String>disresult=pren.linking(text,tagger,wiki,classifier);
		           sb.append(i+".文本:"+text+System.getProperty("line.separator")+"  实体指称项:"+list.toString()+System.getProperty("line.separator")+"  消歧结果:"+disresult.toString()+System.getProperty("line.separator")+System.getProperty("line.separator"));
	        	   System.out.println(sb);
		           bw.write(sb.toString());
	        	   i++;
		         }
		         br.close();
		         reader.close();
		         bw.close();
		         writer.close();
		   }
		   catch(FileNotFoundException e) {
		               e.printStackTrace();
		         }
		         catch(IOException e) {
		               e.printStackTrace();
		         }
		   long endTime=System.currentTimeMillis(); //获取结束时间
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println(minute);	
	}	
}
