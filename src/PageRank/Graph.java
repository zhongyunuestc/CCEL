package PageRank;


public interface Graph {
	 /** 
     * ��Ӷ��� 
     * @param v 
     */  
    public void addVex(Object v);  
    /** 
     * ��ӱ� 
     * @param v1 ��һ������ 
     * @param v2 �ڶ������� 
     * @param weight Ȩֵ 
     */  
    public void addEdge(Object v1, Object v2, double weight);  
    /** 
     * ��ӱ� 
    * @param v1 ��һ������ 
     * @param v2 �ڶ������� 
     * @param info ����Ϣ 
     * @param weight Ȩֵ 
     */  
    public void addEdge(Object v1, Object v2, Object info, double weight);  
    /** 
     * �ÿ�ͼ 
     */  
    public void clear();  
    /** 
     * ��ö���v�ĵ�һ���ڽӽ�� 
     * @param v ���� 
     * @return ����v�ĵ�һ���ڽӽ�� 
     */  
    public Object getFirstVertex(Object v);  
    /** 
     * ��ͼG��Ѱ��v1�����ڽӽ��v2����һ���ڽӽ�� 
     * @param v1 ���� 
     * @param v2 v1��һ���ڽӽ�� 
     * @return �ڽ�v1����v2���һ����� 
     */  
    public Object getNextVertex(Object v1, Object v2);  
    /** 
     * ��ö���ĸ��� 
     * @return 
     */  
    public int getVertexSize();  
    /** 
     *��ñߵ����� 
     * @return 
     */  
    public int getEdgeSize();  
    /** 
     * �Ƴ����� 
     * @param v ���� 
     */  
    public void removeVex(Object v);  
    /** 
     * �Ƴ��� 
     * @param v1 ����1 
     * @param v2 ����2 
     */  
    public void removeEdge(Object v1, Object v2);  
    /** 
     * ������ȱ��� 
     * @param o �����ĳ�ʼ���� 
     * @return �����Ľ�� 
     */  
    public String dfs(Object o);  
    /** 
     * ������ȱ��� 
     * @param o �����ĳ�ʼ���� 
     * @return �����Ľ�� 
     */  
    public String bfs(Object o);  
    /** 
     * ��ӡͼ�ĸ����� 
     * @return 
     */  
    public String printGraph();
    
    public boolean exist(Object v1, Object v2);
}
