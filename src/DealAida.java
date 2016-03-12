import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import EntityLinking.PREntityLinking;
import Recognition.entityRecognize;


public class DealAida {
  public static void main(String[] args) {
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
	  
    entityRecognize er=new entityRecognize();
    try {
        // read file content from file
        FileReader reader = new FileReader("Data/test.txt");
        BufferedReader br = new BufferedReader(reader);
        FileWriter writer = new FileWriter("Data/sim.txt");
        BufferedWriter bw = new BufferedWriter(writer);
        String str=null;
        while((str=br.readLine())!=null) {
          String text=str; 
       //   System.out.println(text);
          StringBuffer sb= new StringBuffer("");
          List<String>list=er.getMention(text,classifier);
          /*
           * 可根据实体个数选择文本
           */
          if(list.size()<=30&&list.size()>25){
        	sb.append(text+System.getProperty("line.separator"));
            bw.write(sb.toString());
          }
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
    System.out.println("Finish");
}
}
