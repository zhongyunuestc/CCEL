package Relate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import WikiDataBase.OperatorWiki;

public class MwSim1 {
    /*
     * ����Wikipedia Link-based Measure (WLM)��ض�,��������ĵ���WLM��ضȣ�ֱ�������ĵ����ض�ֱ��Ϊ1
     * ��������ֵ����ȶ����ͬʵ��֧����ĺ�ѡ֮�䲻������ر�
     * ��ѡʵ�岻��֪ʶ���ʵ�岻��Ҫ�������ʵ�����ضȣ���ѡʵ��=NIL��
     */
	public double compute(String n1,String n2) {
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		int id1=op.getID(wiki,n1);
		int id2=op.getID(wiki,n2);
		System.out.println("id1="+id1);
		System.out.println("id2="+id2);
		/*
		 * ʹ��ҳ�����������֪��ʵ��ļ������ܴ�
		 */
		List<Integer>list1=op.getInlinkID(wiki,n1);
		List<Integer>list2=op.getInlinkID(wiki,n2);
		double jsim=0;
		System.out.println("list1="+list1);
		System.out.println("list2="+list2);
		
		/*
		 * ֱ��������ʵ�����ض�Ϊ1
		 */
		if(list1.contains(id2)||list2.contains(id1)){
			jsim=1.0;
		}
		
		 /*
		 * ���������ʵ��֮�����ض�Ϊ0.5
		 */
//		else if(union(list1,list2)>=3){ 
//			jsim=0.5;
//		}
		
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
	        
			   int number=union(list1,list2);
			   double e1=Math.log10(max(list1.size(),list2.size()))-Math.log10(number);
			   double e2=Math.log10(4988598)-Math.log10(min(list1.size(),list2.size()));
			   jsim=1-e1/e2;
				// jsim=(double)union(list1,list2)/intersection(list1, list2);
			  if(jsim<0.1) jsim=0.0; 
		}
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
	 * ����
	 */
	public int intersection(List<Integer>list1,List<Integer>list2){
		List<Integer>list3=new ArrayList<Integer>();
		list3.addAll(list1);
		list3.removeAll(list2);
		list3.addAll(list2);
	//	System.out.println("������"+list3.size());
		return list3.size();
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
	
	public static void main(String[] args) {
		/*
		 * obamaҳ����Ϣ��̫����ĺ���
		 */
		long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		MwSim1 js=new MwSim1();
		String n1="Li Na";
		String n2="Obama";
		System.out.println("��ضȣ�"+js.compute(n1, n2));
	    long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
	    double minute=(endTime-startTime)/1000.0;
		System.out.println("��������ʱ�䣺 "+minute+"s");
//		System.out.println((double)8/920);
//		List<Integer>l1=Arrays.asList(0,1,2,3,4);
//		List<Integer>l2=Arrays.asList(3,4,5,6);
//		System.out.println((double)js.union(l1, l2)/js.intersection(l1, l2));
	}
}
