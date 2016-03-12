package PageRank;
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Random; 
public class Tanspose {
	/*
	 * Ϊ���ö�ĳ���ڵ����Ҫ�Ի��ܣ������������ӵ��ü��ڵ�ıߣ���Ҫ�Ծ������ת��q=Gq��q��ʾ
	 * ��ʼ�������Ŷȣ�
	 */
  public List<List<Double>>transpose(List<List<Double>>list){
	  Double t;
	  //��i�У���j��
	  for(int i=0;i<list.size();i++){//��
		 //list.add(new ArrayList<Double>());
		  for(int j=0;j<list.get(i).size();j++){ //��
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
   * �����ʼ�����������ǵļ�������л���Ȩ�ؾ���wij/���е�ĺͣ�
   */
  public List<List<Double>> computer(List<List<Double>>list){
	  for(int i=0;i<list.size();i++){//��
		  /*
		   * ��ȡĳһ���ڵ�ĳ���
		   */
		   double sum=0.0;
		  for(int j=0;j<list.get(i).size();j++){//��
			  sum+=list.get(i).get(j);
		  }
		  
		  for(int j=0;j<list.get(i).size();j++){
			  double k=list.get(i).get(j);//��ȡi��j�е�Ԫ��
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
  	 * ����ͼ
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
      System.out.println("ԭʼ����");
      System.out.println(s);
      System.out.println("---------------------------------------------------------");
      List<List<Double>> list2 = new ArrayList<List<Double>>();
      list2=tr.computer(s);
      System.out.println("��ʼ������");
	  System.out.println(list2);
	//  System.out.println(list==s);
	  System.out.println("----------------------------------------------------------");
	  List<List<Double>> list1 = new ArrayList<List<Double>>();
	  list1=tr.transpose(list2);
	  System.out.println("ת�þ���");
	  System.out.println(list1);
	 // System.out.println(list1==s);
  }
 
}
