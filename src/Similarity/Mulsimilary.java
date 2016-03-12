package Similarity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/*
 * 多线程计算相似度，计算速度更快,调用的函数是列表加配置文件
 */
public class Mulsimilary extends Thread {
  public static List<String>canlist=new ArrayList<String>();
  public static HashMap<String,Double>hash=new HashMap<String,Double>();
  public String text;
  MaxentTagger tagger;
  Wikipedia wiki;
  public Mulsimilary(List<String>canlist,String text,MaxentTagger tagger,Wikipedia wiki){
	  this.tagger=tagger;
	  this.canlist=canlist;
	  this.text=text;
	  this.wiki=wiki;
  }
  public Mulsimilary(){
	  
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
			String tmp = getCandidate();//获取url
			System.out.println(tmp);
			if(tmp!=null&&!hash.containsKey(tmp)){
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
  
 public HashMap<String,Double> getsimilary(HashMap<String,Double>simhash){
	 simhash=hash;
	 hash=null;
	 text=null;
	 canlist=null;
	 return simhash;
 }
  public void computer(String text,String name) throws
              UnsupportedEncodingException, IOException {
	 OperatorWiki op=new OperatorWiki();
	 String page=op.getPageText(wiki, name);
	 TextSimilar ts=new TextSimilar();
	 TextProcessing Tp=new TextProcessing();
	 List<String>one=Tp.SegmentProcess(text.replace("Li Na",""),tagger);
	 List<String>two=Tp.SegmentProcess(page.replace("Li Na",""),tagger);
	 double sim=ts.compute(one,two);
	 hash.put(name, sim);  
  }
  
  
  
  public void sim() throws InterruptedException{
//	  String taggerPath = "models/english-left3words-distsim.tagger";
//	  MaxentTagger tagger = new MaxentTagger(taggerPath);
	  Mulsimilary thread1=new Mulsimilary(canlist,text,tagger,wiki);
	  Mulsimilary thread2=new Mulsimilary(canlist,text,tagger,wiki);
	  Mulsimilary thread3=new Mulsimilary(canlist,text,tagger,wiki);
	  Mulsimilary thread4=new Mulsimilary(canlist,text,tagger,wiki);
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
	   MaxentTagger tagger = new MaxentTagger(taggerPath);
	   OperatorWiki op=new OperatorWiki();
	   Wikipedia wiki=op.connect();
	   List<String>canlist=new ArrayList<String>();
	   long startTime=System.currentTimeMillis();  //获取开始时间
	   canlist.add("Li Na");
	   canlist.add("Li Na (diver)");
	   canlist.add("Li Na (singer)");
	   canlist.add("Li Na (fencer)");
	   canlist.add("Li Na (cyclist)");
	   canlist.add("Li Na (daughter of Mao Zedong)");
	   String text="Li Na defeats Victoria Azarenka to qualify for WTA Championships semi-finals.";
	   Mulsimilary ms=new Mulsimilary(canlist,text,tagger,wiki);
	   System.out.println(ms.canlist);
	   ms.sim();
	   long endTime=System.currentTimeMillis(); //获取结束时间
	   double minute=(endTime-startTime)/1000.0;
	   System.out.println("程序运行时间： "+minute+"s");
	   System.out.println(ms.getsimilary(hash));
	   System.out.println(ms.canlist);
	   System.out.println(ms.text+" "+hash);
}
}
