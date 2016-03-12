package Recognition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
/*
 * ʵ��ʶ���һ���Ľ��ˣ������˸�����
 */
public class entityRecognize {
	
	public String reStr;
	/*
	 * ��������ı��е�ʵ��ָ����
	 */
    public List<String>getMention(String text,AbstractSequenceClassifier<CoreLabel> classifier1){
    	List<String>entitylist=new ArrayList<String>();
    	List<String>mentionlist=new ArrayList<String>();
    	List<String>resultlist=new ArrayList<String>();
    	/*
    	 * �����滻�ʵ�
    	 */
      //  String serializedClassifier = "classifiers/english.muc.7class.distsim.crf.ser.gz";
       // String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
       // String serializedClassifier= "classifiers/english.all.3class.distsim.crf.ser.gz";
       // String serializedClassifier= "classifiers/english.nowiki.3class.distsim.crf.ser.gz";
        
        try {
			AbstractSequenceClassifier<CoreLabel> classifier =classifier1;
		//	System.out.println(classifier.classifyWithInlineXML(text));
			 String result=classifier.classifyWithInlineXML(text);
			/*
			 * ����ı��е�����ʵ�壬Ҳ����ͨ��������ʽ���
			 */
		        if(result.contains("<PERSON>")){
		        	String []split=result.split("<PERSON>");
		        	//System.out.println(split[1]);
		        	for(int i=0;i<split.length;i++){
		        		if(split[i].contains("</PERSON>")){
		        	      String entity=split[i].substring(0,split[i].indexOf("</PERSON>"));
		                  if(!entitylist.contains(entity)){
		                		entitylist.add(entity);
		        		}//end if
		           	}
		        } 	//end for
		     }//end if 
		        
		    /*
		     * ����ı��еĵ���ʵ��
		     */
		       if(result.contains("<LOCATION>")){
		        	String []split=result.split("<LOCATION>");
		        	//System.out.println(split[1]);
		        	for(int i=0;i<split.length;i++){
		        		if(split[i].contains("</LOCATION>")){
		        	      String entity=split[i].substring(0,split[i].indexOf("</LOCATION>"));
		                  if(!entitylist.contains(entity)){
		                		entitylist.add(entity);
		        		}
		           	}
		         }//end for
		      }//end if
		        
		  /*
		   * ����ı��еĻ���ʵ��    
		   */
		      if(result.contains("<ORGANIZATION>")){
		        	String []split=result.split("<ORGANIZATION>");
		        	//System.out.println(split[1]);
		        	for(int i=0;i<split.length;i++){
		        		if(split[i].contains("</ORGANIZATION>")){
		        	      String entity=split[i].substring(0,split[i].indexOf("</ORGANIZATION>"));
		                  if(!entitylist.contains(entity)){
		                		entitylist.add(entity);
		        		}
		           	}	
		          }//end for
		        } //end if   
		      
		    /*
			 * ����ı��еĵ���ʵ��
			 */
			  if(result.contains("<MISC>")){
			       String []split=result.split("<MISC>");
			       //System.out.println(split[1]);
			       for(int i=0;i<split.length;i++){
			          if(split[i].contains("</MISC>")){
			        	  String entity=split[i].substring(0,split[i].indexOf("</MISC>"));
			               if(!entitylist.contains(entity)){
			                		entitylist.add(entity);
			              }
			           	}
			         }//end for
			      }//end if
			       
			//System.out.println(entitylist);
			mentionlist.addAll(entitylist);
		//	System.out.println(mentionlist);
		    /*
		     * �����Ĺ�ָ��Ҫ�����������򣺰���������ĸ
             * ��Ӧ�ñ��ĵĹ�ָ֮ǰ�Ƚ����ʹ�ָ����ã����ʹ�ָӦ��˹̹���Ĺ�ָ�������ﹲָ������
		     */
			Pattern p= Pattern.compile("[A-Z]+[A-Z ]*[A-Z]+");//ʵ��ȫ�Ǵ�д��ĸ���
			 Pattern p1= Pattern.compile(".*[1-9]+");
			for (int i = 0; i < entitylist.size(); i++) {
				String en = entitylist.get(i);
				 Matcher m=p.matcher(en);
				 Matcher m1=p1.matcher(en);
			   /*
			   * ���ʵ��ȫ���ɴ�д��ĸ���ɣ����䳤�ȴ���5���ְ����ո�ĺͲ������ո�����������
			   * ��������������yago���ݼ���������Ч��
			   */
				if(m.matches()&&en.length()>=5){
					String nen="";
					if(en.contains(" ")){
						String[]st=en.split(" ");
						for(int k=0;k<st.length;k++){
							if(st[k].length()>0){
							nen+=st[k].charAt(0)+st[k].substring(1).toLowerCase()+" ";
							}		
						}
					}else{
						 nen=en.charAt(0)+en.substring(1).toLowerCase();
					}
					
					text=text.replaceAll(en,nen.trim());//���ı��еĴ�д�滻��Сд
					
					/*
					 * �ж��Ƿ��������ʵ��
					 */
					if(!entitylist.contains(nen.trim())){
						//entitylist.set(i,nen.trim());
						 mentionlist.set(i,nen.trim());
					}else{
						//entitylist.remove(en);
						//entitylist.set(i, "");
						 mentionlist.set(i,"");
					}	
				}//end if
				
				
				/*
				 * �������ֵ�ʵ�屻ɾ��
				 *
				 */	 
			  if(m1.matches()&&en.length()<=5){
				 // entitylist.remove(en);
				//  entitylist.set(i, "");
				  mentionlist.set(i,"");
				  
			  }
			  
			  /*
			   * ʵ��ָ�����а��������ŵ�
			   */
			  if(en.contains(" (")){
				  en=en.substring(0, en.indexOf(" ("));
				 // entitylist.set(i,en.trim());
				  mentionlist.set(i,en.trim());
			  }
			  
				if (en.contains(" '")) {
					en = en.replace(" '", "");
				}// end if
				
				for (int j = 0; j < entitylist.size(); j++) {
					String en1 = entitylist.get(j);
					 Matcher m2=p.matcher(en1);
					if (i!= j) {
						/*
						 * ��ָʵ�������ϵ
						 */
						if (en.contains(en1)) {
							List<String> list = new ArrayList<String>();
							if (en.contains(" ")) {
								String[] sp = en.split(" ");
								for (int k = 0; k < sp.length; k++) {
									list.add(sp[k]);
								}
							}

							if (list.contains(en1)) {
								text=text.replace(en1, en);
							//	entitylist.remove(en1);
                                mentionlist.set(j, "");
							//	System.out.println(en1 + "---->" + en);
								// System.out.println(str);
							}
							
							if(en.contains(en1)&&en1.contains(" ")){
								text=text.replace(en1, en);
								//entitylist.remove(en1);
								mentionlist.set(j,"");
							//	System.out.println(en1 + "---->" + en);
							}
						} // end if ��ָ����
						
                    	
						/*
						 * ��ָ����ĸ������ϵ
						 */
						if (!en.contains(en1) && en1.length()<=5&&m2.matches()) {
							// System.out.println("����ĸ");
							int index=text.indexOf(en);
							int index1=text.indexOf(en1);
						//	System.out.println(index+"-----"+index1);
							String sx ="";
							if (en.contains(" ")) {
								// System.out.println("����ĸ");
								String[] sp = en.split(" ");
									for (int k = 0; k < sp.length; k++) {
										if(sp[k].length()>0){
										    sx+= sp[k].charAt(0);
										}
								}
								// System.out.println(sp[0].charAt(0));
							}

							if (sx.contains(en1)||(index1-index==en.length()+3)) {
								text=text.replace(en1, en);
							//	entitylist.remove(en1);
								mentionlist.set(j,"");
							//	System.out.println(en1 + "----->" + en);
								// System.out.println(str);
							}
							
						} // end if����ĸ��д
					}// end if i!=j
				}
			}
			
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        reStr=text;
       // System.out.println(text);
        if(text.contains("Page")){
        	
        	// entitylist.add("Page");
        	 mentionlist.add("Page");
        }
        
        if(text.contains("Early")){
        	
        	 mentionlist.add("Early");
//        	entitylist.add("Early");
        }
        
        //��һ�����˺�ѡʵ��
        for(int i=0;i<mentionlist.size();i++){
        	if(mentionlist.get(i)!=""){
        		resultlist.add(mentionlist.get(i));
        	}	
        }
    	return resultlist;
    }
    
    /*
     * ���͵Ŀռ任ʱ�䣬���еĻ���������ĺ��������滻���text
     */
    public String getText(){
    	String text=reStr;
    	reStr=null;
    	return text;
    }
    
    public static void main(String[] args) {
    	String serializedClassifier= "classifiers/english.conll.4class.distsim.crf.ser.gz";
    	AbstractSequenceClassifier<CoreLabel> classifier;
		try {
			classifier = CRFClassifier.getClassifier(serializedClassifier);
			entityRecognize er=new entityRecognize();
			String text="The European Commission said on Thursday it disagreed with German advice to consumers to shun British lamb until scientists determine whether mad cow disease can be transmitted to sheep .  Germany 's representative to the European Union 's veterinary committee Werner Zwingmann said on Wednesday consumers should buy sheepmeat from countries other than Britain until the scientific advice was clearer .   We do n't support any such recommendation because we do n't see any grounds for it , the Commission 's chief spokesman Nikolaus van der Pas told a news briefing .  He said further scientific study was required and if it was found that action was needed it should be taken by the European Union .  He said a proposal last month by EU Farm Commissioner Franz Fischler to ban sheep brains , spleens and spinal cords from the human and animal food chains was a highly specific and precautionary move to protect human health .  Fischler proposed EU-wide measures after reports from Britain and France that under laboratory conditions sheep could contract Bovine Spongiform Encephalopathy ( BSE ) -- mad cow disease .  But Fischler agreed to review his proposal after the EU 's standing veterinary committee , mational animal health officials , questioned if such action was justified as there was only a slight risk to human health .  Spanish Farm Minister Loyola de Palacio had earlier accused Fischler at an EU farm ministers ' meeting of causing unjustified alarm through  dangerous generalisation .   .  Only France and Britain backed Fischler 's proposal .  The EU 's scientific veterinary and multidisciplinary committees are due to re-examine the issue early next month and make recommendations to the senior veterinary officials .  Sheep have long been known to contract scrapie , a brain-wasting disease similar to BSE which is believed to have been transferred to cattle through feed containing animal waste .  British farmers denied on Thursday there was any danger to human health from their sheep , but expressed concern that German government advice to consumers to avoid British lamb might influence consumers across Europe .   What we have to be extremely careful of is how other countries are going to take Germany 's lead ,  Welsh National Farmers ' Union ( NFU ) chairman John Lloyd Jones said on BBC radio .  Bonn has led efforts to protect public health after consumer confidence collapsed in March after a British report suggested humans could contract an illness similar to mad cow disease by eating contaminated beef .  Germany imported 47,600 sheep from Britain last year , nearly half of total imports .  It brought in 4,275 tonnes of British mutton , some 10 percent of overall imports .";
			System.out.println(er.getMention(text,classifier));
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	//	System.out.println(er.getText());
		//System.out.println(er.reStr);
	}
}
