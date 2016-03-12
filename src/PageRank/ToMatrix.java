package PageRank;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ToMatrix {
   public HashMap<Object,Integer>computer(Object[]obj,String Matrix){
	   List<Integer>outlist=new ArrayList<Integer>();
       List<Integer>inlist=new ArrayList<Integer>();
       HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
      // List<String>ilist=new ArrayList<String>();
       //��ǰ�淵�ص��ַ�������������ʽ
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      //System.out.println(juzheng[0][0]);   
     //���㶥��ĳ��� 
      for(int i=0;i<len;i++)
      { int out=0;
       	for(int j=0;j<len;j++)
         { 
         if(juzheng[i][j].equals("1.0")){
       	  out++;
          }
   	   //System.out.print("  "+juzheng[i][j]);  
          }
           outlist.add(out);
           System.out.println(obj[i]+"�ĳ���:"+out); 
      } //end ���� 
      System.out.println();
     //���㶥������
      for(int j=0;j<len;j++)
      { int in=0;
      	for(int i=0;i<len;i++)
         { if(juzheng[i][j].equals("1.0")){
       	  in++;
         }
   	   //System.out.print("  "+juzheng[i][j]);  
          }
   	       inlist.add(in);
           System.out.println(obj[j]+"�����:"+in); 
      } //end ���
      System.out.println();
      //����ڵ�Ķȣ�����+��ȣ�
      for(int i=0;i<obj.length;i++)
      {  
   	    int account=outlist.get(i)+inlist.get(i);
   	    hash.put(obj[i], account);
     	System.out.println(obj[i]+"�Ķ�:"+account);  
     	
      }
	   return hash;
   }
   
   public List<List<Double>>listToDouble(Object[]obj,String Matrix){
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
       //����ʾ�У��ڴ��ʾ���е�һ��
       List<List<String>>olist=new ArrayList<List<String>>();
       List<List<Double>>dolist=new ArrayList<List<Double>>();
      // List<String>ilist=new ArrayList<String>();
       //��ǰ�淵�ص��ַ�������������ʽ
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	   olist.add(Arrays.asList(juzheng[i]));
   	  // System.out.println(row[i]);     
      }
      
      /*
       * ��Stringת��ΪDouble������
       */
      for(int i=0;i<olist.size();i++){
    	  List<Double>ilist=new ArrayList<Double>();
    	  for(int j=0;j<olist.get(0).size();j++){
    		  if(olist.get(i).get(j)!=null){
    			  ilist.add(Double.parseDouble(olist.get(i).get(j)));
    		  }
    		  else ilist.add(0.0);
    	  }
    	   dolist.add(ilist);
      }
      return dolist;
   }
   
   
   public HashMap<Object,Integer>outDegree(Object[]obj,String Matrix){
	   HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
      // List<String>ilist=new ArrayList<String>();
       //��ǰ�淵�ص��ַ�������������ʽ
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      
      //System.out.println(juzheng[0][0]);   
     //���㶥��ĳ��� 
      for(int i=0;i<len;i++)
      { int out=0;
     	for(int j=0;j<len;j++)
          { if(juzheng[i][j].equals("1.0")){
       	    out++;
           }
   	   //System.out.print("  "+juzheng[i][j]);  
           }
            hash.put(obj[i], out);
      } //end ���� 
      
	   return hash;
   }
   
   public HashMap<Object,Integer>inDegree(Object[]obj,String Matrix){
	   HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
      // List<String>ilist=new ArrayList<String>();
       //��ǰ�淵�ص��ַ�������������ʽ
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      
      //System.out.println(juzheng[0][0]);   
     //���㶥��ĳ��� 
      for(int j=0;j<len;j++)
      { int in=0;
      	for(int i=0;i<len;i++)
         { if(juzheng[i][j].equals("1.0")){
       	    in++;
         }
   	   //System.out.print("  "+juzheng[i][j]);  
          }
   	     hash.put(obj[j],in); 
      } //end ���
      
	   return hash;
   }
   
   public static void main(String arg[]){
	   
   }
}
