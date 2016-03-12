package PageRank;
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Random; 
public class Tanspose {
	/*
	 * 为了让对某个节点的重要性汇总（引入其他链接到该及节点的边，需要对矩阵进行转置q=Gq，q表示
	 * 初始化的置信度）
	 */
  public List<List<Double>>transpose(List<List<Double>>list){
	  Double t;
	  //第i行，第j列
	  for(int i=0;i<list.size();i++){//行
		 //list.add(new ArrayList<Double>());
		  for(int j=0;j<list.get(i).size();j++){ //列
			  if(i<j){
				t=list.get(i).get(j);
				list.get(i).set(j, list.get(j).get(i));
				list.get(j).set(i, t);
			 }//end if
		 } //end for
	  }//end for
	  return list;
  }//end transpose
  
  /*
   * 计算初始化矩阵，在我们的计算过程中换成权重矩阵（wij/所有点的和）
   */
  public List<List<Double>> computer(List<List<Double>>list){
	  for(int i=0;i<list.size();i++){//行
		  /*
		   * 获取某一个节点的出度
		   */
		   double sum=0.0;
		  for(int j=0;j<list.get(i).size();j++){//列
			  sum+=list.get(i).get(j);
		  }
		  
		  for(int j=0;j<list.get(i).size();j++){
			  double k=list.get(i).get(j);//获取i行j列的元素
			  if(sum!=0){
				 list.get(i).set(j, k/sum);
			  }
			 
		  }
	  }
	  return list;
  }
  public static void main(String[]arg){
	  Tanspose tr=new Tanspose(); 
	  /*
  	 * 构建图
  	 */
  	String result=null;  
      ToMatrix cd=new ToMatrix();
      HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
      Object obj[] = { 'A', 'B', 'C', 'D'};  
      Graph graph = new MatrixGraph(obj);   
      graph.addEdge('A','C',0.5);   
      graph.addEdge('C','B',0.4);  
      result=graph.printGraph();
      List<List<Double>> s=cd.listToDouble(obj, result);
      System.out.println("原始矩阵：");
      System.out.println(s);
      System.out.println("---------------------------------------------------------");
      List<List<Double>> list2 = new ArrayList<List<Double>>();
      list2=tr.computer(s);
      System.out.println("初始化矩阵：");
	  System.out.println(list2);
	//  System.out.println(list==s);
	  System.out.println("----------------------------------------------------------");
	  List<List<Double>> list1 = new ArrayList<List<Double>>();
	  list1=tr.transpose(list2);
	  System.out.println("转置矩阵：");
	  System.out.println(list1);
	 // System.out.println(list1==s);
  }
 
}
