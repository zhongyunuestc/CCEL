package PageRank;

/** 
 * �ڽӾ����ʾ����ʾ��ͼ 
 * @author
 * 
 */  
public class MatrixEdge extends Edge {  
    private Object v1, v2;  
    /** 
     * ���캯�� 
     * @param weight Ȩֵ 
     */  
    public MatrixEdge(double weight) {  
        v1 = null;  
        v2 = null;  
        this.info = null;  
        this.weight = weight;  
    }  
    /** 
     * ���캯�� 
     * @param v1 ��һ������ 
     * @param v2 �ڶ������� 
     * @param info �ߵ���Ϣ 
     * @param weight Ȩֵ 
     */  
    public MatrixEdge( Object v1, Object v2, Object info, double weight ) {  
        super(info, weight);  
        this.v1 = v1;  
        this.v2 = v2;  
    }  
    @Override  
    public Object getFirstVertex() {  
        return v1;  
    }  
  
    @Override  
    public Object getSecondVertex() {  
        return v2;  
    }  
  
    public int compareTo(Object o) {  
        Edge e = (Edge)o;  
        if(this.weight > e.getWeight())  
            return 1;  
        else if(this.weight < e.getWeight())  
            return -1;  
        else  
            return 0;  
    }  
    @Override  
    public boolean equals(Object obj) {  
        return ((Edge)obj).info.equals(info) &&  ((Edge)obj).getWeight() == this.weight;  
    }  
    @Override  
    public String toString() {  
        //return "����Ϣ��" + info + "\tȨֵ��" + weight + "\t����1:" + getFirstVertex() + "\t����2��" + getSecondVertex();  
        return "" + weight;  
    }  
}  