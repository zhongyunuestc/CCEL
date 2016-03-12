package PageRank;
public abstract class Edge implements Comparable{  
    protected double weight;  
    protected Object info;  
    /** 
     * ���캯�� 
     */  
    public Edge() {  
        this.weight = 0.0;  
    }  
    /** 
     * ���캯�� 
     * @param weight Ȩֵ 
     */  
    public Edge(double weight) {  
        this.weight = weight;  
    }  
    public Edge(Object info, double weight) {  
        this.weight = weight;  
        this.info = info;  
    }  
      
    /** 
     * ��ȡȨֵ 
     * @return Ȩֵ 
     */  
    public double getWeight() {  
        return weight;  
    }  
    /** 
     * ����Ȩֵ 
     * @param weight Ȩֵ 
     */  
    public void setWeight(double weight) {  
        this.weight = weight;  
    }  
    /** 
     * ��ȡ�ߵ���Ϣ 
     * @return �ߵ���Ϣ 
     */  
    public Object getInfo() {  
        return info;  
    }  
    /** 
     * ���ñߵ���Ϣ 
     * @param info���ߵ���Ϣ 
     */  
    public void setInfo(Object info) {  
        this.info = info;  
    }  
    /** 
     * ��ȡ�ߵĵ�һ������ 
     * @return ��һ������ 
     */  
    public abstract Object getFirstVertex();  
    /** 
     * ��ȡ�ߵĵڶ������� 
     * @return���ڶ������� 
     */  
    public abstract Object getSecondVertex();  
}
