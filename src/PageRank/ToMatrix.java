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
       //将前面返回的字符串存成数组的形式
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      //System.out.println(juzheng[0][0]);   
     //计算顶点的出度 
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
           System.out.println(obj[i]+"的出度:"+out); 
      } //end 出度 
      System.out.println();
     //计算顶点的入度
      for(int j=0;j<len;j++)
      { int in=0;
      	for(int i=0;i<len;i++)
         { if(juzheng[i][j].equals("1.0")){
       	  in++;
         }
   	   //System.out.print("  "+juzheng[i][j]);  
          }
   	       inlist.add(in);
           System.out.println(obj[j]+"的入度:"+in); 
      } //end 入度
      System.out.println();
      //计算节点的度（出度+入度）
      for(int i=0;i<obj.length;i++)
      {  
   	    int account=outlist.get(i)+inlist.get(i);
   	    hash.put(obj[i], account);
     	System.out.println(obj[i]+"的度:"+account);  
     	
      }
	   return hash;
   }
   
   public List<List<Double>>listToDouble(Object[]obj,String Matrix){
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
       //外层表示行，内存表示其中的一行
       List<List<String>>olist=new ArrayList<List<String>>();
       List<List<Double>>dolist=new ArrayList<List<Double>>();
      // List<String>ilist=new ArrayList<String>();
       //将前面返回的字符串存成数组的形式
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	   olist.add(Arrays.asList(juzheng[i]));
   	  // System.out.println(row[i]);     
      }
      
      /*
       * 将String转化为Double型数组
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
       //将前面返回的字符串存成数组的形式
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      
      //System.out.println(juzheng[0][0]);   
     //计算顶点的出度 
      for(int i=0;i<len;i++)
      { int out=0;
     	for(int j=0;j<len;j++)
          { if(juzheng[i][j].equals("1.0")){
       	    out++;
           }
   	   //System.out.print("  "+juzheng[i][j]);  
           }
            hash.put(obj[i], out);
      } //end 出度 
      
	   return hash;
   }
   
   public HashMap<Object,Integer>inDegree(Object[]obj,String Matrix){
	   HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
	   String []row=Matrix.split("\n");
       int len=row.length;
       String juzheng[][]=new String[len][];
      // List<String>ilist=new ArrayList<String>();
       //将前面返回的字符串存成数组的形式
      for(int i=0;i<len;i++){
   	   juzheng[i]=row[i].split(" ");
   	  // System.out.println(row[i]);     
      }
      
      //System.out.println(juzheng[0][0]);   
     //计算顶点的出度 
      for(int j=0;j<len;j++)
      { int in=0;
      	for(int i=0;i<len;i++)
         { if(juzheng[i][j].equals("1.0")){
       	    in++;
         }
   	   //System.out.print("  "+juzheng[i][j]);  
          }
   	     hash.put(obj[j],in); 
      } //end 入度
      
	   return hash;
   }
   
   public static void main(String arg[]){
	   
   }
}
