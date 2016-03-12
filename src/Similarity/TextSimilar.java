package Similarity;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TextSimilar {
	 public double compute(List<String>list1,List<String>list2){
		 List<String>list=new ArrayList<String>();
		// TextProcessing Tp=new TextProcessing();
		 int k=0;
		String[] array1 = (String[])list1.toArray(new String[0]);
		String[] array2 = (String[])list2.toArray(new String[0]);
	//	String[] array2 = (String[])Tp.selectKey(list2).toArray(new String[0]);
//     	System.out.println(list1);
//	    System.out.println(list2);
//		list.addAll(list1);
//		list.addAll(list2);
	//  list=Tp.selectKey(list2);
	    if(list1.size()<list2.size()){
	      list=(ArrayList<String>)removeDuplicateWithOrder(list1);
	    }
	    else{
	      list=(ArrayList<String>)removeDuplicateWithOrder(list2);
	    }
   //    list=(ArrayList<String>)removeDuplicateWithOrder(intersection(list1,list2));
	 //   System.out.println(list);
		//遍历合并后的list1
		int[] wordNum1 = new int[list.size()];//初始化里面的值全为0
		int[] wordNum2 = new int[list.size()];
		ListIterator<String> It=list.listIterator();
		while(It.hasNext()){
			String word = It.next();
			for(int m=0;m<array1.length;m++){
				if(word.equals(array1[m]))
					wordNum1[k]++;
//					wordNum1[k]=1;
//				    break;
			}
			for(int m=0;m<array2.length;m++){
				if(word.equals(array2[m]))
					wordNum2[k]++;
//					wordNum2[k]=1;
//				     break;
			}
			k++;
		}
		int numerator = 0;
		int denominator1 = 0;
		int denominator2 = 0;
		double sim =0.0;
		for(int m=0;m<list.size();m++){
			numerator+=wordNum1[m]*wordNum2[m];
			denominator1+=Math.pow(wordNum1[m],2);
			denominator2+=Math.pow(wordNum2[m],2);
			}
		if(Math.sqrt(denominator1)*Math.sqrt(denominator2)!=0){
		  sim = numerator/(Math.sqrt(denominator1)*Math.sqrt(denominator2)); //余弦相似度
		  if(sim>1){
			  sim=1.0;
		  }
		}
		//double sim = numerator/(Math.sqrt(denominator1)+Math.sqrt(denominator2)-numerator);//jacard相似�?
		//System.out.println(sim);	
		return sim;
	}
	/*
	 * 删除List中重复元元素*/
	public List removeDuplicateWithOrder(List list) {
	     Set set = new HashSet();  //集合具有唯一性，利用这一点特点我们可以确保元素的唯一�?
	      List newList = new ArrayList();
	   for (Iterator iter = list.iterator(); iter.hasNext();) {
	          Object element = iter.next();
	          if (set.add(element))
	             newList.add(element);
	       } 
	      list.clear();
	      list.addAll(newList);
		return list;
	}
	
	/*
	 * 并集
	 */
	public List<String> intersection(List<String>list1,List<String>list2){
		List<String>list3=new ArrayList<String>();
		list3.addAll(list1);
		list3.removeAll(list2);
		list3.addAll(list2);
	//	System.out.println("并集："+list3.size());
		return list3;
	}
	
	/*
	 * 交集
	 */
	public List<String> union(List<String>list1,List<String>list2){
		List<String>list=new ArrayList<String>();
		list.addAll(list1);
		list.retainAll(list2);
		//System.out.println("交集："+list.size());
		return list;
	}
	
	/*
	 * 找最大数
	 */
	 public double findmax(Double array[]){
		 double max=array[0];
		  for(int i=0;i<array.length;i++){
			 if(array[i]>max) max=array[i];
		  }
		 return max;
	 }
	 
    public static void main(String args[]) throws IOException{
    	String text=null;
    	long startTime=System.currentTimeMillis();  //获取开始时间
 	   String taggerPath = "models/english-left3words-distsim.tagger";
 	   MaxentTagger tagger = new MaxentTagger(taggerPath);
		try {
			FileInputStream fin=new FileInputStream("Data/text.txt");
			byte[] buf=new byte[1000000];//缓存的大小决定文本的大小
			int len=fin.read(buf);//从text.txt中读出内�?
			//System.out.println(new String(buf,0,len));
			text=new String(buf,0,len);
			//System.out.println(text);
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    TextProcessing Tp=new TextProcessing();
    TextSimilar Ts=new TextSimilar();
	String str="Formerly known as Canton or Kwangtung in English, Guangdong surpassed Henan and Sichuan to become the most populous province in China in January 2005, registering 79.1 million permanent residents and 31 million migrants who lived in the province for at least six months of the year.";
    List<String>one=Tp.SegmentProcess(text,tagger);
    List<String>two=Tp.SegmentProcess(str,tagger);
    System.out.println(Ts.compute(one,two));
    long endTime=System.currentTimeMillis(); //获取结束时间
	double minute=(endTime-startTime)/1000.0;
    System.out.println("程序运行时间： "+minute+"s");
    
   }
}
