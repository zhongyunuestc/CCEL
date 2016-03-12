package EntityLinking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Candidate.Candidate;
import Deal.PreDeal;
import Prior.MulPrior;
import Recognition.entityRecognize;
import Similarity.MulSim;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class PriorEntityLinking {
	public HashMap<String,String> link(List<String>entitylist, MaxentTagger tagger,Wikipedia wiki) throws InterruptedException{
		HashMap<String,String>result=new HashMap<String,String>();
		 String wikiurl="https://en.wikipedia.org/wiki/";
		 Candidate can=new Candidate();
		 MulPrior mp=new MulPrior(can.getAllCandidate(wiki, entitylist),tagger,wiki);
		 mp.priors();
		 HashMap<String,Double>canhash=mp.getprior();
		 PreDeal pd=new PreDeal();
	//	 HashMap<String,HashMap<String,Double>>chash=new HashMap<String,HashMap<String,Double>>();
		for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:canhash.keySet()){
				 //获取实体指称项对应的候选实体的相似度
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), canhash.get(key));
				 }
			 }//emd for
			 
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
		  // chash.put(mention, phash);
		 }//end for
		//System.out.println(chash);
		return result;
	}//end function
	
	public static void main(String[] args) throws InterruptedException {
		  long startTime=System.currentTimeMillis();  //获取开始时
		   entityRecognize er=new entityRecognize();
		   String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
		    String taggerPath = "models/english-left3words-distsim.tagger";
			MaxentTagger tagger = new MaxentTagger(taggerPath);
			OperatorWiki op=new OperatorWiki();
			 Wikipedia wiki=op.connect();
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
			  try {
			         // read file content from file
			         FileReader reader = new FileReader("Data/text.txt");
			         BufferedReader br = new BufferedReader(reader);
			         FileWriter writer = new FileWriter("Data/prior.txt");
			         BufferedWriter bw = new BufferedWriter(writer);
			         String str=null;
			         int i=1383;
			         while((str=br.readLine())!=null) {
			          System.out.println("当前文本:"+i);
			          PriorEntityLinking se=new PriorEntityLinking();
			           String text=str; 
			           StringBuffer sb= new StringBuffer("");
			           List<String>list=er.getMention(text,classifier);
			           HashMap<String,String>disresult=se.link(list,tagger,wiki);
			           sb.append(i+".文本:"+text+System.getProperty("line.separator")+"  实体指称项:"+list.toString()+System.getProperty("line.separator")+"  消歧结果:"+disresult.toString()+System.getProperty("line.separator")+System.getProperty("line.separator"));
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
