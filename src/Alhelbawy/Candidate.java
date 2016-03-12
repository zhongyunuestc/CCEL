package Alhelbawy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import wikiSearch.Search;
import WikiDataBase.OperatorWiki;
import EditDistance.SimilarityUtil;
import de.tudarmstadt.ukp.wikipedia.api.Category;
import de.tudarmstadt.ukp.wikipedia.api.Page;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiTitleParsingException;

/*
 * 候选实体识别时至关重要的一步，有待改进，减少噪音数据的影响
 */
public class Candidate {
	
 public HashMap<String,List<String>>getAllCandidate(Wikipedia wiki,List<String>entitylist){
	 Page page=null;
	 OperatorWiki op=new OperatorWiki();
	 HashMap<String,List<String>>canhash=new HashMap<String,List<String>>();
	// SimilarityUtil sim=new SimilarityUtil();
	 for(int i=0;i<entitylist.size();i++){
		 List<String>canlist=new ArrayList<String>();
		 String name=entitylist.get(i);
		 String newname=name;
		 /*
		  * 可能存在拼写错误，使用wiki的did you mean纠正,该功能可以关闭
		  */
//		 if(!op.exist(wiki, name)){
//			 Search sh=new Search();
//			 name=sh.getContent(name);
//		 }
		//   System.out.println("name:"+name);
		 
		 /*
		  * 该页面直接是消歧页面
		  */
		 if(op.isDisambigute(wiki,name)){
			 try {
					page = wiki.getPage(name);
					
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
				//	System.out.println("不存在");
					e.printStackTrace();
				}
			 for(Page outLinkPage:page.getOutlinks()){
	 		     try {
	 		    
	 		     /*
	 		      * 长度小于4的可能是缩写，需要获取其消歧页面的全部实体
	 		      */
	 		      if(name.length()<4&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")
	 		    		  &&isAbbreviation(name,outLinkPage.getTitle().toString())&&!outLinkPage.getTitle().toString().contains("List of")){
	 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
	 		    	    canlist.add(cannode); 
	 		    	 }
	 		      
	 		     else if((outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")
	 		    		 &&!outLinkPage.getTitle().toString().contains("(genus)")&&!outLinkPage.getTitle().toString().contains("List of")
	 		    		 )||isContainPoint(name,outLinkPage.getTitle().toString())){
	 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
	 		    	    canlist.add(cannode); 
	 		    	 //  canlist.add(outLinkPage.getTitle().toString());
	 		    	 }
	 		    
//	 		         /*
//		 		      * 获取里面的list of name
//		 		      */
//		 		   if(outLinkPage.getTitle().toString().contains("List of")){
//		 		    	String listname=outLinkPage.getTitle().toString();
//		 				  Page page2=null;
//		 				try {
//		 			      page2=wiki.getPage(listname);
//		 				} catch (WikiApiException e) {
//		 					// TODO Auto-generated catch block
//		 					e.printStackTrace();
//		 				} //try -catch
//
//		 				  for(Page outLinkPage1:page2.getOutlinks()){
//		 		 		     try {
//		 		 		     if(outLinkPage1.getTitle().toString().contains(name)&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//		 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//		 		 		    	
//		 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//		 		 		    	 if(!canlist.contains(cannode)){
//		 		 		    		 canlist.add(cannode); 
//		 		 		    	 } //end if
//		 		 		       } //end if 
//		 		 		     } 
//		 		 		     catch (WikiTitleParsingException e) {
//		 						// TODO Auto-generated catch block
//		 						e.printStackTrace();
//		 					} //end try-catch
//		 				 }//end for
//		 		     }//end if list of
					
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 	    } //end if
			 
		 } 
		 
		 else{
			 String disname=name+" (disambiguation)";
			 boolean flag=op.exist(wiki,disname);
			 boolean flag1=op.exist(wiki,name);
			 if(flag&&flag1){
				  try {
						page = wiki.getPage(disname);
						
					} catch (WikiApiException e) {
						// TODO Auto-generated catch block
					//	System.out.println("不存在");
						e.printStackTrace();
					} //end try-catch
				 
				 for(Page outLinkPage:page.getOutlinks()){
		 		     try {
		 		    	 /*
			 		      * 长度小于4的可能是缩写，需要获取其消歧页面的全部实体
			 		      */
			 		   if(name.length()<4&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")
			 			&&isAbbreviation(name, outLinkPage.getTitle().toString())&&!outLinkPage.getTitle().toString().contains("List of")){
			 		     String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
			 		    	   canlist.add(cannode); 
			 		    	 }
			 		      
			 		   else if(((outLinkPage.getTitle().toString().contains(name))&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")
		 		    		&&!outLinkPage.getTitle().toString().contains("(genus)")&&!outLinkPage.getTitle().toString().contains("List of"))||isContainPoint(name,outLinkPage.getTitle().toString())){
		 		    	    
		 		    	//  canlist.add(outLinkPage.getTitle().toString());
		 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
		 		    	    canlist.add(cannode); 
		 		    	 }
			 		   
//			 		     /*
//			 		      * 获取里面的list of name
//			 		      */
//			 		     if(outLinkPage.getTitle().toString().contains("List of")){
//			 		    	String listname=outLinkPage.getTitle().toString();
//			 				  Page page2=null;
//			 				   try {
//			 					 page2=wiki.getPage(listname);
//			 				} catch (WikiApiException e) {
//			 					// TODO Auto-generated catch block
//			 					e.printStackTrace();
//			 				} //try -catch
//
//			 				  for(Page outLinkPage1:page2.getOutlinks()){
//			 		 		     try {
//			 		 		     if(outLinkPage1.getTitle().toString().contains(name)&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//			 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//			 		 		    	
//			 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//			 		 		    	 if(!canlist.contains(cannode)){
//			 		 		    		 canlist.add(cannode); 
//			 		 		    	 } //end if
//			 		 		       } //end if 
//			 		 		     } 
//			 		 		     catch (WikiTitleParsingException e) {
//			 						// TODO Auto-generated catch block
//			 						e.printStackTrace();
//			 					} //end try-catch
//			 				 }//end for
//			 		     }//end if list of
			 		     
					} catch (WikiTitleParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //end try-catch
		 	    } //end for
	           /*1
	            * 防止重定向页面没有出现
	            */
				 if(!canlist.contains("<"+name+","+name+">")&&!op.getCategorie(wiki, name).toString().contains("name disambiguation pages")){
					String cannode="<"+name+","+name+">";
	 		    	    canlist.add(cannode);  
				 }
			 }//end if
			 
		  if(!flag&&flag1){
			  List<String>list=op.getCategorie(wiki, name);
			  if(list.contains("Surnames")||list.toString().contains("name disambiguation pages")||list.toString().contains("given names") 
					  ||list.contains("Given names") ||list.contains("Names")){
				  Page page1=null; 
				  try {
						 page1=wiki.getPage(name);
					} catch (WikiApiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //try -catch
					   
				for(Page outLinkPage:page1.getOutlinks()){
				   try {
				 	  if(!outLinkPage.getTitle().toString().contains("Surname")&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")
				 			  &&!outLinkPage.getTitle().toString().contains("Given name")&&!outLinkPage.getTitle().toString().contains("link")&&
				 			  !outLinkPage.getTitle().toString().contains("List of")&&outLinkPage.getTitle().toString().contains(name)){
				 		    	
				 	   String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
				 		   if(!canlist.contains(cannode)){
				 		     canlist.add(cannode); 
				 		    } 
				 	  }
						
				 	     /*
			 		      * 获取里面的list of name
			 		      */
//			 		     if(outLinkPage.getTitle().toString().contains("List of")){
//			 		    	String listname=outLinkPage.getTitle().toString();
//			 				  Page page2=null;
//			 				   try {
//			 					 page2=wiki.getPage(listname);
//			 				} catch (WikiApiException e) {
//			 					// TODO Auto-generated catch block
//			 					e.printStackTrace();
//			 				} //try -catch
//
//			 				  for(Page outLinkPage1:page2.getOutlinks()){
//			 		 		     try {
//			 		 		     if(outLinkPage1.getTitle().toString().contains(name)&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//			 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//			 		 		    	
//			 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//			 		 		    	 if(!canlist.contains(cannode)){
//			 		 		    		 canlist.add(cannode); 
//			 		 		    	 } //end if
//			 		 		       } //end if 
//			 		 		     } 
//			 		 		     catch (WikiTitleParsingException e) {
//			 						// TODO Auto-generated catch block
//			 						e.printStackTrace();
//			 					} //end try-catch
//			 				 }//end for
//			 		     }//end if list of
			 		     
				     } catch (WikiTitleParsingException e) {
						// TODO Auto-generated catch block
			      	e.printStackTrace();
					   } //end try-catch
			    } //end for    
				  
			  }
			  else{
				  String cannode="<"+name+","+name+">";
		    	    canlist.add(cannode); 
			  }
		   }	   
		 }	 //end if else
		 
	  //如果包含name (surname),则把这个页面的链接也抓取下来  <Obama,Obama (surname)>
	 // System.out.println(canlist.contains("<"+name+","+name+" (surname)>"));
	  if(canlist.contains("<"+name+","+name+" (surname)>")){
		  String exname=name+" (surname)";
		  Page page1=null;
		   try {
			 page1=wiki.getPage(exname);
		} catch (WikiApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //try -catch
		   
		 for(Page outLinkPage:page1.getOutlinks()){
	 		     try {
	 		     if(outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")&&
	 		    		 !outLinkPage.getTitle().toString().contains("List of")||isContainPoint(name,outLinkPage.getTitle().toString())){
	 		    	    
	 		    	//  canlist.add(outLinkPage.getTitle().toString());
	 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
	 		    	 if(!canlist.contains(cannode)){
	 		    		 canlist.add(cannode); 
	 		    	 } 
	 		    	 }
	 		     
	 		     /*
	 		      * 获取里面的list of name,如此做之后会多出来好多候选实体，扩大了候选选择
	 		      */
//	 		     if(outLinkPage.getTitle().toString().contains("List of")){
//	 		    	String listname=outLinkPage.getTitle().toString();
//	 				  Page page2=null;
//	 				   try {
//	 					 page2=wiki.getPage(listname);
//	 				} catch (WikiApiException e) {
//	 					// TODO Auto-generated catch block
//	 					e.printStackTrace();
//	 				} //try -catch
//
//	 				  for(Page outLinkPage1:page2.getOutlinks()){
//	 		 		     try {
//	 		 		     if((outLinkPage1.getTitle().toString().contains(name)||Inconsistent(name,outLinkPage1.getTitle().toString()))&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//	 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//	 		 		    	
//	 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//	 		 		    	 if(!canlist.contains(cannode)){
//	 		 		    		 canlist.add(cannode); 
//	 		 		    	 } //end if
//	 		 		       } //end if 
//	 		 		     } 
//	 		 		     catch (WikiTitleParsingException e) {
//	 						// TODO Auto-generated catch block
//	 						e.printStackTrace();
//	 					} //end try-catch
//	 				 }//end for
//	 		     }//end if list of
					
				} catch (WikiTitleParsingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //end try-catch
	 	    } //end for  
		 canlist.remove("<"+name+","+name+" (surname)>");
	  }
	 
	  
	  /*
	    * 包含surname+title 特别情况
	    */
	    if(canlist.contains("<"+name+","+name+" (surname and title)>")){
	     String exname=name+" (surname and title)";
	     Page page1=null;
	      try {
	     page1=wiki.getPage(exname);
	   } catch (WikiApiException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   } //try -catch
	     
	    for(Page outLinkPage:page1.getOutlinks()){
	          try {
	          if(outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")&&
	            !outLinkPage.getTitle().toString().contains("List of")||isContainPoint(name,outLinkPage.getTitle().toString())){
	             
	          //  canlist.add(outLinkPage.getTitle().toString());
	           String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
	           if(!canlist.contains(cannode)){
	            canlist.add(cannode);
	           }
	           }
	         
	          /*
	           * 获取里面的list of name
	           */
//	        if(outLinkPage.getTitle().toString().contains("List of")){
//	           String listname=outLinkPage.getTitle().toString();
//	            Page page2=null;
//	          try {
//	           page2=wiki.getPage(listname);
//	         } catch (WikiApiException e) {
//	        // TODO Auto-generated catch block
//	          e.printStackTrace();
//	         } //try -catch
//
//	         for(Page outLinkPage1:page2.getOutlinks()){
//	             try {
//	             if((outLinkPage1.getTitle().toString().contains(name)||Inconsistent(name,outLinkPage1.getTitle().toString()))&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//	             ||isContainPoint(name,outLinkPage1.getTitle().toString())){
//	             
//	              String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//	              if(!canlist.contains(cannode)){
//	               canlist.add(cannode);
//	              } //end if
//	               } //end if
//	             }
//	             catch (WikiTitleParsingException e) {
//	         // TODO Auto-generated catch block
//	         e.printStackTrace();
//	          } //end try-catch
//	        }//end for
//	          }//end if list of
	     
	     } catch (WikiTitleParsingException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	     } //end try-catch
	        } //end for  
	    canlist.remove("<"+name+","+name+" (surname and title)>");
	    }
	   
	  
	  //如果包含name (given name),则把这个页面的链接也抓取下来  <Obama,Obama (surname)>
		  if(canlist.contains("<"+name+","+name+" (given name)>")){
			  String exname=name+" (given name)";
			  Page page1=null;
			   try {
				 page1=wiki.getPage(exname);
				  
			} catch (WikiApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //try -catch
			   
			 for(Page outLinkPage:page1.getOutlinks()){
		 		     try {
		 		     if(outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")&&
		 		    		 !outLinkPage.getTitle().toString().contains("List of")||isContainPoint(name,outLinkPage.getTitle().toString())){
		 		    	    
		 		    	//  canlist.add(outLinkPage.getTitle().toString());
		 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
		 		    	 if(!canlist.contains(cannode)){
		 		    		 canlist.add(cannode); 
		 		    	 } 
		 		    	 }
						
		 		     /*
		 		      * 获取里面的list of name
		 		      */
//		 		     if(outLinkPage.getTitle().toString().contains("List of")){
//		 		    	String listname=outLinkPage.getTitle().toString();
//		 				  Page page2=null;
//		 				   try {
//		 					 page2=wiki.getPage(listname);
//		 				} catch (WikiApiException e) {
//		 					// TODO Auto-generated catch block
//		 					e.printStackTrace();
//		 				} //try -catch
//
//		 				  for(Page outLinkPage1:page2.getOutlinks()){
//		 		 		     try {
//		 		 		     if((outLinkPage1.getTitle().toString().contains(name)||Inconsistent(name,outLinkPage1.getTitle().toString()))&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//		 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//		 		 		    	
//		 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//		 		 		    	 if(!canlist.contains(cannode)){
//		 		 		    		 canlist.add(cannode); 
//		 		 		    	 } //end if
//		 		 		       } //end if 
//		 		 		     } 
//		 		 		     catch (WikiTitleParsingException e) {
//		 						// TODO Auto-generated catch block
//		 						e.printStackTrace();
//		 					} //end try-catch
//		 				 }//end for
//		 		     }//end if list of
		 		     
					} catch (WikiTitleParsingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //end try-catch
		 	    } //end for  
			 canlist.remove("<"+name+","+name+" (given name)>");
		  }
	  
		//如果包含name (name),则把这个页面的链接也抓取下来  <Obama,Obama (surname)>
			 // System.out.println(canlist.contains("<"+name+","+name+" (surname)>"));
			  if(canlist.contains("<"+name+","+name+" (name)>")){
				  String exname=name+" (name)";
				  Page page1=null;
				   try {
					 page1=wiki.getPage(exname);
				} catch (WikiApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //try -catch
				   
				 for(Page outLinkPage:page1.getOutlinks()){
			 		     try {
			 		     if(outLinkPage.getTitle().toString().contains(name)&&!outLinkPage.getTitle().toString().contains(" (disambiguation)")&&
			 		    		 !outLinkPage.getTitle().toString().contains("List of")||isContainPoint(name,outLinkPage.getTitle().toString())){
			 		    	    
			 		    	//  canlist.add(outLinkPage.getTitle().toString());
			 		    	 String cannode="<"+name+","+outLinkPage.getTitle().toString()+">";
			 		    	 if(!canlist.contains(cannode)){
			 		    		 canlist.add(cannode); 
			 		    	 } 
			 		    	 }
							
//			 		    /*
//			 		      * 获取里面的list of name，降低了速度，增加了准确率
//			 		      */
//			 		     if(outLinkPage.getTitle().toString().contains("List of")){
//			 		    	String listname=outLinkPage.getTitle().toString();
//			 				  Page page2=null;
//			 				   try {
//			 					 page2=wiki.getPage(listname);
//			 				} catch (WikiApiException e) {
//			 					// TODO Auto-generated catch block
//			 					e.printStackTrace();
//			 				} //try -catch
//
//			 				  for(Page outLinkPage1:page2.getOutlinks()){
//			 		 		     try {
//			 		 		     if((outLinkPage1.getTitle().toString().contains(name)||Inconsistent(name,outLinkPage1.getTitle().toString()))&&!outLinkPage1.getTitle().toString().contains(" (disambiguation)")
//			 		 		    	||isContainPoint(name,outLinkPage1.getTitle().toString())){
//			 		 		    	
//			 		 		    	 String cannode="<"+name+","+outLinkPage1.getTitle().toString()+">";
//			 		 		    	 if(!canlist.contains(cannode)){
//			 		 		    		 canlist.add(cannode); 
//			 		 		    	 } //end if
//			 		 		       } //end if 
//			 		 		     } 
//			 		 		     catch (WikiTitleParsingException e) {
//			 						// TODO Auto-generated catch block
//			 						e.printStackTrace();
//			 					} //end try-catch
//			 				 }//end for
//			 		     }//end if list of
			 		     
						} catch (WikiTitleParsingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} //end try-catch
			 	    } //end for  
				 canlist.remove("<"+name+","+name+" (name)>");
			  }
	  
	  if(canlist.size()>0){
		  
		  canhash.put(name,canlist);
	  }
	  
	  else{
		  canhash.put(newname,canlist);
	  } 
	  
	 }
	 return  canhash;
 }
 
 /*
  * 为提高候选实体的质量，针对缩写短语，进一步判断其候选实体的范围
  */
 public boolean isAbbreviation(String src,String tar){
	boolean flag=false;
	String sx ="";
  if(tar.contains(" ")){
	  String[] sp = tar.split(" ");
		for (int k = 0; k < sp.length; k++) {
			if(sp[k].length()>0){
			    sx+= sp[k].charAt(0);
			}
	}//end for
	if(sx.contains(src)){
		flag=true;
	}
  }
  if(!tar.contains(" ")){
	  if(src.charAt(0)==tar.charAt(0)){
		  flag=true;
	  }
  }
	 return flag;
 }
 
 /*
  * 去掉候选实体中无关符号的影响
  */
 public boolean isContainPoint(String str1,String str2){
	 boolean flag=false;
	 if(str1.contains(".")&&!str2.contains(".")){
		 String re=str1.replace(".", "");
		 if(re.contains(str2)||str2.contains(re))
			 flag=true;
	 }
     if(str2.contains(".")&&!str1.contains(".")){
    	 String re=str2.replace(".", "");
		 if(re.contains(str1)||str1.contains(re))
			 flag=true;
	 }
	 return flag;
	 
 }
 
 /*
  * 处理候选形式与实体指称项的形式不一致的情况
  */
 public boolean Inconsistent(String str1,String str2){
	 boolean flag=false;
	 SimilarityUtil sim=new SimilarityUtil();
  if(!str1.contains(" ")&&str2.contains(" ")){
	  String a[]=str2.split(" ");//将str2按空格切分
	  for(int i=0;i<a.length;i++){
		  if(sim.ld(str1, a[i])<3){
			  flag=true;
			  break;
		  }
	  }//end for
  }//end if
  
  if(str1.contains(" ")&&str2.contains(" ")){
	  String b[]=str1.split(" ");//将str1按空格切分
	  String a[]=str2.split(" ");//将str2按空格切分
	  if((b.length==a.length)&&sim.ld(str1.replaceAll(" ",""),str2.replaceAll(" ",""))<3){
		  flag=true;
	  }//end if
  }//en if
	 return flag; 
 }
 
 public static void main(String[] args) {
	 long startTime=System.currentTimeMillis();  //获取开始时间
		OperatorWiki op=new OperatorWiki();
		Wikipedia wiki=op.connect();
		Candidate can=new Candidate();
	//	String name="Barack";
		List<String>list=Arrays.asList("Obama");
	//	System.out.println(can.getCandidate(wiki, name));
		System.out.println(can.getAllCandidate(wiki,list));
		long endTime=System.currentTimeMillis(); //获取结束时间
	    double minute=(endTime-startTime)/1000.0;
	    System.out.println("程序运行时间： "+minute+"s");
}
}
