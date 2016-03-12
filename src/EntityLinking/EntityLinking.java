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
import Deal.Maxprlist;
import Deal.PreDeal;
import Deal.Normalize;
import EditDistance.MulEdit;
import PageRank.Graph;
import PageRank.MatrixGraph;
import PageRank.PageRank;
import PageRank.ToMatrix;
import Prior.MulPrior;
import Recognition.entityRecognize;
import Relate.OneMulRe;
import Relate.OnlyGd;
import Relate.Retest;
import Similarity.MulSim;
import WikiDataBase.OperatorWiki;
/*
 * 在相关实体之间建立边时，相关实体需要满足距离限制的范围
 * 使用pagerank算法时，关闭did you mean功能
 */
public class EntityLinking {
   
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
		Maxprlist ma=new Maxprlist();
		Normalize nor=new Normalize();
		//List<Double>filterlist=new ArrayList<Double>();
		
		/*
		 *实体识别
		 */
		entityRecognize er=new entityRecognize();
        List<String>entitylist=er.getMention(text,classifier);
        String retext=er.getText();//经过共指处理之后的文本
      //  System.out.println(entitylist);
      
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
	   // System.out.println(canhash);
        /*
         * 对候选实体进行过滤，只保留满足我们预设条件的候选,对于每个实体指称项只保持前面k个不为0的
         * 候选,限制图的大小，提高消歧的速率
         */
	    for(int i=0;i<entitylist.size();i++){
	    //  for(String cankey:canlist.keySet()){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:canhash.keySet()){
				 //获取实体指称项对应的候选实体的相似度
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
				 }
			 }//emd for
			//每个实体指称项只保留前面三个候选实体
			// System.out.println(phash);
			 if(phash.size()==1){ //空实体不作为图节点
				  for(String key1:phash.keySet()){
					   if(phash.get(key1)==0.0){
						  result.put(mention, "NIL");
					  }
				  }//end for
			  }//end if
			 
			 HashMap<String,Double>rankhash=new HashMap<String,Double>();
			 for(String key1:sort(phash).keySet()){
	             String men="<"+mention+","+key1+">";
				 rankhash.put(men,sort(phash).get(key1));
			 }
			 
			 /*
			  * 排完序后，候选实体集合为空
			  */
			 if(rankhash.size()==0){
				 result.put(mention, "NIL");
			 }
			 
			 cfhash.putAll(rankhash); 
		 }//end for
	  //    System.out.println(cfhash);
	     
	    
	    /*
	     * 实体相关图的构建,必须先满足预设的距离限制(两个实体在文本中的距离小于100个单词)
	     */
	    if(cfhash.size()>0){	
	     List<String>graphnode=new ArrayList<String>();//图节点集合
	     List<String>backuplist=new ArrayList<String>();
	     List<String>backuplist1=new ArrayList<String>();
	     List<Double>priorlist=new ArrayList<Double>();//用于存储实体的先验概率
	   //  List<Double>editlist=new ArrayList<Double>();
	    //  List<Double>onelist=new ArrayList<Double>(); 不考虑权重系数
	     List<Double>Iconlist=new ArrayList<Double>();//存储初始化矩阵U以及初始化置信度值
	     //节点值和节点一一对应
	     for(String key:cfhash.keySet()){
	    	 graphnode.add(key);
	    	 backuplist.add(key);
	    	 backuplist1.add(key);
	    //	 priorlist1.add(key);
	    	 Iconlist.add(cfhash.get(key));
	    //   onelist.add(1.0/cfhash.size()); //相当于1/n
	     }
	     
	  //   System.out.println(graphnode);
	     /*
	      * 计算候选实体的先验概率（需要改进，不然会很慢）,此处可以换成编辑距离
	      */
	     MulPrior mp=new MulPrior(can.getAllCandidate(wiki, entitylist),tagger,wiki);
		 mp.priors();
		 HashMap<String,Double>priorhash=mp.getprior();
	    // System.out.println(priorhash);
		 
//	     MulEdit  me=new MulEdit(can.getAllCandidate(wiki, entitylist),tagger,wiki);
//		 me.editsim();
//		 HashMap<String,Double>edithash=me.getsimilary();
	     for(int i=0;i<graphnode.size();i++){
	    	 String node=graphnode.get(i);
	    	 priorlist.add(priorhash.get(node));
	     }
	    
	     //归一化节点的初始化可信度
	     List<Double>norlist=nor.normalize(entitylist, graphnode, priorlist);
//	     System.out.println(graphnode);
//	     System.out.println(norlist);
	    
	     
	     /*
	      * 设立图节点
	      */
	     String[] nodearray = (String[])graphnode.toArray(new String[0]);
	     Graph graph = new MatrixGraph(nodearray);
	     
	   //  System.out.println(graphnode);
	     /*
	      * 计算节点间的相关度
	      */
	     HashMap<String, HashMap<String,Double>>cohash=new HashMap<String, HashMap<String,Double>>();
	     //可调整相关度计算方式
	     Retest mr=new Retest(backuplist,wiki,backuplist1);
	   //  OneMulRe mr=new OneMulRe(backuplist,wiki,backuplist1);
	    // OnlyGd mr=new OnlyGd(backuplist,wiki,backuplist1);
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
	     String regraph=graph.printGraph();
	   //  System.out.println(regraph);
	     List<List<Double>>nodelist=tm.listToDouble(nodearray, regraph);
	     //参数列表分别是权重矩阵，初始化矩阵U（也可以退换priorlist,editlist），初始化置信度（可以退换priorlist,editlist），转移的参数alpha
	     List<Double> prlist=PR.calPageRank(nodelist, Iconlist,norlist,PR.ALPHA);
	    // PR.printVec(prlist);
	     
	     //得到每个实体指向项候选实体中可信度最高的候选实体
	     HashMap<String,Double>prhash=ma.Max(entitylist, graphnode, prlist);
	     List<Double> newprlist=new ArrayList<Double>();
	     for(String key:prhash.keySet()){
	    	 
	    	   newprlist.add(prhash.get(key));
	    	    
	    	 }//end for
//	     System.out.println(graphnode);
	//     System.out.println(prhash);
//	     System.out.println(newprlist);
	    
	     //得到每个候选实体与可信度最高的候选实体的权值矩阵
	     List<List<Double>>wlist=new ArrayList<List<Double>>();
	     for(int i=0;i<graphnode.size();i++){ 	
	    	 List<Double>weight=new ArrayList<Double>();
	    	 for(String key:prhash.keySet()){
	    	    int j=graphnode.indexOf(key);
	    		weight.add(nodelist.get(i).get(j));
	    	 }//end for 
	       wlist.add(weight);	 
	    } //end for  graphnode 
//	    System.out.println(nodelist);
//	    System.out.println(wlist);
	      
	   //计算节点与整个输入文本的相关度
	 // List<Double>relevance=PR.vectorMulMatrix(nodelist, prlist);
	    List<Double>relevance=PR.vectorMulMatrix(wlist, newprlist);
	   //  PR.printVec(relevance); 
	     
        /*
         * 实体链接,将文本中的实体指称项链接到知识库中
         */
//	     HashMap<String,Double>candite1=new HashMap<String,Double>();
//		    // System.out.println(graphnode);
//		     for(int i=0;i<prlist.size();i++){
//		    	 String nodename=graphnode.get(i);
//		    	 double scon=prlist.get(i);
//		    	 candite1.put(nodename, scon);
//		     }
//		     System.out.println(candite1);
	     
        //计算语义一致性
	     HashMap<String,Double>candite=new HashMap<String,Double>();
	    // System.out.println(graphnode);
	     for(int i=0;i<relevance.size();i++){
	    	 String nodename=graphnode.get(i);
	    	 double scon=prlist.get(i)+relevance.get(i);
	    	 candite.put(nodename, scon);
	     }
	     
	      System.out.println(candite);
	      
	     //选择语义一致性最大的候选实体作为链接对象
	     for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 double total=0.0;
			 for(String key:candite.keySet()){
				 //获取实体指称项对应的候选实体的语义一致性
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), candite.get(key));
					 //计算同一实体指称项所对应候选实体的语义一致性之和
					 total+=candite.get(key); 
				 }
			 }//end for candidate
			 
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
				 
//				 double total=0.0;
//				/*
//				 * 计算同一实体指称项所对应的所有候选实体的语义一致性之和
//				 */
//				 for(String key:phash.keySet()){
//					 total+=phash.get(key);
//				 }
				 
				 /*
				  *选择语义一致性最大的且大于预设阀值0.2的 
				  */
				  String ne=null;
				  double max=0.2;
				  for(String key1:phash.keySet()){
					  double sc=phash.get(key1)/total;
					//  System.out.println(sc);
					  if(sc>=max){
						  max=sc;
						  ne=key1;
					  }//end if
				  } //end for
				  
				  if(ne.length()==0){
					  
					  result.put(mention, "NIL"); 
				  }
				  else{
					  
					  result.put(mention, wikiurl+ne); 
				  } 
			  }//end if
		 }//end for
	     
	   }//end if cfcash
	  //  System.out.println(entitylist);
		return result;
	}
	
	
	//选择相似度排名前k的候选实体作为最终的候选集合
   public HashMap<String,Double> sort(HashMap<String,Double>hash){
	   HashMap<String,Double>subhash=new HashMap<String,Double>();
	   List<String> keyList = new ArrayList<String>();  
	   keyList.addAll(hash.keySet());  
	  List<Double> valueList = new ArrayList<Double>();  
	  valueList.addAll(hash.values());  
		if(hash.size()==1){
			if(valueList.get(0)>0.1)
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
				if(valueList.size()>5){
					for (int i=0;i<5;i++) {
						if(valueList.get(i)>0.1){
							subhash.put(keyList.get(i), valueList.get(i));
						}
					}//end for
				}//end if
				else{
				  for (int i=0;i<valueList.size()-1;i++){
					 if(valueList.get(i)>0.1){
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
	public HashMap<String,Double> sort1(HashMap<String,Double>hash){
		
	  HashMap<String,Double>subhash=new HashMap<String,Double>();	  	  
      List<Map.Entry<String, Double>> infoIds =new ArrayList<Map.Entry<String, Double>>(hash.entrySet());
   
      //调用集合排序的方法  
     Collections.sort(infoIds, new Comparator<Map.Entry<String,Double>>() {  
       public int compare(Map.Entry<String, Double> o1,  
          Map.Entry<String, Double> o2) {  
    	   return  o1.getValue() >=o2.getValue() ? 1 :-1;
     }  
     });  
     
   if(infoIds.size()>5){//多余5个候选的只保留5个，少于5个的则全保留,外层循环k，内存循环k+1,同时可以加相似度限制
	  for (int i=infoIds.size()-1;i>infoIds.size()-5;i--) {  
		    Entry<String,Double> ent=infoIds.get(i);  
		    if(ent.getValue()>0.1){ //在此加相似度限制
		    	 subhash.put(ent.getKey(), ent.getValue());
		    }
		   
		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
	   }   
     }else{
	   for (int i=infoIds.size()-1;i>=0;i--) {  
		    Entry<String,Double> ent=infoIds.get(i); 
		    if(ent.getValue()>0.1){ //在此加相似度度限制
		    	subhash.put(ent.getKey(), ent.getValue());
		    }
		   
		    //System.out.println(ent.getKey()+"="+ent.getValue());       
	  }   
    }
    return subhash;
}
	
	public static void main(String[] args) throws InterruptedException {
		long startTime=System.currentTimeMillis();  //获取开始时间
		//载入字典和配置文件
		String taggerPath = "models/english-left3words-distsim.tagger";
		//不同的语料用不同的词典
		String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier=null;
		MaxentTagger tagger = new MaxentTagger(taggerPath);
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
	    entityRecognize er=new entityRecognize();
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
				  
	   try {
	         // read file content from file
	         FileReader reader = new FileReader("Data/text.txt");
	         BufferedReader br = new BufferedReader(reader);
	         FileWriter writer = new FileWriter("Data/our.txt");
	         BufferedWriter bw = new BufferedWriter(writer);
	         String str=null;
	         int i=1;
	         while((str=br.readLine())!=null) {
	          System.out.println("当前文本:"+i);
	          EntityLinking en=new EntityLinking();
	           String text=str; 
	           StringBuffer sb= new StringBuffer("");
	           List<String>list=er.getMention(text,classifier);
	           if(list.size()==0) continue;//里面没有候选
	           HashMap<String,String>disresult=en.linking(text,tagger,wiki,classifier);
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
