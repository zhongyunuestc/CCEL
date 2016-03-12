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

public class MulRelate extends Thread{
	  public static List<String>relist; //相关实体节点
	  public static HashMap<String, HashMap<String,Double>>hash=new HashMap<String, HashMap<String,Double>>();
	  public static HashMap<String,Double>rehash=new HashMap<String,Double>();
	  public String node;//节点名字
	  public Wikipedia wiki;
	  public static int id;//当前页面的id号
	  public static List<Integer>inlist;//存储页面的链接id号
	  public MulRelate(String node,List<String>relist,Wikipedia wiki){
		  this.node=node;
		  this.relist=relist;
		  this.wiki=wiki;
	  }
	  public MulRelate(){
		  
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
				String tmp = getRel();//获取url
				if(tmp!=null){
					try {
						//在此计算相相似度
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
			HashMap<String, Double> rehash1=(HashMap<String, Double>) rehash.clone();
			hash.put(node, rehash1);
			//rehash=null;
			
		}
	  
	 public HashMap<String, HashMap<String,Double>>getRelatity(){
		 HashMap<String, HashMap<String,Double>> relhash=(HashMap<String, HashMap<String, Double>>) hash.clone();
		// System.out.println(relhash);
		 hash.clear();
		 rehash.clear();
		 inlist.clear();
		 return relhash;
	 }
	 
	  public double computer(int id,List<Integer>inlist,String n2) throws
	              UnsupportedEncodingException, IOException {
		 OperatorWiki op=new OperatorWiki();
		 PreDeal pd=new  PreDeal();
		 String name2=pd.getCan(n2);
		// int id1=op.getID(wiki,n1);
		 int id2=op.getID(wiki,name2);
		// System.out.println(node);
		// System.out.println(node+"-->id:"+id);
		// System.out.println(n1+"id1="+id1);
		// System.out.println(n2+"-->id2="+id2);
		/*
		 * 使用页面的出链
		 */
		//List<Integer>list1=op.getOutlinkID(wiki,n1);
		List<Integer>list2=op.getOutlinkID(wiki,name2);
			/*
			 * 使用页面的入链，对知名实体的计算量很大
			 */
//			List<Integer>list1=op.getInlinkID(wiki,n1);
//			List<Integer>list2=op.getInlinkID(wiki,n2);
			double jsim=0;
		//	System.out.println("list1="+list1);
//			System.out.println("inlist="+inlist);
//			System.out.println("list2="+list2);
			
			/*
			 * 直接相连的实体的相关度为1
			 */
			if(inlist.contains(id2)||list2.contains(id)){
				jsim=1.0;
			}
			
			 /*
			 * 间接相连的实体之间的相关度为0.5
			 */
//			else if(union(list1,list2)>=3){ 
//				jsim=0.5;
//			}
			
			/*
			 * <Li Na,Li Na(tennis)>
			 * 图节点的名字<mention-candidate of entity>，包含一个初始化的得分（editdis，prior，pop）
			 * 既不是之间相连也不间接相连的实体相关度根据jaccard相关度计算
			 * jaccard相关度计算小于0.1的看做是0
			 * 也可以根据页面相似度来计算（页面相似度大于0.4页之间也添加一条有向边）
			 * 否则也返回0
			 * 或者使用M&W相似度，相关度=0表示之间不存在边
			 * wiki的总页面数
			 * 
			 */
			else{
		        /*
		         * 如果以出链出链，得限制交集的个数
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
		//	System.out.println(jsim);
			return jsim;
	  }
	  
	    /*
		 * 交集
		 */
		public int union(List<Integer>list1,List<Integer>list2){
			List<Integer>list=new ArrayList<Integer>();
			list.addAll(list1);
			list.retainAll(list2);
		//  System.out.println("交集："+list.size());
			return list.size();
		}
		
		/*
		 * 求最大值
		 */
		public int max(int m,int n){
			if(m>n) return m;
			else return n;
		}
		
		/*
		 * 求最小值
		 */
		public int min(int m,int n){
			if(m>n) return n;
			else return m;
		}
	  
	  public void relate() throws InterruptedException{
		  OperatorWiki op=new OperatorWiki();
		  PreDeal pd=new  PreDeal();
		  String name=pd.getCan(node);
		  id=op.getID(wiki,name);
		  inlist=op.getOutlinkID(wiki,name);
		  
		  MulRelate thread1=new MulRelate(node,relist,wiki);
		  MulRelate thread2=new MulRelate(node,relist,wiki);
		  MulRelate thread3=new MulRelate(node,relist,wiki);
		  MulRelate thread4=new MulRelate(node,relist,wiki);
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
		   List<String>rellist1=new ArrayList<String>();
		   List<String>rellist2=new ArrayList<String>();
		   OperatorWiki op=new OperatorWiki();
		   Wikipedia wiki=op.connect();
		  // String entity="<Obama,Barack Obama>";
		   List<String>enlist=Arrays.asList("<Obama,Barack Obama>","<Li Na,Li Na>");
		   
		 //  System.out.println(mw.name);
		   long startTime=System.currentTimeMillis();  //获取开始时间
		   rellist1.add("<Hillary Rodham Clinton,Hillary Rodham Clinton>");
		   rellist1.add("<Democratic Party,Democratic Party of Guam>");
		   rellist1.add("<Democratic Party,Democratic Party (Italy)>");
		   rellist1.add("<United States Senate,United States Senate>");
		   rellist1.add("<Illinois,Illinois>");
		   
		   rellist2.add("<Hillary Rodham Clinton,Hillary Rodham Clinton>");
		   rellist2.add("<Democratic Party,Democratic Party of Guam>");
		   rellist2.add("<Democratic Party,Democratic Party (Italy)>");
		   rellist2.add("<United States Senate,United States Senate>");
		   rellist2.add("<Illinois,Illinois>");
		   
		 //  System.out.println(relist);测试时rellist先不清空
		   for(int i=0;i<enlist.size();i++){  
			   if(i==0){
			//	   System.out.println(rellist1);
				   MulRelate mr=new MulRelate(enlist.get(i),rellist1,wiki);
				//   System.out.println("相关:"+relist);
				   mr.relate();
					//  System.out.println(mr.rehash);
				   System.out.println(mr.getRelatity()); 
			   }
			   if(i==1){
				//   System.out.println(rellist2);
				   MulRelate mr=new MulRelate(enlist.get(i),rellist2,wiki);
				//   System.out.println("相关:"+relist);
				   mr.relate();
					//  System.out.println(mr.rehash);
				   System.out.println(mr.getRelatity());  
			   }
			  
			 
		   }
		 
//		   System.out.println(mr.node);
		   long endTime=System.currentTimeMillis(); //获取结束时间
		   double minute=(endTime-startTime)/1000.0;
		   System.out.println("程序运行时间： "+minute+"s");
		 
//		    System.out.println(mr.node);
//		    System.out.println(mr.hash);
//		    System.out.println(mr.rehash);
//		    System.out.println(mr.relist);
	}
}
