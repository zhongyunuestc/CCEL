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
     * ����wiki���ݿ⣬�ܷ���������ݿ��ʱ�䣿��ÿ������ֻ����һ�����ݿ�
     * �������ֿ��Ի�ȡ��ҳ�棬����ҳ���id��ͬ��Ҳ���Ի�ȡ��ҳ�棬�Լ�������
     */
	public  Wikipedia connect(){
      DatabaseConfiguration dbConfig=new DatabaseConfiguration();
   	  //localhost��ʾ�������ݿ�ķ�������ַ
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
	 * ��ȡ�ض���ҳ��
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
	 * ��ȡ����ҳ��(ʵ��ָ����ĺ�ѡʵ��)����˵�༭����Ƚ϶̵�(С��5��)
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
				//	System.out.println("������");
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
					//	System.out.println("������");
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
	 * ��ȡָ���ҳ��������Լ���������Ϊ�˱�֤Ψһ�ԣ��Ƿ���pageid����ȽϺ�
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
	 	  System.out.println("ָ���ҳ���������ĿΪ:"+page.getNumberOfInlinks());
	   }
 	    
		return list;
	}
	
	/*
	 * ��ȡָ���ҳ�������ҳ��ID
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
	 * ��ȡ��ҳ��ָ���ҳ���Լ���Ŀ
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
		   System.out.println("��ҳ��ĳ�������ĿΪ��"+page.getNumberOfInlinks());
	   }
	   
	     return list;
	}
	
	/*
	 * ��ȡ��ҳ����ָ��ĵ�����ҳ��ID
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
	  * ��ȡĿ¼��Ϣ
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
	  //   System.out.println("����������Ϊ��"+page.getNumberOfCategories());
		return list;
	}
	
    /*
     * ��wikiĿ¼ת��String��
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
	  //  System.out.println("����������Ϊ��"+page.getNumberOfCategories());
		return str.trim();
	 }
	
	
	/*
	 * ��ȡҳ������,��Ҫʱ���԰ѷ�����Ϣ�ӵ���������������ƶȱȽ�
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
	 * ��ȡҳ���id��
	 */
	public int getID(Wikipedia wiki,String name){
	//	Wikipedia wiki=connect();
		 Page page=null;
		 /*
		  * ��ҳ�治����ʱ����-1
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
	 * �Ƿ�������ҳ��
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
	 * �Ƿ����ض���ҳ��
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
	 * ����Ƿ���ڸ�wikiҳ��
	 */
	public boolean exist(Wikipedia wiki,String name){
		// Wikipedia wiki=connect();
		 return wiki.existsPage(name);
	}
	
	/*
	 * ��ȡҳ�������
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
	 * ��ȡҳ��������
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
		long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		String name="Switzerland";
//		System.out.println(op.isRedirect(wiki, name));
//		System.out.println(op.exist(wiki, name));
		System.out.println(op.getRedirect(wiki, name));
		long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
	    double minute=(endTime-startTime)/1000.0;
	    System.out.println("��������ʱ�䣺 "+minute+"s");
	}
}
