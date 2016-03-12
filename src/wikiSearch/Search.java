package wikiSearch;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Search {
   
	public String getContent(String search){
		// 给出访问的URL  
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
		      // 初始化URL  
		      URL url = new URL(wikiSearchURL);  
		      // 创建HttpURLConnection，并打开连接  
		      HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();  
		      // 判断获取的应答码是否正常  
		      if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {  
		                // 给出连接成功的提示  
		               System.out.println("实体名"+search+"拼写可能不正确,Did you mean 进行纠正： ");
		                // 创建InputStreamReader  
		                InputStreamReader isr =  
		                // 设置字符编码为utf-8  
		                        new InputStreamReader(httpconn.getInputStream(), "utf-8");  
		                int i;   
		                // 读取消息到content中  
		                while ((i = isr.read()) != -1) {  
		                       content=content + (char) i;  
		                }  
		                isr.close();  
		      }  
		      else{
		    	  System.out.println("失败");
		      }
		      // 断开连接  
		    httpconn.disconnect();  
		} catch (Exception e) {  
		      // 提示连接失败  
		      // Toast.LENGTH_SHORT设置显示较短的时间  
		     System.out.println("连接失败");
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
		long endTime=System.currentTimeMillis(); //获取结束时间
		double minute=(endTime-startTime)/1000.0;
		System.out.println("程序运行时间： "+minute+"s");
		
	}
}
