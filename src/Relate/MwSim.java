package Relate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import Similarity.TextProcessing;
import Similarity.TextSimilar;
import WikiDataBase.OperatorWiki;

public class MwSim {
    /*
     * 计算Wikipedia Link-based Measure (WLM)相关度,间接相连的点用WLM相关度，直接相连的点的相关度直接为1
     * ？？？？值得商榷，相同实体支撑项的候选之间不存在相关边
     * 候选实体不在知识库的实体不需要计算与该实体的相关度（候选实体=NIL）
     */
	public double compute(String n1,String n2) {
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		int id1=op.getID(wiki,n1);
		int id2=op.getID(wiki,n2);
		System.out.println("id1="+id1);
		System.out.println("id2="+id2);
		/*
		 * 使用页面的出链
		 */
		List<Integer>list1=op.getOutlinkID(wiki,n1);
		List<Integer>list2=op.getOutlinkID(wiki,n2);
		/*
		 * 使用页面的入链，对知名实体的计算量很大
		 */
//		List<Integer>list1=op.getInlinkID(wiki,n1);
//		List<Integer>list2=op.getInlinkID(wiki,n2);
		double jsim=0;
		System.out.println("list1="+list1);
		System.out.println("list2="+list2);
		
		/*
		 * 直接相连的实体的相关度为1
		 */
		if(list1.contains(id2)||list2.contains(id1)){
			jsim=1.0;
		}
		
		 /*
		 * 间接相连的实体之间的相关度为0.5
		 */
//		else if(union(list1,list2)>=3){ 
//			jsim=0.5;
//		}
		
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
			 int number=union(list1,list2)-5;
			 if(number>0){
			   double e1=Math.log10(max(list1.size(),list2.size()))-Math.log10(number);
			   double e2=Math.log10(4988598)-Math.log10(min(list1.size(),list2.size()));
			   jsim=1-e1/e2;
				// jsim=(double)union(list1,list2)/intersection(list1, list2);
			  if(jsim<0.01) jsim=0.0; 
			 }
			 else{
				 jsim=0.0;
			 }
		}
		return jsim;
	}
	
    /*
     * 利用目录信息计算相似度
     */
	public double computeSim(String n1,String n2) throws UnsupportedEncodingException{
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		int id1=op.getID(wiki,n1);
		int id2=op.getID(wiki,n2);
		System.out.println("id1="+id1);
		System.out.println("id2="+id2);
		/*
		 * 使用页面的出链
		 */
		List<Integer>list1=op.getOutlinkID(wiki,n1);
		List<Integer>list2=op.getOutlinkID(wiki,n2);
		/*
		 * 使用页面的入链，对知名实体的计算量很大
		 */
//		List<Integer>list1=op.getInlinkID(wiki,n1);
//		List<Integer>list2=op.getInlinkID(wiki,n2);
		double jsim=0;
		System.out.println("list1="+list1);
		System.out.println("list2="+list2);
		
		/*
		 * 直接相连的实体的相关度为1
		 */
		if(list1.contains(id2)||list2.contains(id1)){
			jsim=1.0;
			list1=null;
			list2=null;
		}
		
		else{
		    String taggerPath = "models/english-left3words-distsim.tagger";
		    MaxentTagger tagger = new MaxentTagger(taggerPath);
			TextSimilar ts=new TextSimilar();
			TextProcessing tp=new TextProcessing();
			List<String>ls1=tp.SegmentProcess(op.getCatToString(wiki, n1), tagger);
			List<String>ls2=tp.SegmentProcess(op.getCatToString(wiki, n2), tagger);
			jsim=ts.compute(ls1, ls2);
		}
		
		return jsim;
	}
	
	
	/*
	 * 交集
	 */
	public int union(List<Integer>list1,List<Integer>list2){
		List<Integer>list=new ArrayList<Integer>();
		list.addAll(list1);
		list.retainAll(list2);
		System.out.println("交集："+list.size());
		return list.size();
	}
	
	
	/*
	 * 并集
	 */
	public int intersection(List<Integer>list1,List<Integer>list2){
		List<Integer>list3=new ArrayList<Integer>();
		list3.addAll(list1);
		list3.removeAll(list2);
		list3.addAll(list2);
	//	System.out.println("并集："+list3.size());
		return list3.size();
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
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*
		 * obama页面信息量太大，算的很慢
		 */
		long startTime=System.currentTimeMillis();  //获取开始时间
		MwSim js=new MwSim();
		String n1="Pennetta";
		String n2="Urszula Radwańska";
		System.out.println("相关度："+js.compute(n1, n2));
	    long endTime=System.currentTimeMillis(); //获取结束时间
	    double minute=(endTime-startTime)/1000.0;
		System.out.println("程序运行时间： "+minute+"s");
//		System.out.println((double)8/920);
//		List<Integer>l1=Arrays.asList(0,1,2,3,4);
//		List<Integer>l2=Arrays.asList(3,4,5,6);
//		System.out.println((double)js.union(l1, l2)/js.intersection(l1, l2));
	}
}
