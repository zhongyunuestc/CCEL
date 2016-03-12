package EditDistance;
/** 
* �༭��������ַ������ƶ� 
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
        int d[][];    //����  ,�洢�ַ�����ֵ
        int n = str1.length(); 
        int m = str2.length(); 
        int i;    //����str1�� 
        int j;    //����str2�� 
        char ch1;    //str1�� 
        char ch2;    //str2�� 
        int temp;    //��¼��ͬ�ַ�,��ĳ������λ��ֵ������,����0����1 
        if(n == 0) { 
            return m; 
        } 
        if(m == 0) { 
            return n; 
        } 
        d = new int[n+1][m+1]; 
        for(i=0; i<=n; i++) {    //��ʼ����һ�� 
            d[i][0] = i; 
        } 
        for(j=0; j<=m; j++) {    //��ʼ����һ�� 
            d[0][j] = j; 
        } 
        for(i=1; i<=n; i++) {    //����str1 
            ch1 = str1.charAt(i-1); 
            //ȥƥ��str2 
            for(j=1; j<=m; j++) { 
                ch2 = str2.charAt(j-1); 
                if(ch1 == ch2) { 
                    temp = 0;    //���ۣ���ǰ�ַ�����ȣ�����Ϊ0����ǰ�ַ�������ȣ�����Ϊ1
                } else { 
                    temp = 1; 
                } 
                //���+1���൱�ڲ��룻�ϱ�+1���൱��ɾȥ; ���Ͻ�+temp,�൱���˻�������ֵ��ȡ��С 
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
     * �����ַ��������ƶ�
     */
    public static void main(String[] args) { 
    	SimilarityUtil sim=new SimilarityUtil();
        String str1 = "Michael Jordan"; 
        String str2 = "Michael Jordan (diambiguation)"; 
        System.out.println("�༭����="+sim.ld(str1, str2)); 
        System.out.println("�ַ��������ƶ�="+sim.similary(str1, str2)); 
    } 
}