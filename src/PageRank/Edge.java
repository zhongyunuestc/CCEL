package PageRank;
public abstract class Edge implements Comparable{  
    protected double weight;  
    protected Object info;  
    /** 
     * 构造函数 
     */  
    public Edge() {  
        this.weight = 0.0;  
    }  
    /** 
     * 构造函数 
     * @param weight 权值 
     */  
    public Edge(double weight) {  
        this.weight = weight;  
    }  
    public Edge(Object info, double weight) {  
        this.weight = weight;  
        this.info = info;  
    }  
      
    /** 
     * 获取权值 
     * @return 权值 
     */  
    public double getWeight() {  
        return weight;  
    }  
    /** 
     * 设置权值 
     * @param weight 权值 
     */  
    public void setWeight(double weight) {  
        this.weight = weight;  
    }  
    /** 
     * 获取边的信息 
     * @return 边的信息 
     */  
    public Object getInfo() {  
        return info;  
    }  
    /** 
     * 设置边的信息 
     * @param info　边的信息 
     */  
    public void setInfo(Object info) {  
        this.info = info;  
    }  
    /** 
     * 获取边的第一个顶点 
     * @return 第一个顶点 
     */  
    public abstract Object getFirstVertex();  
    /** 
     * 获取边的第二个顶点 
     * @return　第二个顶点 
     */  
    public abstract Object getSecondVertex();  
}
