package Relate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Deal.PreDeal;
import WikiDataBase.OperatorWiki;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;

public class OneMulRe extends Thread{
	  public static List<String>relist; //���ʵ��ڵ�
	  public static HashMap<String, HashMap<String,Double>>hash=new HashMap<String, HashMap<String,Double>>();
	  public Wikipedia wiki;
	  public static List<String>bylist;
	  public OneMulRe(List<String>rellist,Wikipedia wiki,List<String>bylist){
		  this.relist=rellist;
		  this.wiki=wiki;
		  this.bylist=bylist;
	  }
	  public static synchronized String getRel() {
			String tmpUrl;
			if(relist.isEmpty()) return null;
			tmpUrl =relist.get(0);
			relist.remove(0);
			return tmpUrl;
		}
	  
	  public void run() {
		   PreDeal pd=new  PreDeal();
			while (!relist.isEmpty()) {
				String tmp = getRel();//��ȡurl
				if(tmp!=null){
				 HashMap<String,Double>rehash=new HashMap<String,Double>();
					try {
						//�ڴ˼��������ƶ�
					//	System.out.println(name);
						for(int i=0;i<bylist.size();i++){
							if(!pd.getMen(tmp).equals(pd.getMen(bylist.get(i)))){
								rehash.put(bylist.get(i),computer(tmp,bylist.get(i)));
							}
						}
					//   System.out.println(tmp+"bylist"+bylist.size());
					   hash.put(tmp, rehash);	  
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//rehash=null;
			
		}
	  
	 public HashMap<String, HashMap<String,Double>> getRelatity(){
		 HashMap<String, HashMap<String,Double>> relhash=(HashMap<String, HashMap<String, Double>>) hash.clone();
		 hash.clear();
		 relist.clear();
		 bylist.clear();
		 return relhash;
	 }
	  public double computer(String n1,String n2) throws
	              UnsupportedEncodingException, IOException {
		 OperatorWiki op=new OperatorWiki();
		 PreDeal pd=new  PreDeal();
		 String name1=pd.getCan(n1);
		 String name2=pd.getCan(n2);
		 int id1=op.getID(wiki,name1);
		 int id2=op.getID(wiki,name2);
		// System.out.println(node);
		// System.out.println(node+"-->id:"+id);
		// System.out.println(n1+"id1="+id1);
		// System.out.println(n2+"-->id2="+id2);
		/*
		 * ʹ��ҳ��ĳ���
		 */
		List<Integer>list1=op.getOutlinkID(wiki,name1);
		List<Integer>list2=op.getOutlinkID(wiki,name2);
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
			if(list1.contains(id2)||list2.contains(id1)){
				jsim=1.0;
			}
			
			 /*
			 * ���������ʵ��֮�����ض�Ϊ0.5
			 */
//			else if(union(list1,list2)>=3){ 
//				jsim=0.5;
//			}
			
			/*
			 * ֻҪ����ʵ��֮���н�������1������һ�ֿ����Ծ��Ǽ���ֱ������ԣ����Լ�������
			 */
			else{
		        /*
		         * ����Գ��������������ƽ����ĸ���
		         */
				 int number=union(list1,list2)-6;
				 if(number>0){
				  jsim=1.0;
				 } 
				 
				 else{
					 jsim=0.0;
				 }
			}
		//	System.out.println(jsim);
			return jsim;
	  }
	  
	    /*
		 * ����
		 */
		public int union(List<Integer>list1,List<Integer>list2){
			List<Integer>list=new ArrayList<Integer>();
			list.addAll(list1);
			list.retainAll(list2);
		//  System.out.println("������"+list.size());
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
		  OneMulRe thread1=new OneMulRe(relist,wiki,bylist);
		  OneMulRe thread2=new OneMulRe(relist,wiki,bylist);
		  OneMulRe thread3=new OneMulRe(relist,wiki,bylist);
		  OneMulRe thread4=new OneMulRe(relist,wiki,bylist);
//		  Retest thread5=new Retest(relist,wiki,bylist);
//		  Retest thread6=new Retest(relist,wiki,bylist);
//		  Retest thread7=new Retest(relist,wiki,bylist);
//		  Retest thread8=new Retest(relist,wiki,bylist);
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
		   List<String>rellist=new ArrayList<String>();
		   List<String>rellist1=new ArrayList<String>();
		   OperatorWiki op=new OperatorWiki();
		   Wikipedia wiki=op.connect();
		  // String entity="<Obama,Barack Obama>";
		   List<String>enlist=Arrays.asList("<Obama,Barack Obama>","<Li Na,Li Na>");
		   
		 //  System.out.println(mw.name);
		   long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		   rellist.add(" <Zheng Jie,Zheng Jie>");
		   rellist.add("<Li Na,Li Na>");
		   rellist.add("<Victoria Azarenka,Victoria Azarenka>");
		   rellist.add(" <Li Na,Li Na (fencer)>");
		   
		   rellist1.add(" <Zheng Jie,Zheng Jie>");
		   rellist1.add("<Li Na,Li Na>");
		   rellist1.add("<Victoria Azarenka,Victoria Azarenka>");
		   rellist1.add(" <Li Na,Li Na (fencer)>");
		   
		   OneMulRe mr=new OneMulRe(rellist,wiki,rellist1);
		//   System.out.println(mr.bylist);
		   mr.relate();
		   System.out.println(mr.getRelatity());
//		   System.out.println(mr.node);
		   System.out.println(rellist1);
		   long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println("��������ʱ�䣺 "+minute+"s");
		 
//		    System.out.println(mr.node);
//		    System.out.println(mr.hash);
//		    System.out.println(mr.rehash);
//		    System.out.println(mr.relist);
	}
}