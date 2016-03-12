package wikiSearch;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Search {
   
	public String getContent(String search){
		// �������ʵ�URL  
		String wikiSearchURL=null;
		if(search.contains(" ")&&search.split(" ").length>1){
			search=search.replace(" ", "%20");
			 wikiSearchURL =  
				      "https://en.wikipedia.org/w/api.php?action=query&list=search&prop=info&format=json&srsearch="+search+"&titles="+search+"&generator=search&gsrsearch=meaning";  
		}
		else{
			 wikiSearchURL =  
				      "https://en.wikipedia.org/w/api.php?action=query&list=search&prop=info&format=json&srsearch="+search+"&titles="+search+"&generator=search&gsrsearch=meaning";  
		}
		
		  String content =""; 
		try {  
		      // ��ʼ��URL  
		      URL url = new URL(wikiSearchURL);  
		      // ����HttpURLConnection����������  
		      HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();  
		      // �жϻ�ȡ��Ӧ�����Ƿ�����  
		      if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {  
		                // �������ӳɹ�����ʾ  
		               System.out.println("ʵ����"+search+"ƴд���ܲ���ȷ,Did you mean ���о����� ");
		                // ����InputStreamReader  
		                InputStreamReader isr =  
		                // �����ַ�����Ϊutf-8  
		                        new InputStreamReader(httpconn.getInputStream(), "utf-8");  
		                int i;   
		                // ��ȡ��Ϣ��content��  
		                while ((i = isr.read()) != -1) {  
		                       content=content + (char) i;  
		                }  
		                isr.close();  
		      }  
		      else{
		    	  System.out.println("ʧ��");
		      }
		      // �Ͽ�����  
		    httpconn.disconnect();  
		} catch (Exception e) {  
		      // ��ʾ����ʧ��  
		      // Toast.LENGTH_SHORT������ʾ�϶̵�ʱ��  
		     System.out.println("����ʧ��");
		      e.printStackTrace();  
		} 
		String suggestion=" ";
		content=content.toString();
		if(content.contains("suggestion")&&content.contains("suggestionsnippet")){
			suggestion=content.substring(content.indexOf("suggestion")+13, content.indexOf("suggestionsnippet")-3);
		}
		suggestion=LowToCap(suggestion);
		return suggestion;
	}
	
	public String LowToCap(String name){
		String str="";
		if(name.contains(" ")&&name.split(" ").length>1){
			String a[]=name.split("\\s+");
		  for(int i=0;i<a.length;i++){
			  str+=Character.toUpperCase(a[i].charAt(0))+a[i].substring(1)+" ";		 
		  }
		}
		else{
			str=Character.toUpperCase(name.charAt(0))+name.substring(1);
		}
		 return str.trim();
	 }
	
	public static void main(String[] args) {
		long startTime=System.currentTimeMillis();
		Search wh=new Search();
		String search="Colorado";
		System.out.println("The page "+search+" does not exist.Did you mean:"+wh.getContent(search));
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
		double minute=(endTime-startTime)/1000.0;
		System.out.println("��������ʱ�䣺 "+minute+"s");
		
	}
}
