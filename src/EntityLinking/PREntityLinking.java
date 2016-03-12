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
 * �����ʵ��֮�佨����ʱ�����ʵ����Ҫ����������Ƶķ�Χ
 */
public class PREntityLinking {
   
	public HashMap<String,String>linking(String text,MaxentTagger tagger,Wikipedia wiki,AbstractSequenceClassifier<CoreLabel> classifier) throws InterruptedException{
		/*
		 * ��������
		 */
		HashMap<String,String>result=new HashMap<String,String>();
		String wikiurl="https://en.wikipedia.org/wiki/";
		PreDeal pd=new PreDeal();
	//	Rentity ren=new Rentity();
		ToMatrix tm=new ToMatrix();
		PageRank PR=new PageRank();
		//List<Double>filterlist=new ArrayList<Double>();
		
		/*
		 *ʵ��ʶ��
		 */
		entityRecognize er=new entityRecognize();
        List<String>entitylist=er.getMention(text,classifier);
        String retext=er.getText();//������ָ����֮����ı�
        
        /*
         * ��ѡ��ȡ�Լ���ѡʵ�����ƶȼ��㣬�ù��̿����滻����������(����editdis,prior)
         */
        Candidate can=new Candidate();
        HashMap<String,List<String>>canlist=can.getAllCandidate(wiki, entitylist);
      //  System.out.println(can.getAllCandidate(wiki, entitylist));
        MulSim  ms=new MulSim(canlist,retext,tagger,wiki);
	    ms.sim();
	    HashMap<String,Double>canhash=ms.getsimilary();
	    HashMap<String,Double>cfhash=new HashMap<String,Double>();//�洢����֮��ĺ�ѡʵ��,ÿ��ʵ��ֻ����ǰk����ѡʵ��
	//    System.out.println(canhash);
        /*
         * �Ժ�ѡʵ����й��ˣ�ֻ������������Ԥ�������ĺ�ѡ,����ÿ��ʵ��ָ����ֻ����ǰ��k����Ϊ0��
         * ��ѡ,����ͼ�Ĵ�С��������������
         */
	//    System.out.println(entitylist);
	    for(int i=0;i<entitylist.size();i++){
	     // for(String cankey:canlist.keySet()){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:canhash.keySet()){
				 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ������ƶ�
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
				 }
			 }//emd for
			//ÿ��ʵ��ָ����ֻ����ǰ��5����ѡʵ��
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
			  * ������󣬺�ѡʵ�弯��Ϊ��
			  */
			 if(rankhash.size()==0){
				 result.put(mention, "NIL");
			 }
			 
			 cfhash.putAll(rankhash); 
		 }//end for
	//      System.out.println(cfhash);
	     
	    /*
	     * ʵ�����ͼ�Ĺ���,����������Ԥ��ľ�������(����ʵ�����ı��еľ���С��100������)
	     */
	    if(cfhash.size()>0){
	    	
	     List<String>graphnode=new ArrayList<String>();//ͼ�ڵ㼯��
	     List<String>backuplist=new ArrayList<String>();
	     List<String>backuplist1=new ArrayList<String>();
	     List<Double>Iconlist=new ArrayList<Double>();
	     List<Double>onelist=new ArrayList<Double>();
	     for(String key:cfhash.keySet()){
	    	 graphnode.add(key);
	    	 backuplist.add(key);
	    	 backuplist1.add(key);
	    	 Iconlist.add(cfhash.get(key));
	    	 onelist.add(1.0/cfhash.size()); //��ͳ��pr ��ƽ��ƫ��1/n
	     }
	     String[] nodearray = (String[])graphnode.toArray(new String[0]);
	     Graph graph = new MatrixGraph(nodearray);
	    
	     /*
	      * ����ڵ�����ض�
	      */
	     HashMap<String, HashMap<String,Double>>cohash=new HashMap<String, HashMap<String,Double>>();
	     OneMulRe mr=new OneMulRe(backuplist,wiki,backuplist1);
    	 mr.relate();
    	 cohash=mr.getRelatity();
	     
	     for(String nodekey1:cohash.keySet()){
	    	HashMap<String,Double>inhash=cohash.get(nodekey1);
	    	// System.out.println("��ǰ�ڵ�"+nodekey1);
	    	 for(String nodekey2:inhash.keySet()){
	    		 /*
	    		  * ����������ǲ���ͬһʵ��ָ����ĺ�ѡ����֮�䲻���ڱ�,�������ı��ľ�������
	    		  */
	    		 if(!graph.exist(nodekey1, nodekey2)){
	    			 graph.addEdge(nodekey1, nodekey2, inhash.get(nodekey2));
	    		 }//end if
	    	 }//end for
	     }//end for
	   
	    /*
	     * ʹ��ͼ�ϵ���������㷨
	     */
	        String regraph=graph.printGraph(); //���ж�ͼ����Ϊ��
	  //   System.out.println(graphnode);
	    	List<List<Double>>nodelist=tm.listToDouble(nodearray, regraph);
	  	   //  System.out.println(nodelist);
	  	     //�����б�ֱ���Ȩ�ؾ��󣬳�ʼ������U����ʼ�����Ŷȣ�ת�ƵĲ���alpha
	  	    List<Double> prlist=PR.calPageRank(nodelist, onelist,Iconlist,PR.ALPHA);
	  	    // PR.printVec(prlist);
	  	     //����ڵ������������ı�����ض�
	  	   //  List<Double>relevance=PR.vectorMulMatrix(nodelist, prlist);
	  	   //  PR.printVec(relevance); 
	  	     
	          /*
	           * ʵ������,���ı��е�ʵ��ָ�������ӵ�֪ʶ����
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
					 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ������ƶ�
					 if(mention.equals(pd.getMen(key))){
						 phash.put(pd.getCan(key), candite.get(key));
					 }
				 }//emd for
				 
				 /*
				  * ����������к�ѡʵ�������ڹ���ʱ�򱻹��˵��ˣ���phash�ĳ���Ϊ0
				  */
				 if(phash.size()==0){
					 result.put(mention, "NIL");
				 }
				 
				 /*
				  * ���ֻ��һ����ѡʵ��
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
				   * ����ж����ѡʵ��
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
	
	//����
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
				          //ͬ��������Ӧ��keyֵ  
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
	 * ����
	 */
//	public HashMap<String,Double> sort(HashMap<String,Double>hash){
//		
//	  HashMap<String,Double>subhash=new HashMap<String,Double>();	  	  
//      List<Map.Entry<String, Double>> infoIds =new ArrayList<Map.Entry<String, Double>>(hash.entrySet());
//   
//      //���ü�������ķ���  
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
//   if(infoIds.size()>4){//����5����ѡ��ֻ����5��������5������ȫ����,���ѭ��k���ڴ�ѭ��k+1
//	  for (int i=infoIds.size()-1;i>infoIds.size()-4;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i);  
//		    if(ent.getValue()>0.15){ //�ô��ķ�ֵ������������
//		    	 subhash.put(ent.getKey(), ent.getValue());
//		    }
//		   
//		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
//	   }   
//     }else{
//	   for (int i=infoIds.size()-1;i>=0;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i); 
//		    if(ent.getValue()>0.15){ //�ô���f��ֵ������������
//		    	subhash.put(ent.getKey(), ent.getValue());
//		    }
//		   
//		    //System.out.println(ent.getKey()+"="+ent.getValue());       
//	  }   
//    }
//    return subhash;
//}
	
	public static void main(String[] args) throws InterruptedException {
		long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		  entityRecognize er=new entityRecognize();
		  OperatorWiki op=new OperatorWiki();
		   Wikipedia wiki=op.connect();
		  /*
		   * ����ʵ�
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
		          System.out.println("��ǰ�ı�:"+i);
		          PREntityLinking pren=new PREntityLinking();
		           String text=str; 
		           StringBuffer sb= new StringBuffer("");
		           List<String>list=er.getMention(text,classifier);
		           if(list.size()==0) continue;//����û�к�ѡ
		           HashMap<String,String>disresult=pren.linking(text,tagger,wiki,classifier);
		           sb.append(i+".�ı�:"+text+System.getProperty("line.separator")+"  ʵ��ָ����:"+list.toString()+System.getProperty("line.separator")+"  ������:"+disresult.toString()+System.getProperty("line.separator")+System.getProperty("line.separator"));
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
		   long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println(minute);	
	}	
}
