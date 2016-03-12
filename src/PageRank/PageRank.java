package PageRank;
/*
  求解G的特征向量可以通过q(next)=G*q(cur)这样不断迭代获得，已经证明，q(next)与q(cur)
  最终会收敛。刚开始q可以去一个随机的向量这里，通过编程求解pagerank，取alpha=0.85，通过
  不断的迭代，当q(next)和q(cur)之间的距离小于0.0000001时，认为已经收敛。pagerank就是
  特征值为1的特征向量，1,2,3,4号网页的价值分别为特征向量中对应维的值。
  矩阵可以用二层list表示，向量用一层list表示。
 */
import java.util.ArrayList;  
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;  
import java.util.Random;  
  
public class PageRank {  
    public static final double ALPHA = 0.85;  //阻尼系数
    public static final double DISTANCE = 0.1;//收敛条件 |q(next)-q(cur)|<DISTANCE;可调节
  
    public static void main(String[] args) {  
        // List<Double> q1=getInitQ(4);  
        System.out.println("alpha的值为: " + ALPHA);  
        List<List<Double>>wlist=new ArrayList<List<Double>>();
        /*
         * 在我们的方法中，节点的初始化值选择edit distance/similarly/popularity等等 
         * 0.402,0.308,0.0492,0.155,0.0208,0.13,0.145
         */
        List<Double>q1= new ArrayList<Double>(); //存储初始化节点重要性
        q1.add(new Double(0.402));  
        q1.add(new Double(1.0));  
        q1.add(new Double(0.0492));  
        q1.add(new Double(0.155));  
        
        q1.add(new Double(0.0208));  
        q1.add(new Double(0.13));  
        q1.add(new Double(0.145));    
        System.out.println("初始的向量q为:");  
        printVec(q1);  
      //   System.out.println(getU(q1));
      //  getS(); 
     //   printMatrix(getG(ALPHA));  
        List<Double> pageRank = calPageRank(wlist,q1, q1,ALPHA);  
        System.out.println("PageRank为:");  
        printVec(pageRank);  
        System.out.println();  
    }  
  
    /** 
     * 打印输出一个矩阵 ，相当于一个二维数组。双层list，
     * 第一层list相当与行，第二层list相当于列
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
     * 打印输出一个向量 
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
     * 获得一个初始的随机向量q 
     *  
     * @param n 
     *            向量q的维数 
     * @return 一个随机的向量q，每一维是0-5之间的随机数 
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
     * 计算两个向量的距离 ，距离公式计算
     *  
     * @param q1 
     *            第一个向量 
     * @param q2 
     *            第二个向量 
     * @return 它们的距离 
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
     * 计算pagerank 
     *  
     * @param q1 
     *            初始向量 
     * @param a 
     *            alpha的值 
     * @return pagerank的结果 
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
     * 计算获得初始的G矩阵  G=aS+U*(1-a)/n
     *  
     * @param a 
     *            为alpha的值，0.85 
     * @return 初始矩阵G 
     */  
    public static List<List<Double>> getG(List<List<Double>>weilist,List<Double>sim,double a) {  

      //  int n = getS().size();  
        List<List<Double>> aS = numberMulMatrix(getS(weilist), a);  
        List<List<Double>> nU = numberMulMatrix(getU(sim), (1 - a));  
        List<List<Double>> g = addMatrix(aS, nU);  
//        System.out.println("初始的矩阵G为:"); 
//        System.out.println("g="+g);
        return g;  
    }  
  
    /** 
     * 计算一个矩阵乘以一个向量 
     *  
     * @param m 
     *            一个矩阵 
     * @param v 
     *            一个向量 
     * @return 返回一个新的向量 
     */  
    public static List<Double> vectorMulMatrix(List<List<Double>> m,  
            List<Double> v) {  
        if (m == null || v == null || m.size() <= 0  
                || m.get(0).size() != v.size()) {  //矩阵的行数不等于向量的列数
            return null;  
        }  
  
        List<Double> list = new ArrayList<Double>();  
        for (int i = 0; i < m.size(); i++) {  
            double sum = 0;  
            for (int j = 0; j < m.get(i).size(); j++) {  //行数与列数对应相乘
                double temp = m.get(i).get(j).doubleValue()  
                        * v.get(j).doubleValue();  
                sum += temp;  
            }  
            list.add(sum);  
        }  
  
        return list;  
    }  
  
    /** 
     * 计算两个矩阵的和 
     *  
     * @param list1 
     *            第一个矩阵 
     * @param list2 
     *            第二个矩阵 
     * @return 两个矩阵的和 
     * 同型矩阵才可以相加
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
     * 计算一个数乘以矩阵 
     *  
     * @param s 
     *            矩阵s 
     * @param a 
     *            double类型的数 
     * @return 一个新的矩阵 
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
     * 初始化S矩阵 
     * 行表出链。列表入链
     * 权重矩阵,转移
     *  
     * @return S 
     */  
    public static List<List<Double>> getS(List<List<Double>> s) {  
    	/*
    	 * 构建图
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
         * 转置和初始化值（在我们的计算过程中要换成权重矩阵）
         */
        Tanspose tan=new Tanspose();
        List<List<Double>> list1= new ArrayList<List<Double>>();
        list1=tan.computer(s);
        List<List<Double>> list2= new ArrayList<List<Double>>();
        list2=tan.transpose(list1);
      //  System.out.println("转移矩阵："+list2);
        return list2;  
    }  
  
    /** 
     * 初始化U矩阵 ,节点的相似度矩阵
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
     * 返回相似度计算的列向量
     */
    public static List<Double>getSimMatrix(){
    	List<Double>sim=Arrays.asList(0.402,1.0,0.0492,0.155,0.0208,0.13,0.145);
    	 return sim;
    }
}  
/*
 * 采用并行化Hadoop的PageRank算法对于解决大量节点的效率问题有实际意思？？？
 */
