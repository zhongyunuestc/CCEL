package WikiDataBase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import EditDistance.SimilarityUtil;
import de.tudarmstadt.ukp.wikipedia.api.*;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiInitializationException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;
public class OperatorWiki {
    /*
     * 连接wiki数据库，能否减少连数据库的时间？？每个操作只连接一次数据库
     * 给定名字可以获取其页面，给定页面的id号同样也可以获取其页面，以及其名字
     */
	public  Wikipedia connect(){
      DatabaseConfiguration dbConfig=new DatabaseConfiguration();
   	  //localhost表示运行数据库的服务器地址
   	  dbConfig.setHost("localhost");
   	  dbConfig.setDatabase("zhongyun");
   	  dbConfig.setUser("root");
   	  dbConfig.setPassword("133356");
   	  dbConfig.setLanguage(Language.english);
   	  Wikipedia wiki=null;
   	  try {
		 wiki=new Wikipedia(dbConfig);
	} catch (WikiInitializationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return  wiki;
	}
	
	/*
	 * 获取重定向页面
	 * 
	 */
	public List<String>getRedirect(Wikipedia wiki,String name){
		//Wikipedia wiki=connect();
		 Page page=null;
		 List<String>list=new ArrayList<String>();
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	     for(String redirect:page.getRedirects()){
	 		     list.add(redirect);
	 	    }
		}
		
		return list;
	}
	
	/*
	 * 获取消歧页面(实体指称项的候选实体)或者说编辑距离比较短的(小于5的)
	 * Li Na (disambiguation)
	 */
	public List<String>getDisambigute(Wikipedia wiki,String name) {
		// Wikipedia wiki=connect();
		 Page page=null;
		 List<String>list=new ArrayList<String>();
		 SimilarityUtil sim=new SimilarityUtil();
		 if(isDisambigute(wiki,name)){
			 try {
					page = wiki.getPage(name);
					
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
				//	System.out.println("不存在");
					e.printStackTrace();
				}
			 for(Page outLinkPage:page.getOutlinks()){
	 		     try {
	 		     if(!outLinkPage.getTitle().toString().contains(" (disambiguation)")){
	 		        
	 		    	 list.add(outLinkPage.getTitle().toString()); 
	 		    	 
	 		    	 }
					
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    } //end if
			 
		 } 
		 else{
			 String disname=name+" (disambiguation)";
			 boolean flag=exist(wiki,disname);
			 boolean flag1=exist(wiki,name);
			 if(flag&&flag1){
				  try {
						page = wiki.getPage(disname);
						
					} catch (WikiApiException e) {
						// TODO Auto-generated catch block
					//	System.out.println("不存在");
						e.printStackTrace();
					}
				 
				 for(Page outLinkPage:page.getOutlinks()){
		 		     try {
		 		     if((outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)"))
		 		    		|| sim.ld(outLinkPage.getTitle().toString(), name)<5){
		 		        
		 		    	    list.add(outLinkPage.getTitle().toString()); 
		 		    	 
		 		    	 }
						
					} catch (WikiTitleParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		 	    } 
				 
			 }
			 
		  if(!flag&&flag1){
				 list.add(name);
			 }	   
		 
		 if(!flag1){
			  list.add("NIL");
		  }
			 
		 }	 
		return list;
	}
	
	/*
	 * 获取指向该页面的链接以及链接数，为了保证唯一性，是否用pageid代替比较好
	 */
	public List<String>getInlink(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 Page page=null;
		 List<String>list=new ArrayList<String>();
	   if(exist(wiki,name)){
		   try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	     for(Page inLinkPage:page.getInlinks()){
	 		     try {
					list.add(inLinkPage.getTitle().toString());
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    }  
	 	  System.out.println("指向该页面的链接数目为:"+page.getNumberOfInlinks());
	   }
 	    
		return list;
	}
	
	/*
	 * 获取指向该页面的其他页面ID
	 */
	public List<Integer>getInlinkID(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 Page page=null;
		 List<Integer>list=new ArrayList<Integer>();
		if(exist(wiki,name)){
		  try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 	     for(Page inLinkPage:page.getInlinks()){
	 		    list.add(inLinkPage.getPageId());
	 	    }
		}	
		else{
			list.add(-1);
		}
 	     return list;
	}
	
	/*
	 * 获取该页面指向的页面以及数目
	 */
	public List<String>getOutlink(Wikipedia wiki,String name){
		//Wikipedia wiki=connect();
		Page page=null;
	   List<String>list=new ArrayList<String>();
	   if(exist(wiki,name)){
		   try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		     for(Page outLinkPage:page.getOutlinks()){
			     try {
					list.add(outLinkPage.getTitle().toString());
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } 
		   System.out.println("该页面的出链接数目为："+page.getNumberOfInlinks());
	   }
	   
	     return list;
	}
	
	/*
	 * 获取该页面所指向的的其他页面ID
	 */
	public List<Integer>getOutlinkID(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 Page page=null;
		 List<Integer>list=new ArrayList<Integer>();
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 	     for(Page outLinkPage:page.getOutlinks()){
	 		    list.add(outLinkPage.getPageId());
	 	    }
		}
		
		else{
			list.add(-1);
		}
 	     return list;
	}
	
	 /*
	  * 获取目录信息
	  */
	public List<String>getCategorie(Wikipedia wiki,String name){
	//	 Wikipedia wiki=connect();
		 Page page=null;
		 List<String>list=new ArrayList<String>();
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 for (Category category : page.getCategories()) {
	             try {
					list.add(category.getTitle().toString());
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
		}
	  //   System.out.println("所属类别个数为："+page.getNumberOfCategories());
		return list;
	}
	
    /*
     * 将wiki目录转成String型
     */
	 public String getCatToString(Wikipedia wiki,String name){
		 Page page=null;
		 String str="";
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 for (Category category : page.getCategories()) {
	             try {
					str=str+category.getTitle().toString()+" ";
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
		}
	  //  System.out.println("所属类别个数为："+page.getNumberOfCategories());
		return str.trim();
	 }
	
	
	/*
	 * 获取页面内容,必要时可以把分类信息加到内容里面进行相似度比较
	 */
	public String getPageText(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 Page page=null;
		 String text=null;
		 if(exist(wiki,name)){
			 try {
					page = wiki.getPage(name);
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					 text=page.getPlainText();
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 }
		return text;
	}
	/*
	 * 获取页面的id号
	 */
	public int getID(Wikipedia wiki,String name){
	//	Wikipedia wiki=connect();
		 Page page=null;
		 /*
		  * 该页面不存在时返回-1
		  */
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return page.getPageId();
		}
		return -1;
	}
	
	/*
	 * 是否是消歧页面
	 */
	public boolean isDisambigute(Wikipedia wiki,String name){
	//	 Wikipedia wiki=connect();
		 Page page=null;
		 boolean flag=false;
		if(exist(wiki,name)){
			try {
				page = wiki.getPage(name);
				flag=page.isDisambiguation();
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return flag;
	}
	
	/*
	 * 是否是重定向页面
	 */
	public boolean isRedirect(Wikipedia wiki,String name){
		//	 Wikipedia wiki=connect();
			 Page page=null;
			 boolean flag=false;
			if(exist(wiki,name)){
				try {
					page = wiki.getPage(name);
					flag=page.isRedirect();
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			return flag;
		}
	
	/*
	 * 检查是否存在该wiki页面
	 */
	public boolean exist(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 return wiki.existsPage(name);
	}
	
	/*
	 * 获取页面出链数
	 */
	public int  getOutnumber(Wikipedia wiki,String name){
		      Page page=null;
		      int number=0;
		   if(exist(wiki,name)){
			   try {
					page = wiki.getPage(name);
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
		 	 number=page.getNumberOfOutlinks();
		   }
		   return number;
	}
	
	/*
	 * 获取页面入链数
	 */
	public int  getInnumber(Wikipedia wiki,String name){
	      Page page=null;
	      int number=0;
	   if(exist(wiki,name)){
		   try {
				page = wiki.getPage(name);
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	 	 number=page.getNumberOfInlinks();
	   }
	   return number;
}

	
	public static void main(String[] args) {
		long startTime=System.currentTimeMillis();  //获取开始时间
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		String name="Switzerland";
//		System.out.println(op.isRedirect(wiki, name));
//		System.out.println(op.exist(wiki, name));
		System.out.println(op.getRedirect(wiki, name));
		long endTime=System.currentTimeMillis(); //获取结束时间
	    double minute=(endTime-startTime)/1000.0;
	    System.out.println("程序运行时间： "+minute+"s");
	}
}
