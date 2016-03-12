import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import Recognition.entityRecognize;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import Candidate.Candidate;


public class statitic {

  public static void main(String[]args){
	  long startTime=System.currentTimeMillis();  //获取开始时间
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		Candidate can=new Candidate();
		
		String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier=null;
		 int sum=0;
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
	         FileReader reader = new FileReader("Data/test.txt");
	         BufferedReader br = new BufferedReader(reader);
	         String str=null;
	         while((str=br.readLine())!=null) {
	           String text=str; 
	           List<String>list=er.getMention(text,classifier);
	           HashMap<String,List<String>>hash=can.getAllCandidate(wiki, list);
	           for(String key:hash.keySet()){
	        	   if(hash.get(key).size()==0){
	        		   sum+=1;
	        	   }
	           }
	         //  System.out.println(list);
	         }
	         br.close();
	         reader.close();
	   }
	   catch(FileNotFoundException e) {
	               e.printStackTrace();
	         }
	         catch(IOException e) {
	               e.printStackTrace();
	         }
	   System.out.println(sum);
		long endTime=System.currentTimeMillis(); //获取结束时间
	    double minute=(endTime-startTime)/1000.0;
	    System.out.println("程序运行时间： "+minute+"s");
	 }
}
