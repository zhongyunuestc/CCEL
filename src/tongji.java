import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import EntityLinking.EntityLinking;
import Recognition.entityRecognize;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;


public class tongji {
  public static void main(String[] args) {
	  long startTime=System.currentTimeMillis();  //获取开始时间		
	  int  sum=0;
	   try {
	         // read file content from file
	         FileReader reader = new FileReader("Data/test.txt");
	         BufferedReader br = new BufferedReader(reader);
             String str=null;
           
	         while((str=br.readLine())!=null) {
	           String st[]=str.trim().split(" ");
	           int k=Integer.parseInt(st[1]);
	           sum+=k;
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
	   System.out.println(minute);	
}
}
