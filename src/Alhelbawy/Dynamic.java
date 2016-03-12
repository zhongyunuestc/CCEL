package Alhelbawy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dynamic {
	
	//动态选择算法
    public String dynamic(HashMap<String,Double>phash,HashMap<String,Double>phash1,String wikiurl){
      
    	String correct=null;
    	//如果候选实体集合为空
    	if(phash.size()==0){
			 correct="NIL";
		 }
		 
		 
		 // 如果只有一个候选实体
		  if(phash.size()==1){
			  for(String key1:phash.keySet()){
				  if(phash.get(key1)!=0.0){
					  correct=wikiurl+key1;
				  }//end if 
				  else if(phash.get(key1)==0.0){
					  correct="NIL";
				  }
			  }//end for
		  }//end if
    	
    	//如果候选实体个数大于1
		 if(phash.size()>1){
			 List<String> keylist = new ArrayList<String>();  
			 List<String> keylist1 = new ArrayList<String>();  
			 
			 //先对phash和phash1进行排序,获取排序后的列表
			 keylist=sort(phash);
		  	 keylist1=sort(phash1);
		  	 
		  	 //计算phash和phash1中前两个数的差
		  	 double pdiff=phash.get(keylist.get(0))-phash.get(keylist.get(1));
			 double p1diff=phash1.get(keylist1.get(0))-phash1.get(keylist1.get(1));
			 
			 //动态选择目标链接对象
			 if(pdiff>p1diff){
				 correct=wikiurl+keylist.get(0);
			 }
			 else{
				 correct=wikiurl+keylist1.get(0);
			 }
		 }
    	return correct;
    }
    
  //对hashmap进行排序
  	public List<String> sort(HashMap<String,Double>hash){
  		
  		List<String> keyList = new ArrayList<String>();  
  		keyList.addAll(hash.keySet());  
  		List<Double> valueList = new ArrayList<Double>();  
  		valueList.addAll(hash.values());  
  			for(int i=0; i<valueList.size(); i++)  
  				   for(int j=i+1; j<valueList.size(); j++) {  
  				      if(valueList.get(j)>valueList.get(i)) {  
  				    	  Double tmp=valueList.get(j);
  				          valueList.set(j, valueList.get(i));  
  				          valueList.set(i, tmp);  
  				          //同样调整对应的key值  
  				          String kk=keyList.get(j);
  				          keyList.set(j, keyList.get(i));  
  				          keyList.set(i, kk);   
  				      }  
  			}
  			
      return keyList;
  	}
}
