package Similarity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TextProcessing {
	/**
	 * 分词并删除停用词,取里面的名词和命名实体当做特征向量词
	 * @throws UnsupportedEncodingException 
	*/
	public List<String> SegmentProcess(String zhaiYao, MaxentTagger tagger) throws UnsupportedEncodingException{
		
		String line=null;
		String result=null;
		HashSet<String> StopWord=new HashSet();//记录停用词
		ArrayList<String> WordList=new ArrayList();//用于存储特征词
		try {
			InputStream in = new FileInputStream("Data/StopWord.txt");//读取文件上的数据
			//指定编码方式，将字节流向字符流的转换
			InputStreamReader isr = new InputStreamReader(in,"UTF-8");
			//创建字符流缓冲区
			BufferedReader bufr = new BufferedReader(isr);
			while((line = bufr.readLine())!=null){
//				 System.out.println(line);
				StopWord.add(line);
			}
	        isr.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tagger tt=new Tagger();
	    try {
			result=tt.pos(zhaiYao,tagger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		String word[] =result.split("\\s+");
		/*
		 * 用文本中的实体和名词当特征
		 */
	   ArrayList<String>nounlist=new ArrayList<String>();//储存文本中的名词特征
		for(int i=0;i<word.length;i++){
			if(word[i].contains("NN")){
				// if(word[i].endsWith("/n")){
				String sub=word[i].substring(0, word[i].indexOf("/"));
			 if(!nounlist.contains(sub)&&sub.length()>1){
				  nounlist.add(sub);
			 }
		}
		}
	  	return nounlist;
	}
	
	/*
	 * 统计文本里面的关键字
	 */
	public List<String> selectKey(List<String>list){
		
		 List<String>endlist=new ArrayList<String>();
		  HashMap<String,Integer>hash=new HashMap<String,Integer>();
		  for(int i=0;i<list.size();i++){
			String str=list.get(i);
			int sum=0;
			for(int j=0;j<list.size();j++){
				if(str.equals(list.get(j)))
					sum++;
				}//end for
			hash.put(str, sum);
			//if(sum>4) endlist.add(str);
		}//end for	 
		  
  List<Map.Entry<String, Integer>> infoIds =new ArrayList<Map.Entry<String, Integer>>(hash.entrySet());

 //排序  
  Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {  
   public int compare(Map.Entry<String, Integer> o1,  
           Map.Entry<String, Integer> o2) {  
       return ( o1.getValue()-o2.getValue());  
     }  
  });  
  
  if(infoIds.size()>8){//有些页面的文本很短
	  for (int i=infoIds.size()-1;i>infoIds.size()-7;i--) {  
		    Entry<String,Integer> ent=infoIds.get(i);  
		    endlist.add(ent.getKey());
		 //  System.out.println(ent.getKey()+"="+ent.getValue());       
	 }   
  }
  else{
	  for (int i=infoIds.size()-1;i>=0;i--) {  
		    Entry<String,Integer> ent=infoIds.get(i);  
		    endlist.add(ent.getKey());
		    //System.out.println(ent.getKey()+"="+ent.getValue());       
	 }   
   }
   return endlist;
	}
	/**
	 * 删除List中重复元素
	*/
	public List removeDuplicateWithOrder(List list) {
	     Set set = new HashSet();  //集合具有唯一性，利用这一点特点我们可以确保元素的唯一性
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
	
	 public static void main(String[]arg) throws IOException{
		   String taggerPath = "models/english-left3words-distsim.tagger";
		   MaxentTagger tagger = new MaxentTagger(taggerPath);
		    String str="Zheng Jie (born 5 July 1983) is a Chinese professional tennis player. Her career high ranking is World No. 15 which she achieved on 18 May 2009. As of 9 June 2014, Zheng is ranked World No. 90 in singles and World No. 22 in doubles and is the 4th ranked Chinese player.Zheng is one of the most successful tennis players from China. She has won four WTA singles titles – Hobart in 2005, Estoril, Stockholm in 2006, and Auckland in 2012. She has also won fifteen doubles titles, eleven of them with Yan Zi including Wimbledon and the Australian Open in 2006. She won the bronze medal in doubles with Yan Zi at the 2008 Beijing Olympics. Her career high doubles ranking is World No. 3. Zheng has reached the singles semi-finals at the 2008 Wimbledon Championships, defeating a World No. 1, Ana Ivanovic, in the process, becoming the first Chinese female player to advance to the semi-finals at a Grand Slam. She also advanced to the semi-finals at the 2010 Australian Open.";
		    List<String>result=new ArrayList<String>();
		    TextProcessing tp=new TextProcessing();
		    result=tp.SegmentProcess(str,tagger);
		    System.out.println(result);
	   }


}
