package PageRank;
/*
  ���G��������������ͨ��q(next)=G*q(cur)�������ϵ�����ã��Ѿ�֤����q(next)��q(cur)
  ���ջ��������տ�ʼq����ȥһ��������������ͨ��������pagerank��ȡalpha=0.85��ͨ��
  ���ϵĵ�������q(next)��q(cur)֮��ľ���С��0.0000001ʱ����Ϊ�Ѿ�������pagerank����
  ����ֵΪ1������������1,2,3,4����ҳ�ļ�ֵ�ֱ�Ϊ���������ж�Ӧά��ֵ��
  ��������ö���list��ʾ��������һ��list��ʾ��
 */
import java.util.ArrayList;  
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;  
import java.util.Random;  
  
public class PageRank {  
    public static final double ALPHA = 0.85;  //����ϵ��
    public static final double DISTANCE = 0.1;//�������� |q(next)-q(cur)|<DISTANCE;�ɵ���
  
    public static void main(String[] args) {  
        // List<Double> q1=getInitQ(4);  
        System.out.println("alpha��ֵΪ: " + ALPHA);  
        List<List<Double>>wlist=new ArrayList<List<Double>>();
        /*
         * �����ǵķ����У��ڵ�ĳ�ʼ��ֵѡ��edit distance/similarly/popularity�ȵ� 
         * 0.402,0.308,0.0492,0.155,0.0208,0.13,0.145
         */
        List<Double>q1= new ArrayList<Double>(); //�洢��ʼ���ڵ���Ҫ��
        q1.add(new Double(0.402));  
        q1.add(new Double(1.0));  
        q1.add(new Double(0.0492));  
        q1.add(new Double(0.155));  
        
        q1.add(new Double(0.0208));  
        q1.add(new Double(0.13));  
        q1.add(new Double(0.145));    
        System.out.println("��ʼ������qΪ:");  
        printVec(q1);  
      //   System.out.println(getU(q1));
      //  getS(); 
     //   printMatrix(getG(ALPHA));  
        List<Double> pageRank = calPageRank(wlist,q1, q1,ALPHA);  
        System.out.println("PageRankΪ:");  
        printVec(pageRank);  
        System.out.println();  
    }  
  
    /** 
     * ��ӡ���һ������ ���൱��һ����ά���顣˫��list��
     * ��һ��list�൱���У��ڶ���list�൱����
     *  
     * @param m 
     */  
    public static void printMatrix(List<List<Double>> m) {  
        for (int i = 0; i < m.size(); i++) {  
            for (int j = 0; j < m.get(i).size(); j++) {  
                System.out.print(m.get(i).get(j) + ", ");  
            }  
            System.out.println();  
        }  
    }  
  
    /** 
     * ��ӡ���һ������ 
     *  
     * @param v 
     */  
    public static void printVec(List<Double> v) {  
        for (int i = 0; i < v.size(); i++) {  
            System.out.print(v.get(i) + ", ");  
        }  
        System.out.println();  
    }  
  
    /** 
     * ���һ����ʼ���������q 
     *  
     * @param n 
     *            ����q��ά�� 
     * @return һ�����������q��ÿһά��0-5֮�������� 
     */  
    public static List<Double> getInitQ(int n) {  
        Random random = new Random();  
        List<Double> q = new ArrayList<Double>();  
        for (int i = 0; i < n; i++) {  
            q.add(new Double(5 * random.nextDouble()));  
        }  
        return q;  
    }  
  
    /** 
     * �������������ľ��� �����빫ʽ����
     *  
     * @param q1 
     *            ��һ������ 
     * @param q2 
     *            �ڶ������� 
     * @return ���ǵľ��� 
     */  
    public static double calDistance(List<Double> q1, List<Double> q2) {  
        double sum = 0;  
  
        if (q1.size() != q2.size()) {  
            return -1;  
        }  
  
        for (int i = 0; i < q1.size(); i++) {  
            sum += Math.pow(q1.get(i).doubleValue() - q2.get(i).doubleValue(),  
                    2);  
        }  
        return Math.sqrt(sum);  
    }  
  
    /** 
     * ����pagerank 
     *  
     * @param q1 
     *            ��ʼ���� 
     * @param a 
     *            alpha��ֵ 
     * @return pagerank�Ľ�� 
     */  
    public static List<Double> calPageRank(List<List<Double>>wlist,List<Double>sim,List<Double> q1, double a) {  
  
        List<List<Double>> g = getG(wlist,sim,a);  
        List<Double> q = null;  
       // boolean flag=true;
        while (true) {  
            q = vectorMulMatrix(g, q1);  
            double dis = calDistance(q, q1);  
          //  System.out.println(dis);  
            if (dis <= DISTANCE) {  
             //   System.out.println("q1:");  
              //  printVec(q1);  
            //    System.out.println("q:");  
            //    printVec(q);  
                break;  
            }  
            q1 = q;  
        }  
        return q;  
    }  
  
    /** 
     * �����ó�ʼ��G����  G=aS+U*(1-a)/n
     *  
     * @param a 
     *            Ϊalpha��ֵ��0.85 
     * @return ��ʼ����G 
     */  
    public static List<List<Double>> getG(List<List<Double>>weilist,List<Double>sim,double a) {  

      //  int n = getS().size();  
        List<List<Double>> aS = numberMulMatrix(getS(weilist), a);  
        List<List<Double>> nU = numberMulMatrix(getU(sim), (1 - a));  
        List<List<Double>> g = addMatrix(aS, nU);  
//        System.out.println("��ʼ�ľ���GΪ:"); 
//        System.out.println("g="+g);
        return g;  
    }  
  
    /** 
     * ����һ���������һ������ 
     *  
     * @param m 
     *            һ������ 
     * @param v 
     *            һ������ 
     * @return ����һ���µ����� 
     */  
    public static List<Double> vectorMulMatrix(List<List<Double>> m,  
            List<Double> v) {  
        if (m == null || v == null || m.size() <= 0  
                || m.get(0).size() != v.size()) {  //�������������������������
            return null;  
        }  
  
        List<Double> list = new ArrayList<Double>();  
        for (int i = 0; i < m.size(); i++) {  
            double sum = 0;  
            for (int j = 0; j < m.get(i).size(); j++) {  //������������Ӧ���
                double temp = m.get(i).get(j).doubleValue()  
                        * v.get(j).doubleValue();  
                sum += temp;  
            }  
            list.add(sum);  
        }  
  
        return list;  
    }  
  
    /** 
     * ������������ĺ� 
     *  
     * @param list1 
     *            ��һ������ 
     * @param list2 
     *            �ڶ������� 
     * @return ��������ĺ� 
     * ͬ�;���ſ������
     */  
    public static List<List<Double>> addMatrix(List<List<Double>> list1,  
            List<List<Double>> list2) {  
        List<List<Double>> list = new ArrayList<List<Double>>();  
        if (list1.size() != list2.size() || list1.size() <= 0  
                || list2.size() <= 0) {  
            return null;  
        }  
        for (int i = 0; i < list1.size(); i++) {  
            list.add(new ArrayList<Double>());  
            for (int j = 0; j < list1.get(i).size(); j++) {  
                double temp = list1.get(i).get(j).doubleValue()  
                        + list2.get(i).get(j).doubleValue();  
                list.get(i).add(new Double(temp));  
            }  
        }  
        return list;  
    }  
  
    /** 
     * ����һ�������Ծ��� 
     *  
     * @param s 
     *            ����s 
     * @param a 
     *            double���͵��� 
     * @return һ���µľ��� 
     */  
    public static List<List<Double>> numberMulMatrix(List<List<Double>> s,  
            double a) {  
        List<List<Double>> list = new ArrayList<List<Double>>();  
  
        for (int i = 0; i < s.size(); i++) {  
            list.add(new ArrayList<Double>());  
            for (int j = 0; j < s.get(i).size(); j++) {  
                double temp = a * s.get(i).get(j).doubleValue();  
                list.get(i).add(new Double(temp));  
            }  
        }  
        return list;  
    }  
  
    /** 
     * ��ʼ��S���� 
     * �б�������б�����
     * Ȩ�ؾ���,ת��
     *  
     * @return S 
     */  
    public static List<List<Double>> getS(List<List<Double>> s) {  
    	/*
    	 * ����ͼ
    	 */
//    	String result=null;  
//        ToMatrix cd=new ToMatrix();
//        HashMap<Object,Integer>hash=new HashMap<Object,Integer>();
//        Object obj[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G'};  
//        Graph graph = new MatrixGraph(obj);   
//        graph.addEdge('A','B',0.2759);   
//        graph.addEdge('A','C',0.4167);
//        graph.addEdge('C','B',0.3607);
//        graph.addEdge('A','G',0.00138);
//        graph.addEdge('A','F',0.1154);
//        graph.addEdge('A','D',0.2285);
//        graph.addEdge('C','D',0.2609);
//        graph.addEdge('D','B',0.01935);
//        graph.addEdge('E','B',0.0);
//        result=graph.printGraph();
//        List<List<Double>> s=cd.listToDouble(obj, result);
        /*
         * ת�úͳ�ʼ��ֵ�������ǵļ��������Ҫ����Ȩ�ؾ���
         */
        Tanspose tan=new Tanspose();
        List<List<Double>> list1= new ArrayList<List<Double>>();
        list1=tan.computer(s);
        List<List<Double>> list2= new ArrayList<List<Double>>();
        list2=tan.transpose(list1);
      //  System.out.println("ת�ƾ���"+list2);
        return list2;  
    }  
  
    /** 
     * ��ʼ��U���� ,�ڵ�����ƶȾ���
     *  
     * @return U 
     */  
    public static List<List<Double>> getU(List<Double>list) {  
       List<List<Double>> s = new ArrayList<List<Double>>(); 
    	for(int i=0;i<list.size();i++){
    		 List<Double> row= new ArrayList<Double>(); 
    		for(int j=0;j<list.size();j++){
    			row.add(0.0);
    		}
    		s.add(row);  
    	}
    	
    	for(int i=0;i<list.size();i++){
    		s.get(i).set(i, list.get(i));
    	}
        return s;  
    }  
    
    
    /*
     * �������ƶȼ����������
     */
    public static List<Double>getSimMatrix(){
    	List<Double>sim=Arrays.asList(0.402,1.0,0.0492,0.155,0.0208,0.13,0.145);
    	 return sim;
    }
}  
/*
 * ���ò��л�Hadoop��PageRank�㷨���ڽ�������ڵ��Ч��������ʵ����˼������
 */
