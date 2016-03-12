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
    	  //localhost��ʾ�������ݿ�ķ�������ַ
    	  dbConfig.setHost("localhost");
    	  dbConfig.setDatabase("zhwiki");
    	  dbConfig.setUser("root");
    	  dbConfig.setPassword("133356");
    	  dbConfig.setLanguage(Language.chinese);
    	  //����wikipedia�������
    	  Wikipedia wiki=new Wikipedia(dbConfig);
    	  Page page=wiki.getPage(str);
//    	  System.out.println("��ҳ����"+page.getTitle());
//    	  System.out.println("��������"+page.getNumberOfInlinks());
//    	  System.out.println("�Ƿ�������ҳ"+page.isDisambiguation());
//    	  System.out.println("���ڶ��ٸ����"+page.getNumberOfCategories());
    	  //����ָ�����ҳ��
//    	  StringBuilder sb=new StringBuilder();
//    	  sb.append("����ָ���ҳ������ӣ�"+LF);
//    	  for(Page inLinkPage:page.getInlinks()){
//    		  sb.append(" "+inLinkPage.getTitle()+LF);  
//    	  }
//    	  sb.append(LF);
//         System.out.println(sb);
        /*
         * ָ�������˵�ҳ�棨�����ַ�����ȡʵ�������ҳ������ݣ�
         * �൱�ں�ѡ��ѡʵ�壨�������ʵ��ָ�������ֲ���Ϊ���Ǻ�ѡʵ�壩
         * û�к�׺��ǩ�ı���Ϊ���������ߵĺ�ѡ
         */
         StringBuilder sb1=new StringBuilder();
  	     sb1.append("ָ������ҳ������ӣ�"+LF);
  	     for(Page outLinkPage:page.getOutlinks()){
  		  sb1.append(" "+outLinkPage.getTitle()+LF);  
  	      }
  	    sb1.append(LF);
        System.out.println(sb1);
//        //�ض���ҳ��
//         StringBuilder sb2=new StringBuilder();
//  	     sb2.append("�ض���ҳ�棺"+LF);
//  	     for(String redirect:page.getRedirects()){
//  		  sb2.append(" "+new Title(redirect).getPlainTitle()+LF);  
//  	    }
//  	     sb2.append(LF);
//         System.out.println(sb2);
//       /*
//       * �õ�ҳ��Ĵ��ı���ʽ�����õ����ݵ�See alsoΪֹ,�������jsoup������
//       */
////         System.out.println("ҳ�����ݣ�");
////         System.out.println(page.getPlainText());
//      /*
//       * ��ȡĿ¼��Ϣ
//       */
//         System.out.println("Ŀ¼��Ϣ��");
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
    	  //localhost��ʾ�������ݿ�ķ�������ַ
    	  dbConfig.setHost("localhost");
    	  dbConfig.setDatabase("zhwiki");
    	  dbConfig.setUser("root");
    	  dbConfig.setPassword("133356");
    	  dbConfig.setLanguage(Language.chinese);
    	  //����wikipedia�������
    	  Wikipedia wiki=new Wikipedia(dbConfig);
    	  int i=0;
          Set<String> uniqueArticleTitles = new TreeSet<String>();
          for (Page title : wiki.getArticles()) {
              uniqueArticleTitles.add(title.getTitle().toString());
             
          }

          return uniqueArticleTitles;
      }
      /*
       * ʵ������ҳ��Ļ�ȡ����ʵ���������(disambiguation)
       * ��ѡ�Ļ�ȡ����������������������ʣ��ض���ҳ������
       * ��ָ��������ҳ����������ƶȽ�һ�����˵�һЩ����صĺ�ѡʵ��
       */
      public static void main(String[] args) throws WikiApiException {
		ConnectWiki con=new ConnectWiki();
		String str="�й�";
		con.getWiki(str);
	}
}
			