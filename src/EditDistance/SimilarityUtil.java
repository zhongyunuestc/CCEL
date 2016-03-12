package EditDistance;
/** 
* 编辑距离的两字符串相似度 
*/ 
public class SimilarityUtil { 
    private  int min(int one, int two, int three) { 
        int min = one; 
        if(two < min) { 
            min = two; 
        } 
        if(three < min) { 
            min = three; 
        } 
        return min; 
    } 
    
    public  int ld(String str1, String str2) { 
        int d[][];    //矩阵  ,存储字符串的值
        int n = str1.length(); 
        int m = str2.length(); 
        int i;    //遍历str1的 
        int j;    //遍历str2的 
        char ch1;    //str1的 
        char ch2;    //str2的 
        int temp;    //记录相同字符,在某个矩阵位置值的增量,不是0就是1 
        if(n == 0) { 
            return m; 
        } 
        if(m == 0) { 
            return n; 
        } 
        d = new int[n+1][m+1]; 
        for(i=0; i<=n; i++) {    //初始化第一列 
            d[i][0] = i; 
        } 
        for(j=0; j<=m; j++) {    //初始化第一行 
            d[0][j] = j; 
        } 
        for(i=1; i<=n; i++) {    //遍历str1 
            ch1 = str1.charAt(i-1); 
            //去匹配str2 
            for(j=1; j<=m; j++) { 
                ch2 = str2.charAt(j-1); 
                if(ch1 == ch2) { 
                    temp = 0;    //代价，当前字符串相等，代价为0，当前字符串不相等，代价为1
                } else { 
                    temp = 1; 
                } 
                //左边+1，相当于插入；上边+1，相当于删去; 左上角+temp,相当于退换，三个值中取最小 
                d[i][j] = min(d[i-1][j]+1, d[i][j-1]+1, d[i-1][j-1]+temp); 
            } 
        } 
        return d[n][m]; 
    } 
    
    public double similary(String str1, String str2) { 
        int ld = ld(str1, str2); 
        return 1 - (double) ld / Math.max(str1.length(), str2.length()); 
    } 
    
    /*
     * 计算字符串的相似度
     */
    public static void main(String[] args) { 
    	SimilarityUtil sim=new SimilarityUtil();
        String str1 = "Michael Jordan"; 
        String str2 = "Michael Jordan (diambiguation)"; 
        System.out.println("编辑距离="+sim.ld(str1, str2)); 
        System.out.println("字符串的相似度="+sim.similary(str1, str2)); 
    } 
}