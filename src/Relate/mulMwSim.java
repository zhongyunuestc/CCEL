package Relate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Similarity.Mulsimilary;
import Similarity.TextProcessing;
import Similarity.TextSimilar;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/*
 * ���õĽӿ���ʵ����+���ʵ���б�+wiki����
 */

public class mulMwSim extends Thread{
	  public static List<String>relist;
	  public static HashMap<String, HashMap<String,Double>>hash=new HashMap<String, HashMap<String,Double>>();
	  public String name;
	  public static HashMap<String,Double>rehash=new HashMap<String,Double>();
	  public Wikipedia wiki;
	  public static int id;
	  public static List<Integer>inlist;
	  public mulMwSim(String name,List<String>relist,Wikipedia wiki){
		  this.name=name;
		  this.relist=relist;
		  this.wiki=wiki;
	  }
	  public mulMwSim(){
		  
	  }
	  public synchronized static String getRel() {
			String tmpUrl;
			if(relist.isEmpty()) return null;
			tmpUrl =relist.get(0);
			relist.remove(0);
			return tmpUrl;
		}
	  
	  public void run() {
			while (!relist.isEmpty()) {
				String tmp = getRel();//��ȡurl
				if(tmp!=null){
					try {
						//�ڴ˼��������ƶ�
					//	System.out.println(name);
						rehash.put(tmp,computer(id,inlist,tmp));
					//	System.out.println(computer(id,inlist,tmp));
						  
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			hash.put(name, rehash);
			//rehash=null;
			
		}
	  
	 public HashMap<String, HashMap<String,Double>> getRelatity(HashMap<String, HashMap<String,Double>>relhash){
		 relhash=hash;
		 hash=null;
		 rehash=null;
		 inlist=null;
		 relist=null;
		 id=0;
		 name=null;
		 return relhash;
	 }
	  public double computer(int id,List<Integer>inlist,String n2) throws
	              UnsupportedEncodingException, IOException {
		 OperatorWiki op=new OperatorWiki();
		// int id1=op.getID(wiki,n1);
		 int id2=op.getID(wiki,n2);
		 System.out.println(name+"-->id:"+id);
		// System.out.println(n1+"id1="+id1);
		 System.out.println(n2+"-->id2="+id2);
		/*
		 * ʹ��ҳ��ĳ���
		 */
		//List<Integer>list1=op.getOutlinkID(wiki,n1);
		List<Integer>list2=op.getOutlinkID(wiki,n2);
			/*
			 * ʹ��ҳ�����������֪��ʵ��ļ������ܴ�
			 */
//			List<Integer>list1=op.getInlinkID(wiki,n1);
//			List<Integer>list2=op.getInlinkID(wiki,n2);
			double jsim=0;
		//	System.out.println("list1="+list1);
//			System.out.println("inlist="+inlist);
//			System.out.println("list2="+list2);
			
			/*
			 * ֱ��������ʵ�����ض�Ϊ1
			 */
			if(inlist.contains(id2)||list2.contains(id)){
				jsim=1.0;
			}
			
			 /*
			 * ���������ʵ��֮�����ض�Ϊ0.5
			 */
//			else if(union(list1,list2)>=3){ 
//				jsim=0.5;
//			}
			
			/*
			 * <Li Na,Li Na(tennis)>
			 * ͼ�ڵ������<mention-candidate of entity>������һ����ʼ���ĵ÷֣�editdis��prior��pop��
			 * �Ȳ���֮������Ҳ�����������ʵ����ضȸ���jaccard��ضȼ���
			 * jaccard��ضȼ���С��0.1�Ŀ�����0
			 * Ҳ���Ը���ҳ�����ƶ������㣨ҳ�����ƶȴ���0.4ҳ֮��Ҳ���һ������ߣ�
			 * ����Ҳ����0
			 * ����ʹ��M&W���ƶȣ���ض�=0��ʾ֮�䲻���ڱ�
			 * wiki����ҳ����
			 * 
			 */
			else{
		        /*
		         * ����Գ��������������ƽ����ĸ���
		         */
				 int number=union(inlist,list2)-6;
				 if(number>0){
				   double e1=Math.log10(max(inlist.size(),list2.size()))-Math.log10(number);
				   double e2=Math.log10(4988598)-Math.log10(min(inlist.size(),list2.size()));
				   jsim=1-e1/e2;
					// jsim=(double)union(list1,list2)/intersection(list1, list2);
				  if(jsim<0.01) jsim=0.0; 
				 }
				 else{
					 jsim=0.0;
				 }
			}
			System.out.println(jsim);
			return jsim;
	  }
	  
	  
	  
	    /*
		 * ����
		 */
		public int union(List<Integer>list1,List<Integer>list2){
			List<Integer>list=new ArrayList<Integer>();
			list.addAll(list1);
			list.retainAll(list2);
			System.out.println("������"+list.size());
			return list.size();
		}
		
		/*
		 * �����ֵ
		 */
		public int max(int m,int n){
			if(m>n) return m;
			else return n;
		}
		
		/*
		 * ����Сֵ
		 */
		public int min(int m,int n){
			if(m>n) return n;
			else return m;
		}
	  
	  public void relate() throws InterruptedException{
		  OperatorWiki op=new OperatorWiki();
		  id=op.getID(wiki,name);
		  inlist=op.getOutlinkID(wiki,name);
		  
		  mulMwSim thread1=new mulMwSim(name,relist,wiki);
		  mulMwSim thread2=new mulMwSim(name,relist,wiki);
		  mulMwSim thread3=new mulMwSim(name,relist,wiki);
		  mulMwSim thread4=new mulMwSim(name,relist,wiki);
		//  Mulsimilary thread5=new Mulsimilary(tagger);
//		  Mulsimilary thread6=new Mulsimilary();
//		  Mulsimilary thread7=new Mulsimilary();
//		  Mulsimilary thread8=new Mulsimilary();
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
		//	thread5.join();
//			thread6.join();
//			thread7.join();
//			thread8.join();
	  }
	  
	   public static void main(String[] args) throws InterruptedException {
		   List<String>relist=new ArrayList<String>();
		   OperatorWiki op=new OperatorWiki();
		   Wikipedia wiki=op.connect();
		   String entity="Beijing";
		 //  System.out.println(mw.name);
		   long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		   relist.add("Yao Ming");
		   relist.add("Liu Xiang");
		   relist.add("Zheng Jie");
		   relist.add("Li Na (singer)");
		   relist.add("Li Na");
		   relist.add("Pu Zhiqiang");
		   relist.add("Peng Liyuan"); 
		   relist.add("Obama");
		   relist.add("Michael Jordan");
		   relist.add("Yan Zi");
		   relist.add("Nangjing");
		   relist.add("Shanghai");
		   relist.add("Yu opera"); 
		   relist.add("Kewang");
		   relist.add("Google");
		   relist.add("Wang Qishan");
		   relist.add("Guangdong");
		 //  System.out.println(relist);
		   mulMwSim mw=new mulMwSim(entity,relist,wiki);
		   mw.relate();
		   long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println("��������ʱ�䣺 "+minute+"s");
		   System.out.println(mw.getRelatity(hash));
		    System.out.println(mw.name);
	}
}
