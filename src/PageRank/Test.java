package PageRank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 * 测试函数以及计算节点的出入度函数
 */
public class Test {
	public static void main(String args[]) {  
        String result=null;  
        ToMatrix cd=new ToMatrix();
        HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
//        List<Integer>outlist=new ArrayList<Integer>();
//        List<Integer>inlist=new ArrayList<Integer>();
        Object obj[] = { 'A', 'B', 'C', 'D', 'E', 'F' }; 
        //Graph graph = new MatrixGraph(obj);  
        Graph graph = new MatrixGraph(obj);  
        //graph.addVex('F');  
        graph.addEdge('A','C',0.5);  
        graph.addEdge('B','A',0.6);  
        graph.addEdge('C','B',0.1);  
        graph.addEdge('E','D',1);  
        graph.addEdge('F','E',1);  
        result=graph.printGraph();
        System.out.println(result);  
        System.out.println("矩阵：");
        System.out.println(cd.listToDouble(obj, result));
        System.out.println("入度：");
        System.out.println(cd.inDegree(obj, result)); 
        System.out.println("出度：");
        System.out.println(cd.outDegree(obj, result)); 
     //   System.out.println();  
     //   System.out.println(hash);  
     //   System.out.println(graph.exist('E','A'));
//        String []row=result.split("\n");
//        int len=row.length;
//       // System.out.println(len); 
//        String juzheng[][]=new String[len][];
//        //将前面返回的边存成数组的形式
//       for(int i=0;i<len;i++){
//    	   juzheng[i]=row[i].split(" ");
//    	  // System.out.println(row[i]);     
//       }
//       
//       //System.out.println(juzheng[0][0]);   
//      //计算顶点的出度 
//       for(int i=0;i<len;i++)
//       { int out=0;
//    	 for(int j=0;j<len;j++)
//          { if(juzheng[i][j].equals("1.0")){
//        	  out++;  
//          }
//    	   //System.out.print("  "+juzheng[i][j]);  
//           }
//    	   outlist.add(out); 
//            System.out.println(obj[i]+"的出度:"+out); 
//       } //end 出度
//       
//       System.out.println();
//      //计算顶点的入度
//       for(int j=0;j<len;j++)
//       { int in=0;
//    	for(int i=0;i<len;i++)
//          { if(juzheng[i][j].equals("1.0")){
//        	  in++;
//          }
//    	   //System.out.print("  "+juzheng[i][j]);  
//           }
//    	   inlist.add(in);
//            System.out.println(obj[j]+"的入度:"+in); 
//       } //end 入度
//       
//       System.out.println();
//       //计算节点的度（出度+入度）
//       for(int i=0;i<obj.length;i++)
//       {  
//    	   int account=outlist.get(i)+inlist.get(i);
//    	System.out.println(obj[i]+"的度:"+account);  
//       }
//       System.out.println(outlist.get(0));
//       System.out.println (inlist.get(0));  
    } //end main
}
