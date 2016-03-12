package WikiDataBase;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.tudarmstadt.ukp.wikipedia.api.*;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
public class ConnectWiki implements WikiConstants {
      public void getWiki(String str) throws WikiApiException{
    	  DatabaseConfiguration dbConfig=new DatabaseConfiguration();
    	  //localhost表示运行数据库的服务器地址
    	  dbConfig.setHost("localhost");
    	  dbConfig.setDatabase("zhwiki");
    	  dbConfig.setUser("root");
    	  dbConfig.setPassword("133356");
    	  dbConfig.setLanguage(Language.chinese);
    	  //创建wikipedia处理对象
    	  Wikipedia wiki=new Wikipedia(dbConfig);
    	  Page page=wiki.getPage(str);
//    	  System.out.println("网页名字"+page.getTitle());
//    	  System.out.println("入链接数"+page.getNumberOfInlinks());
//    	  System.out.println("是否是消歧页"+page.isDisambiguation());
//    	  System.out.println("属于多少个类别"+page.getNumberOfCategories());
    	  //所有指向其的页面
//    	  StringBuilder sb=new StringBuilder();
//    	  sb.append("所有指向该页面的链接："+LF);
//    	  for(Page inLinkPage:page.getInlinks()){
//    		  sb.append(" "+inLinkPage.getTitle()+LF);  
//    	  }
//    	  sb.append(LF);
//         System.out.println(sb);
        /*
         * 指向其他人的页面（用这种方法获取实体的消歧页面的内容）
         * 相当于候选获选实体（必须包含实体指称项名字才认为其是候选实体）
         * 没有后缀标签的被认为是流向度最高的候选
         */
         StringBuilder sb1=new StringBuilder();
  	     sb1.append("指向其他页面的链接："+LF);
  	     for(Page outLinkPage:page.getOutlinks()){
  		  sb1.append(" "+outLinkPage.getTitle()+LF);  
  	      }
  	    sb1.append(LF);
        System.out.println(sb1);
//        //重定向页面
//         StringBuilder sb2=new StringBuilder();
//  	     sb2.append("重定向页面："+LF);
//  	     for(String redirect:page.getRedirects()){
//  		  sb2.append(" "+new Title(redirect).getPlainTitle()+LF);  
//  	    }
//  	     sb2.append(LF);
//         System.out.println(sb2);
//       /*
//       * 得到页面的纯文本格式，有用的内容到See also为止,无需借助jsoup来处理
//       */
////         System.out.println("页面内容：");
////         System.out.println(page.getPlainText());
//      /*
//       * 获取目录信息
//       */
//         System.out.println("目录信息：");
//         StringBuilder sb3=new StringBuilder();
//         for (Category category : page.getCategories()) {
//             sb3.append("  " + category.getTitle() + LF);
//         }
//         sb3.append(LF);
//         System.out.println(sb3);
       	  
      }
      
      public static Set<String> getUniqueArticleTitles() throws WikiInitializationException,WikiApiException {
          // configure the database connection parameters
    	  DatabaseConfiguration dbConfig=new DatabaseConfiguration();
    	  //localhost表示运行数据库的服务器地址
    	  dbConfig.setHost("localhost");
    	  dbConfig.setDatabase("zhwiki");
    	  dbConfig.setUser("root");
    	  dbConfig.setPassword("133356");
    	  dbConfig.setLanguage(Language.chinese);
    	  //创建wikipedia处理对象
    	  Wikipedia wiki=new Wikipedia(dbConfig);
    	  int i=0;
          Set<String> uniqueArticleTitles = new TreeSet<String>();
          for (Page title : wiki.getArticles()) {
              uniqueArticleTitles.add(title.getTitle().toString());
             
          }

          return uniqueArticleTitles;
      }
      /*
       * 实体消歧页面的获取是在实体名后面加(disambiguation)
       * 候选的获取是名字里面必须包括这个单词，重定向页面用于
       * 共指处理。利用页面的余弦相似度进一步过滤掉一些不相关的候选实体
       */
      public static void main(String[] args) throws WikiApiException {
		ConnectWiki con=new ConnectWiki();
		String str="中国";
		con.getWiki(str);
	}
}
			