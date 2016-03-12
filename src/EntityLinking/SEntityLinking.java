package EntityLinking;

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
import EntityWindow.Rentity;
import PageRank.Graph;
import PageRank.MatrixGraph;
import PageRank.PageRank;
import PageRank.ToMatrix;
import Recognition.entityRecognize;
import Relate.MulRelate;
import Relate.Retest;
import Similarity.MulSim;
import WikiDataBase.OperatorWiki;
/*
 * �����ʵ��֮�佨����ʱ�����ʵ����Ҫ����������Ƶķ�Χ,ʹ�ô�ͷ��ǩ�����ݼ�
 * ʵ��ʱ��ѡ�ķ�ֵ����Ϊ0.25
 */
public class SEntityLinking {
   
	public HashMap<String,String>linking(String text) throws InterruptedException{
		/*
		 * ��������
		 */
		HashMap<String,String>result=new HashMap<String,String>();
		String wikiurl="https://en.wikipedia.org/wiki/";
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
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		PreDeal pd=new PreDeal();
		Rentity ren=new Rentity();
		ToMatrix tm=new ToMatrix();
		PageRank PR=new PageRank();
		Maxprlist ma=new Maxprlist();
		//List<Double>filterlist=new ArrayList<Double>();
		
		/*
		 *ʵ��ʶ��
		 */
		entityRecognize er=new entityRecognize();
		//���ò����ķ�ʽ��ǰ�����ı����������ֵ�
        List<String>entitylist=er.getMention(text,classifier);
        String retext=er.getText();//������ָ����֮����ı�
      //  System.out.println(entitylist);
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
	 //   System.out.println(canhash);
        /*
         * �Ժ�ѡʵ����й��ˣ�ֻ������������Ԥ�������ĺ�ѡ,����ÿ��ʵ��ָ����ֻ����ǰ��k����Ϊ0��
         * ��ѡ,����ͼ�Ĵ�С��������������
         */
	    for(int i=0;i<entitylist.size();i++){
	    //  for(String cankey:canlist.keySet()){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:canhash.keySet()){
				 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ������ƶ�
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
				 }
			 }//emd for
			//ÿ��ʵ��ָ����ֻ����ǰ��������ѡʵ��
			// System.out.println(phash);
			 if(phash.size()==1){ //��ʵ�岻��Ϊͼ�Ľڵ�
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
			 cfhash.putAll(rankhash); 
		 }//end for
	 //     System.out.println(cfhash);
	     
	    /*
	     * ʵ�����ͼ�Ĺ���,����������Ԥ��ľ�������(����ʵ�����ı��еľ���С��100������)
	     */
	   if(cfhash.size()>0){
		   
	     List<String>graphnode=new ArrayList<String>();//ͼ�ڵ㼯��
	     List<Double>Iconlist=new ArrayList<Double>();
	     for(String key:cfhash.keySet()){
	    	 graphnode.add(key);
	    	 Iconlist.add(cfhash.get(key));
	     }
	     String[] nodearray = (String[])graphnode.toArray(new String[0]);
	     Graph graph = new MatrixGraph(nodearray);
//	     System.out.println(graphnode);
//	     System.out.println(Iconlist);
	     
	     for(String key:cfhash.keySet()){
	    	  List<String>rentitylist=new ArrayList<String>();
	    	  for(String key2:cfhash.keySet()){
	    		  if(!pd.getMen(key).equals(pd.getMen(key2))&&!graph.exist(key, key2)
	    				  &&ren.iscontain(pd.getMen(key),retext,pd.getMen(key2))){
	    			    rentitylist.add(key2);
	    		  }//end if
	    	  }//end for
	    	//�õ���ʵ�����ض�
	    //	System.out.println(key+"--->"+rentitylist);
	        HashMap<String, HashMap<String,Double>>cohash=new HashMap<String, HashMap<String,Double>>();
	 	    MulRelate mr=new MulRelate(key,rentitylist,wiki);
	     	mr.relate();
	     	cohash=mr.getRelatity();
	     //	System.out.println(cohash);
	     	//������
	     	for(String inkey:cohash.get(key).keySet()){
	     		 graph.addEdge(key, inkey, cohash.get(key).get(inkey));
	     	}	  
	      }//end for
	    
	    /*
	     * ʹ��ͼ�ϵ���������㷨
	     */
	     String regraph=graph.printGraph();
	 //    System.out.println(regraph);
	     List<List<Double>>nodelist=tm.listToDouble(nodearray, regraph);
	     //�����б�ֱ���Ȩ�ؾ��󣬳�ʼ������U����ʼ�����Ŷȣ�ת�ƵĲ���alpha
	     List<Double> prlist=PR.calPageRank(nodelist, Iconlist,Iconlist,PR.ALPHA);
	    // PR.printVec(prlist);
	   //�õ�ÿ��ʵ��ָ�����ѡʵ���п��Ŷ���ߵĺ�ѡʵ��
	     HashMap<String,Double>prhash=ma.Max(entitylist, graphnode, prlist);
	     List<Double> newprlist=new ArrayList<Double>();
	     for(String key:prhash.keySet()){
	    	    newprlist.add(prhash.get(key));
	    	 }//end for
//	     System.out.println(graphnode);
//	     System.out.println(prhash);
//	     System.out.println(newprlist);
	    
	     //�õ�ÿ����ѡʵ������Ŷ���ߵĺ�ѡʵ���Ȩֵ����
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
	      
	   //����ڵ������������ı�����ض�
	  //   List<Double>relevance=PR.vectorMulMatrix(nodelist, prlist);
	    List<Double>relevance=PR.vectorMulMatrix(wlist, newprlist);
	   //  PR.printVec(relevance); 
	     
        /*
         * ʵ������,���ı��е�ʵ��ָ�������ӵ�֪ʶ����
         */
	     HashMap<String,Double>candite=new HashMap<String,Double>();
	     for(int i=0;i<relevance.size();i++){
	    	 String nodename=graphnode.get(i);
	    	 double scon=prlist.get(i)+relevance.get(i);
	    	 candite.put(nodename, scon);
	     }
	  //   System.out.println(candite);
	     for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:candite.keySet()){
				 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ������ƶ�
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
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
				  String ne=null;
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
		   if(valueList.get(0)>0.25)
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
						if(valueList.get(i)>0.25){
						 subhash.put(keyList.get(i), valueList.get(i));
						}
					}//end for
				}//end if
				else{
					 for(int i=0;i<valueList.size()-1;i++){
					   if(valueList.get(i)>0.25){
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
//    	   return  o1.getValue() >=o2.getValue() ? 1 :-1;
//     }  
//     });  
//     
//   if(infoIds.size()>4){//����5����ѡ��ֻ����5��������5������ȫ����,���ѭ��k���ڴ�ѭ��k+1
//	  for (int i=infoIds.size()-1;i>infoIds.size()-4;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i);  
//		    if(ent.getValue()>0.15){ //�õط��ķ�ֵʱ���滻��
//		    	 subhash.put(ent.getKey(), ent.getValue());
//		    }
//		   
//		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
//	   }   
//     }else{
//	   for (int i=infoIds.size()-1;i>=0;i--) {  
//		    Entry<String,Double> ent=infoIds.get(i); 
//		    if(ent.getValue()>0.15){ //ʵ��ʱ�õط��ķ�ֵʱ���滻��
//		    	subhash.put(ent.getKey(), ent.getValue());
//		    }
//		    //System.out.println(ent.getKey()+"="+ent.getValue());       
//	  }   
//    }
//    return subhash;
//}
	
	public static void main(String[] args) throws InterruptedException {
		 long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		 String text="Early calls on CME live and feeder cattle futures ranged from 0.200 cent higher to 0.100 lower , livestock analysts said .  The continued strong tone to cash cattle and beef markets should prompt further support .  Outlook for a bullish cattle-on-feed report is also expected to lend support and prompt some bull spreading , analysts said .  However , trade will likely be light and prices could drift on evening up ahead of the report .  Cash markets are also expected to be quiet after the record amount of feedlot cattle traded this week , they said ."; 
		 SEntityLinking sen=new SEntityLinking();
		   System.out.println(sen.linking(text));
		 long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		 double minute=(endTime-startTime)/1000.0;
		 System.out.println(minute);
	}
	
}
