package EditDistance;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

public class MulEdit extends Thread{
	public static List<String>canlist=new ArrayList<String>();
	  public static HashMap<String,List<String>>canhash=new HashMap<String,List<String>>();
	  public static HashMap<String,Double>hash=new HashMap<String,Double>();
	  MaxentTagger tagger;
	  Wikipedia wiki;
	  public MulEdit(HashMap<String,List<String>>canhash,MaxentTagger tagger,Wikipedia wiki){
		  this.canhash=canhash;
		  this.tagger=tagger;
		  this.canlist=Resolve();
		  this.wiki=wiki;
	  }
	  
	  public MulEdit(){
		  
	  }
	  /*
	   * static方法修饰符一定要加，限制线程的访问顺序
	   */
	  public synchronized  static String getCandidate() {
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
						  computer(tmp);
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
		 hash.clear();
		 canlist.clear();
		 canhash.clear();
		 return simhash;
	 }
	  public void computer(String node) throws
	              UnsupportedEncodingException, IOException {
		 OperatorWiki op=new OperatorWiki();
		 PreDeal pd=new PreDeal();
		 String candidate=pd.getCan(node);
		 String men=pd.getMen(node);
		 String page=op.getPageText(wiki, candidate);
		 SimilarityUtil su=new SimilarityUtil();
		 double sim=su.similary(men, candidate);
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
	  
	  public void editsim() throws InterruptedException{
		  
//		  String taggerPath = "models/english-left3words-distsim.tagger";
//		  MaxentTagger tagger = new MaxentTagger(taggerPath);
		  MulEdit thread1=new  MulEdit(canhash,tagger,wiki);
		  MulEdit  thread2=new  MulEdit(canhash,tagger,wiki);
		  MulEdit thread3=new  MulEdit(canhash,tagger,wiki);
		  MulEdit  thread4=new  MulEdit(canhash,tagger,wiki);
//		  Mulsimilary thread5=new Mulsimilary(canlist,text,tagger,wiki);
//		  Mulsimilary thread6=new Mulsimilary(canlist,text,tagger,wiki);
//		  Mulsimilary thread7=new Mulsimilary(canlist,text,tagger,wiki);
//		  Mulsimilary thread8=new Mulsimilary(canlist,text,tagger,wiki);
		//	Thread.currentThread().wait();
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
//			thread5.start();
//			thread6.start();
//			thread7.start();
//			thread8.start();
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
//			thread5.join();
//			thread6.join();
//			thread7.join();
//			thread8.join();
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
		   String text="Li Na and her teammate Zheng Jie defeats Victoria Azarenka to qualify for WTA Championships semi-finals.";
		   List<String>list=er.getMention(text,classifier);
		//   System.out.println(can.getAllCandidate(wiki, list));
		   MulEdit me=new MulEdit(can.getAllCandidate(wiki, list),tagger,wiki);
//		   System.out.println(ms.canlist);
//		   System.out.println(ms.canhash);
		   me.editsim();
		   long endTime=System.currentTimeMillis(); //获取结束时间
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println("程序运行时间： "+minute+"s");
		   System.out.println(me.getsimilary());
		   System.out.println(me.canlist);
		   System.out.println(hash);
	}
}
