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
 * �����ʵ��֮�佨����ʱ�����ʵ����Ҫ����������Ƶķ�Χ
 * ʹ��pagerank�㷨ʱ���ر�did you mean����
 */
public class EntityLinking {
   
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
		Maxprlist ma=new Maxprlist();
		Normalize nor=new Normalize();
		//List<Double>filterlist=new ArrayList<Double>();
		
		/*
		 *ʵ��ʶ��
		 */
		entityRecognize er=new entityRecognize();
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
	   // System.out.println(canhash);
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
			 if(phash.size()==1){ //��ʵ�岻��Ϊͼ�ڵ�
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
			  * ������󣬺�ѡʵ�弯��Ϊ��
			  */
			 if(rankhash.size()==0){
				 result.put(mention, "NIL");
			 }
			 
			 cfhash.putAll(rankhash); 
		 }//end for
	  //    System.out.println(cfhash);
	     
	    
	    /*
	     * ʵ�����ͼ�Ĺ���,����������Ԥ��ľ�������(����ʵ�����ı��еľ���С��100������)
	     */
	    if(cfhash.size()>0){	
	     List<String>graphnode=new ArrayList<String>();//ͼ�ڵ㼯��
	     List<String>backuplist=new ArrayList<String>();
	     List<String>backuplist1=new ArrayList<String>();
	     List<Double>priorlist=new ArrayList<Double>();//���ڴ洢ʵ����������
	   //  List<Double>editlist=new ArrayList<Double>();
	    //  List<Double>onelist=new ArrayList<Double>(); ������Ȩ��ϵ��
	     List<Double>Iconlist=new ArrayList<Double>();//�洢��ʼ������U�Լ���ʼ�����Ŷ�ֵ
	     //�ڵ�ֵ�ͽڵ�һһ��Ӧ
	     for(String key:cfhash.keySet()){
	    	 graphnode.add(key);
	    	 backuplist.add(key);
	    	 backuplist1.add(key);
	    //	 priorlist1.add(key);
	    	 Iconlist.add(cfhash.get(key));
	    //   onelist.add(1.0/cfhash.size()); //�൱��1/n
	     }
	     
	  //   System.out.println(graphnode);
	     /*
	      * �����ѡʵ���������ʣ���Ҫ�Ľ�����Ȼ�������,�˴����Ի��ɱ༭����
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
	    
	     //��һ���ڵ�ĳ�ʼ�����Ŷ�
	     List<Double>norlist=nor.normalize(entitylist, graphnode, priorlist);
//	     System.out.println(graphnode);
//	     System.out.println(norlist);
	    
	     
	     /*
	      * ����ͼ�ڵ�
	      */
	     String[] nodearray = (String[])graphnode.toArray(new String[0]);
	     Graph graph = new MatrixGraph(nodearray);
	     
	   //  System.out.println(graphnode);
	     /*
	      * ����ڵ�����ض�
	      */
	     HashMap<String, HashMap<String,Double>>cohash=new HashMap<String, HashMap<String,Double>>();
	     //�ɵ�����ضȼ��㷽ʽ
	     Retest mr=new Retest(backuplist,wiki,backuplist1);
	   //  OneMulRe mr=new OneMulRe(backuplist,wiki,backuplist1);
	    // OnlyGd mr=new OnlyGd(backuplist,wiki,backuplist1);
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
	     String regraph=graph.printGraph();
	   //  System.out.println(regraph);
	     List<List<Double>>nodelist=tm.listToDouble(nodearray, regraph);
	     //�����б�ֱ���Ȩ�ؾ��󣬳�ʼ������U��Ҳ�����˻�priorlist,editlist������ʼ�����Ŷȣ������˻�priorlist,editlist����ת�ƵĲ���alpha
	     List<Double> prlist=PR.calPageRank(nodelist, Iconlist,norlist,PR.ALPHA);
	    // PR.printVec(prlist);
	     
	     //�õ�ÿ��ʵ��ָ�����ѡʵ���п��Ŷ���ߵĺ�ѡʵ��
	     HashMap<String,Double>prhash=ma.Max(entitylist, graphnode, prlist);
	     List<Double> newprlist=new ArrayList<Double>();
	     for(String key:prhash.keySet()){
	    	 
	    	   newprlist.add(prhash.get(key));
	    	    
	    	 }//end for
//	     System.out.println(graphnode);
	//     System.out.println(prhash);
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
	 // List<Double>relevance=PR.vectorMulMatrix(nodelist, prlist);
	    List<Double>relevance=PR.vectorMulMatrix(wlist, newprlist);
	   //  PR.printVec(relevance); 
	     
        /*
         * ʵ������,���ı��е�ʵ��ָ�������ӵ�֪ʶ����
         */
//	     HashMap<String,Double>candite1=new HashMap<String,Double>();
//		    // System.out.println(graphnode);
//		     for(int i=0;i<prlist.size();i++){
//		    	 String nodename=graphnode.get(i);
//		    	 double scon=prlist.get(i);
//		    	 candite1.put(nodename, scon);
//		     }
//		     System.out.println(candite1);
	     
        //��������һ����
	     HashMap<String,Double>candite=new HashMap<String,Double>();
	    // System.out.println(graphnode);
	     for(int i=0;i<relevance.size();i++){
	    	 String nodename=graphnode.get(i);
	    	 double scon=prlist.get(i)+relevance.get(i);
	    	 candite.put(nodename, scon);
	     }
	     
	      System.out.println(candite);
	      
	     //ѡ������һ�������ĺ�ѡʵ����Ϊ���Ӷ���
	     for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 double total=0.0;
			 for(String key:candite.keySet()){
				 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ�������һ����
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), candite.get(key));
					 //����ͬһʵ��ָ��������Ӧ��ѡʵ�������һ����֮��
					 total+=candite.get(key); 
				 }
			 }//end for candidate
			 
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
				 
//				 double total=0.0;
//				/*
//				 * ����ͬһʵ��ָ��������Ӧ�����к�ѡʵ�������һ����֮��
//				 */
//				 for(String key:phash.keySet()){
//					 total+=phash.get(key);
//				 }
				 
				 /*
				  *ѡ������һ���������Ҵ���Ԥ�跧ֵ0.2�� 
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
	
	
	//ѡ�����ƶ�����ǰk�ĺ�ѡʵ����Ϊ���յĺ�ѡ����
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
					          //ͬ��������Ӧ��keyֵ  
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
	 * ����
	 */
	public HashMap<String,Double> sort1(HashMap<String,Double>hash){
		
	  HashMap<String,Double>subhash=new HashMap<String,Double>();	  	  
      List<Map.Entry<String, Double>> infoIds =new ArrayList<Map.Entry<String, Double>>(hash.entrySet());
   
      //���ü�������ķ���  
     Collections.sort(infoIds, new Comparator<Map.Entry<String,Double>>() {  
       public int compare(Map.Entry<String, Double> o1,  
          Map.Entry<String, Double> o2) {  
    	   return  o1.getValue() >=o2.getValue() ? 1 :-1;
     }  
     });  
     
   if(infoIds.size()>5){//����5����ѡ��ֻ����5��������5������ȫ����,���ѭ��k���ڴ�ѭ��k+1,ͬʱ���Լ����ƶ�����
	  for (int i=infoIds.size()-1;i>infoIds.size()-5;i--) {  
		    Entry<String,Double> ent=infoIds.get(i);  
		    if(ent.getValue()>0.1){ //�ڴ˼����ƶ�����
		    	 subhash.put(ent.getKey(), ent.getValue());
		    }
		   
		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
	   }   
     }else{
	   for (int i=infoIds.size()-1;i>=0;i--) {  
		    Entry<String,Double> ent=infoIds.get(i); 
		    if(ent.getValue()>0.1){ //�ڴ˼����ƶȶ�����
		    	subhash.put(ent.getKey(), ent.getValue());
		    }
		   
		    //System.out.println(ent.getKey()+"="+ent.getValue());       
	  }   
    }
    return subhash;
}
	
	public static void main(String[] args) throws InterruptedException {
		long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		//�����ֵ�������ļ�
		String taggerPath = "models/english-left3words-distsim.tagger";
		//��ͬ�������ò�ͬ�Ĵʵ�
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
	          System.out.println("��ǰ�ı�:"+i);
	          EntityLinking en=new EntityLinking();
	           String text=str; 
	           StringBuffer sb= new StringBuffer("");
	           List<String>list=er.getMention(text,classifier);
	           if(list.size()==0) continue;//����û�к�ѡ
	           HashMap<String,String>disresult=en.linking(text,tagger,wiki,classifier);
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
