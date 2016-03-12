package Similarity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Candidate.Candidate;
import Deal.PreDeal;
import Recognition.entityRecognize;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/*
 * 多线程计算相似度，计算速度更快,调用的函数是列表加配置文件
 */
public class MulSim extends Thread {
  public static List<String>canlist=new ArrayList<String>();
  //用于存储候选实体列表
  public static HashMap<String,List<String>>canhash=new HashMap<String,List<String>>();
  public static HashMap<String,Double>hash=new HashMap<String,Double>();
  public String text;
  MaxentTagger tagger;
  Wikipedia wiki;
  public MulSim(HashMap<String,List<String>>canhash,String text,MaxentTagger tagger,Wikipedia wiki){
	  this.canhash=canhash;
	  this.tagger=tagger;
	  this.canlist=Resolve();
	  this.text=text;
	  this.wiki=wiki;
  }
  
  public MulSim(){
	  
  }
  /*
   * static方法修饰符一定要加，限制线程的访问顺序,同步方法
   */
  public synchronized static String getCandidate() {
		String tmpUrl;
		if(canlist.isEmpty()) return null;
		tmpUrl =canlist.get(0);
		canlist.remove(0);
		return tmpUrl;
	}
  public void run() {

		while (!canlist.isEmpty()) {
			String tmp = getCandidate();
		//	System.out.println(tmp);
			if(tmp!=null){
				try {
					//在此计算相相似度
					  computer(text,tmp);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//end if
		}//end while
	}
  
 public HashMap<String,Double> getsimilary(){
	 HashMap<String,Double> simhash=(HashMap<String, Double>) hash.clone();
	// System.out.println(canhash);
	 /*
	  * 单个句子消歧可以把下面的置为空，但是多个句子消歧则不行
	  */
     hash.clear();//清空列表里面的内容
 //	 text=null;
	 canlist.clear();
	 canhash.clear();
	 return simhash;
 }
  public void computer(String text,String node) throws
              UnsupportedEncodingException, IOException {
	 OperatorWiki op=new OperatorWiki();
	 PreDeal pd=new PreDeal();
	 String candidate=pd.getCan(node);
	 String men=pd.getMen(node);
	 String page=op.getPageText(wiki, candidate);
	 TextSimilar ts=new TextSimilar();
	 TextProcessing Tp=new TextProcessing();
	 List<String>one=Tp.SegmentProcess(text.replaceAll(men,""),tagger);
	 List<String>two=Tp.SegmentProcess(page.replaceAll(men,""),tagger);
	 double sim=ts.compute(one,two);
	 hash.put(node, sim);  
  }
  
  public List<String> Resolve(){
	  List<String>relist=new ArrayList<String>();
	  for(String key:canhash.keySet()){
		  if(canhash.get(key).size()==1){
			  hash.put(canhash.get(key).get(0),1.0);
			  }//end if
		  if(canhash.get(key).size()==0){
			  hash.put("<"+key+",NIL>",0.0);
			  }//end if
		   if(canhash.get(key).size()>1){
			   relist.addAll(canhash.get(key));
		   }
		  }//emd for
	   return relist;
	  }
  
  public void sim() throws InterruptedException{
	  
//	  String taggerPath = "models/english-left3words-distsim.tagger";
//	  MaxentTagger tagger = new MaxentTagger(taggerPath);
	  MulSim thread1=new MulSim(canhash,text,tagger,wiki);
	  MulSim  thread2=new MulSim(canhash,text,tagger,wiki);
	  MulSim thread3=new MulSim(canhash,text,tagger,wiki);
	  MulSim  thread4=new MulSim(canhash,text,tagger,wiki);
//	  Mulsimilary thread5=new Mulsimilary(canlist,text,tagger,wiki);
//	  Mulsimilary thread6=new Mulsimilary(canlist,text,tagger,wiki);
//	  Mulsimilary thread7=new Mulsimilary(canlist,text,tagger,wiki);
//	  Mulsimilary thread8=new Mulsimilary(canlist,text,tagger,wiki);
	//	Thread.currentThread().wait();
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
//		thread5.start();
//		thread6.start();
//		thread7.start();
//		thread8.start();
		thread1.join();
		thread2.join();
		thread3.join();
		thread4.join();
//		thread5.join();
//		thread6.join();
//		thread7.join();
//		thread8.join();
  }
  
   public static void main(String[] args) throws InterruptedException {
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
	   List<String>canlist=new ArrayList<String>();
	   Candidate can=new Candidate();
	   entityRecognize er=new entityRecognize();
	   long startTime=System.currentTimeMillis();  //获取开始时间
	  // List<String>list=Arrays.asList("Zheng Jie", "Yan Zi","Li Na");
	   String text=" Results from the Canadian Open  tennis tournament on Thursday ( prefix number denotes seeding ) :  Third round  3 - Wayne Ferreira ( South Africa ) beat Tim Henman ( Britain ) 6-4  6-4  4 - Marcelo Rios ( Chile ) beat Daniel Vacek ( Czech Republic ) 6-4  6-3  5 - Thomas Enqvist ( Sweden ) beat Petr Korda ( Czech Republic )  6-3 6-4  Patrick Rafter ( Australia ) beat 6 - MaliVai Washington ( U.S. )  6-2 6-1  7 - Todd Martin ( U.S. ) beat 9 - Cedric Pioline ( France ) 2-6 6-2  6-4  Mark Philippoussis ( Australia ) beat Bohdan Ulihrach ( Czech Republic ) 6-3 6-4  Alex O'Brien ( U.S. ) beat Mikael Tillstrom ( Sweden ) 6-3 2-6  6-3  Todd Woodbridge ( Australia ) beat Daniel Nestor ( Canada ) 7-6  ( 7-2 ) 7-6 ( 7-4 )";
	   String text1="Obama received national attention during his campaign to represent Illinois in the United States Senate with his victory in the March Democratic Party primary, his keynote address at the Democratic National Convention in July, and his election to the Senate in November. He began his presidential campaign in 2007 and, after a close primary campaign against Hillary Rodham Clinton in 2008, he won sufficient delegates in the Democratic Party primaries to receive the presidential nomination.";
	   for(int i=0;i<2;i++){
		   if(i==0){
			  List<String>list=er.getMention(text,classifier);
	          MulSim  ms=new MulSim(can.getAllCandidate(wiki, list),text,tagger,wiki);
//				   System.out.println(ms.canlist);
//				   System.out.println(ms.canhash);
			  ms.sim();
		     System.out.println(ms.getsimilary());
		   }
		   if(i==1){
			   List<String>list=er.getMention(text1,classifier);
		        MulSim  ms=new MulSim(can.getAllCandidate(wiki, list),text1,tagger,wiki);
//					   System.out.println(ms.canlist);
//					   System.out.println(ms.canhash);
				 ms.sim();
			     System.out.println(ms.getsimilary());
		   }
	   }
	  
	   long endTime=System.currentTimeMillis(); //获取结束时间
	   double minute=(endTime-startTime)/1000.0;
	   System.out.println("程序运行时间： "+minute+"s");
	  
	 //  System.out.println(ms.canhash);
	   System.out.println(hash);
}
}
