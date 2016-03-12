package PageRank;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/** 
 * �ڽӾ��󷨱�ʾͼ 
 * @author  
 * 
 */  
public class MatrixGraph implements Graph {  
    private static final int defaultSize = 100;  
    private int maxLen;  //�������󳤶�  
    private int edgeNum; //�ߵ�����   
    private List vertexs;  //�����б�
    private Edge edges[][]; //�ߵ����飨��ʾ�����Ĺ�ϵ��
      
  //  private enum Visit{unvisited, visited};  
      
    /** 
     * ���캯�� 
     */  
    public MatrixGraph() {  
        maxLen = defaultSize;  
        vertexs  = new ArrayList();  
        edges = new MatrixEdge[maxLen][maxLen];  
    }  
    /** 
     * ���캯�� 
     * @param vexs ��������� 
     */  
    public MatrixGraph(Object vexs[]) {  
        maxLen = vexs.length;  
        vertexs  = new ArrayList();  
        edges = new MatrixEdge[maxLen][maxLen];  
        for(int i=0; i<maxLen; i++) {  
            vertexs.add(vexs[i]);  
        }  
    }  
    public void addEdge(Object v1, Object v2, double weight) {  
    	int i1 = vertexs.indexOf(v1);  
        int i2 = vertexs.indexOf(v2);  
        boolean flag=true;
        /*
         * ���ȴ��ڱߣ�flag=false
         */
        if(edges[i1][i2]!= null||edges[i2][i1]!= null){
        	flag=false;
        }
        //System.out.println("i1: " + i1 + "  i2:" + i2); 
        //���Ȳ����ڱߣ���ӱ�
        if(flag){
        	if(i1>=0 && i1<vertexs.size() && i2 >=0 && i2<vertexs.size()) {  
                edges[i1][i2] = new MatrixEdge(v1, v2,null, weight); 
                edges[i2][i1]=new MatrixEdge(v1, v2,null, weight);
                edgeNum ++;  
            } else {  
                throw new ArrayIndexOutOfBoundsException("����Խ����Ӧ�ı߲��Ϸ���");  
            }  
        } //end if flag  
       //����ʲôҲ����
        else{
        	//System.out.println("�ظ�"+v1+v2);
        }
    }  
    public void addEdge(Object v1, Object v2, Object info, double weight) {  
        int i1 = vertexs.indexOf(v1);  
        int i2 = vertexs.indexOf(v2);  
        if(i1>=0 && i1<vertexs.size() && i2 >=0 && i2<vertexs.size()) {  
            edges[i1][i2] = new MatrixEdge( v1, v2, info, weight);  
            edges[i2][i1] = new MatrixEdge( v1, v2, info, weight);
            edgeNum ++;  
        } else {  
            throw new ArrayIndexOutOfBoundsException("����Խ����Ӧ�ı߲��Ϸ���");  
        }  
    }  
    //�����µĽڵ�֮�󣬴���ͼ�ķ���
    public void addVex(Object v) {  
        vertexs.add(v);  
        if(vertexs.size() > maxLen) {  
            expand();  
        }  
    }  
    private void expand() {  
        MatrixEdge newEdges[][] = new MatrixEdge[2*maxLen][2*maxLen];  
        for(int i=0; i<maxLen; i++) {  
            for(int j=0; j<maxLen; j++) {  
                newEdges[i][j] = (MatrixEdge) edges[i][j];  
            }  
        }  
        edges = newEdges;  
    }  
      
    public int getEdgeSize() {  
        return edgeNum;  
    }  
    public int getVertexSize() {  
        return vertexs.size();  
    }  
    public void removeEdge(Object v1, Object v2) {  
        int i1 = vertexs.indexOf(v1);  
        int i2 = vertexs.indexOf(v2);  
        if(i1>=0 && i1<vertexs.size() && i2 >=0 && i2<vertexs.size()) {  
            if(edges[i1][i2] == null) {  
                try {  
                    throw new Exception("�ñ߲����ڣ�");  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            } else {  
                edges[i1][i2] = null;  
                edges[i2][i1] = null;  
                edgeNum --;  
            }  
        } else {  
            throw new ArrayIndexOutOfBoundsException("����Խ����Ӧ�ı߲��Ϸ���");  
        }  
    }  
    public void removeVex(Object v) {  
        int index = vertexs.indexOf(v);  
        int n = vertexs.size();  
        vertexs.remove(index);  
        for(int i=0; i<n; i++){  
            edges[i][n-1] = null;  
            edges[n-1][i] = null;  
        }  
    }  
    public String printGraph() {  
        StringBuilder sb = new StringBuilder();  
        int n = getVertexSize();  
        for (int i = 0; i < n; i++) {  
            for(int j=0; j<n; j++) {  
            	if(edges[i][j]==null) 
            	edges[i][j]=new MatrixEdge(null,null ,null,0.0); 
                sb.append(edges[i][j]+" ");  
            }  
            sb.append("\n");  
        }  
         return sb.toString();  
      // return edges.toString();
    }  
    public void clear() {  
        maxLen = defaultSize;  
        vertexs.clear();  
        edges = null;  
    }  
    public String dfs(Object o) {
//        Visit visit[] = new Visit[vertexs.size()];  
//        for(int i=0; i<vertexs.size(); i++)  
//            visit[i] = Visit.unvisited;  
//        StringBuilder sb = new StringBuilder();  
//        dfs(o, visit, sb);  
//        return sb.toString();
    	  return null;  
    }  
//    private void dfs(Object o, Visit[] visit, StringBuilder sb) {  
////        int n = vertexs.indexOf(o);  
////        sb.append(o + "\t");  
////        visit[n] = Visit.visited;  
////          
////        Object v = getFirstVertex(o);  
////        while(null != v) {  
////            if(Visit.unvisited == visit[vertexs.indexOf(v)])  
////                dfs(v, visit, sb);  
////            v = getNextVertex(o, v);  
////        }  
//    }  
    public Object getFirstVertex(Object v) {  
        int i = vertexs.indexOf(v);  
        if(i<0)  
            throw new ArrayIndexOutOfBoundsException("����v�����ڣ�");  
        for(int col=0; col<vertexs.size(); col++)  
            if(edges[i][col] != null)  
                return vertexs.get(col);  
        return null;  
    }  
    public Object getNextVertex(Object v1, Object v2) {  
        int i1 = vertexs.indexOf(v1);  
        int i2 = vertexs.indexOf(v2);  
        if(i1<0 || i2<0)  
            throw new ArrayIndexOutOfBoundsException("����v�����ڣ�");  
        for(int col=i2+1; col<vertexs.size(); col++)  
            if(edges[i1][col] != null)  
                return vertexs.get(col);  
        return null;  
    }
	public String bfs(Object o) {
		// TODO Auto-generated method stub
		return null;
	}  
	/*
	 * �ڽ���ʵ��ͼʱ�����жϽڵ���Ƿ���ڱ���
	 */
	public boolean exist(Object v1, Object v2){
		int i1 = vertexs.indexOf(v1);  
        int i2 = vertexs.indexOf(v2); 
        if(edges[i1][i2]!= null||edges[i2][i1]!= null){
        	return true;
        }
		return false;
	}
}  
