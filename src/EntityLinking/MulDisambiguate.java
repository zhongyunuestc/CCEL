package EntityLinking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.chainsaw.Main;

import Recognition.entityRecognize;
import Relate.mulMwSim;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/*
 * 对于具有很多文本数据集而言，采用多线程消歧可以加快文本的消歧速度
 */
public class MulDisambiguate extends Thread {
  public static List<String>textlist=new ArrayList<String>();
  public final int length;
  MaxentTagger tagger;
  AbstractSequenceClassifier<CoreLabel> classifier;
  public Wikipedia wiki;
  private final ReentrantLock lock=new  ReentrantLock();
  public MulDisambiguate(List<String>textlist,MaxentTagger tagger, AbstractSequenceClassifier<CoreLabel> classifier,Wikipedia wiki){
	  this.textlist=textlist;
	  this.length=textlist.size();
	  this.tagger=tagger;
	  this.classifier=classifier;
	  this.wiki=wiki;
  }
  
  /*
   * 线程同步方法
   */
  public synchronized static String getText() {
		String tmpUrl;
		if(textlist.isEmpty()) return null;
		tmpUrl =textlist.get(0);
		textlist.remove(0);
		return tmpUrl;
	}
  
  /*
   * 启动所有的线程
   */
  public void startD(){
	  MulDisambiguate thread5=new MulDisambiguate(textlist,tagger,classifier,wiki);
	  MulDisambiguate thread6=new MulDisambiguate(textlist,tagger,classifier,wiki);
	  MulDisambiguate thread7=new MulDisambiguate(textlist,tagger,classifier,wiki);
	  MulDisambiguate thread8=new MulDisambiguate(textlist,tagger,classifier,wiki);
	//  MulDisambiguate thread9=new MulDisambiguate(textlist,tagger,classifier,wiki);
		thread5.start();
		thread6.start();
		thread7.start();
		thread8.start();
		//thread9.start();
		try {
			thread5.join();
			thread6.join();
			thread7.join();
			thread8.join();
			//thread9.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
    /*
    * 调用的消歧方法
    */
  public HashMap<String,String> disambiguate(String text){
	 entityRecognize er=new entityRecognize();
	 PREntityLinking pre=new PREntityLinking();
	 HashMap<String,String>result=new HashMap<String,String>();
	 HashMap<String,String>disresult=new HashMap<String,String>();
	 List<String>list=er.getMention(text,classifier);
	 try {
		disresult=pre.linking(text,tagger,wiki,classifier);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	 result.put(list.toString(), disresult.toString());
	 return result;
  }
  
  public void write(HashMap<String,String>Dresult,String tmp,int i){
	  lock.lock();
	  try {
		   System.out.println("当前文本:"+i);
			FileWriter writer = new FileWriter("Data/BasicPR.txt",true);
			BufferedWriter bw= new BufferedWriter(writer);
			 StringBuffer sb= new StringBuffer("");
			// System.out.println(Dresult);
			 for(String key: Dresult.keySet()){
				 sb.append(i+".文本:"+tmp+System.getProperty("line.separator")+"  实体指称项:"+key.toString()+System.getProperty("line.separator")+"  消歧结果:"+ Dresult.get(key)+System.getProperty("line.separator")+System.getProperty("line.separator"));
				 bw.write(sb.toString());
			 }
        //  System.out.print(sb);
			 
		//	 resultbuffer.add(sb);
			 bw.close();
	         writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
  }
  
  
  /*
   * 激活线程的run方法
   */
  public void run() {
		while (!textlist.isEmpty()) {
			String tmp = getText();//获取url
			int i=length-textlist.size()+43;
			if(tmp!=null){
				//在此计算进行消歧
			//    write(Dresult,tmp,i);
				
			//	FileWriter writer = new FileWriter("Data/prior.txt",true);
			//BufferedWriter bw= new BufferedWriter(writer);
				 StringBuffer sb= new StringBuffer("");
				HashMap<String,String>Dresult=new HashMap<String,String>();
				Dresult=disambiguate(tmp);
									// System.out.println(Dresult);
				 for(String key: Dresult.keySet()){
				 sb.append(i+".文本:"+tmp+System.getProperty("line.separator")+"  实体指称项:"+key.toString()+System.getProperty("line.separator")+"  消歧结果:"+ Dresult.get(key)+System.getProperty("line.separator")+System.getProperty("line.separator"));
				 //	 bw.write(sb.toString());
						}
				  System.out.println("当前消歧文本为:"+i);
				  System.out.print(sb);
				// resultbuffer.add(sb);
				// bw.close();
				// writer.close();
				
			}//end if
		}//end while
	}
  
   /*
    * 以链表的形式获取结果
    */
//  public List<StringBuffer>getResult(){
//	 List<StringBuffer>buffer=resultbuffer;
//	 resultbuffer.clear();
//	 return buffer;
//  }
  
   /*
    * 主函数
    */
  public static void main(String[] args) {
	  /*
	   * 将待消歧的数据集存储为list形式
	   */
	  List<String>textlist=new ArrayList<String>();
	  String taggerPath = "models/english-left3words-distsim.tagger";
	  String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
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
	  MaxentTagger tagger = new MaxentTagger(taggerPath);
		 FileReader reader;
		try {
			reader = new FileReader("Data/text.txt");
			BufferedReader br = new BufferedReader(reader);
			String str=null;
		   while((str=br.readLine())!=null){
			    textlist.add(str);
			 }
		    br.close();
	        reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	 MulDisambiguate  disambigutor=new MulDisambiguate(textlist,tagger,classifier,wiki);
	  disambigutor.startD();
	  System.out.println("Finish");
}
}
