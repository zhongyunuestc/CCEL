package EntityWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Rentity {
	
	/*
	 * ����ʵ��֮�佨���ߵ�ʱ�򣬿���һ��ʵ���ڲ��ڵ�ǰ���ʵ��Ļ������ڣ�����50������֮�࣬���ڴ�С100�����ʣ�
	 *  ����������ʽ��ȡ�Ӵ��ķ���
	 * Pattern p=Pattern.compile("cd");
	   Matcher matcher=p.matcher("abcdefghijklmnabcd");
	   while(matcher.find()){
	   System.out.println(matcher.start());
	 */
	public List<Integer>getLocation(String text,String name){
		List<Integer>ilist=new ArrayList<Integer>();
		/*
		 * ��������ǰһ��ȡ��name��λ�ÿ�ʼ����
		 */
//		 for(int i=0;i<text.length();i++){
//			   i = text.indexOf(name,i);
//			   if(i<0)  break;
//			 //  System.out.println(i);
//			   ilist.add(i);
//	  }
		  Pattern p=Pattern.compile(name);
		  Matcher matcher=p.matcher(text);
		  while(matcher.find()){
//		  System.out.println(matcher.start());
		   ilist.add(matcher.start());
		   }
		   
		// System.out.println(ilist);
		 return ilist;
	}
	
	 public boolean iscontain(String en1,String text,String en2){
		 List<Integer>indexlist=getLocation(text,en1);
		 boolean flag=false;
		 for(int i=0;i<indexlist.size();i++){
			 int start=indexlist.get(i)-250;
			  if(start<0){
				  start=0;
			  }
			 int end=indexlist.get(i)+250;
			  if(end>text.length()-1){
				  end=text.length()-1;
			  }
			  
			   if(text.substring(start,end).contains(en2)){
				   flag=true;
				   break;
			   }
		 }
		 return flag;
		 
	 }
	
	public static void main(String[] args) {
		Rentity fd=new Rentity();
		 String en1="National League for Democracy";
		 String en2="NLD";
		 String text=" In May the government launched a sweeping crackdown on the democracy movement , detaining over 260 members of Suu Kyi 's National League for Democracy ( NLD ) ahead of a party congress .  Most were released but several dozen remain in custody .";
		 System.out.println(fd.iscontain(en1, text, en2));
	}
}
