package Alhelbawy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dynamic {
	
	//��̬ѡ���㷨
    public String dynamic(HashMap<String,Double>phash,HashMap<String,Double>phash1,String wikiurl){
      
    	String correct=null;
    	//�����ѡʵ�弯��Ϊ��
    	if(phash.size()==0){
			 correct="NIL";
		 }
		 
		 
		 // ���ֻ��һ����ѡʵ��
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
    	
    	//�����ѡʵ���������1
		 if(phash.size()>1){
			 List<String> keylist = new ArrayList<String>();  
			 List<String> keylist1 = new ArrayList<String>();  
			 
			 //�ȶ�phash��phash1��������,��ȡ�������б�
			 keylist=sort(phash);
		  	 keylist1=sort(phash1);
		  	 
		  	 //����phash��phash1��ǰ�������Ĳ�
		  	 double pdiff=phash.get(keylist.get(0))-phash.get(keylist.get(1));
			 double p1diff=phash1.get(keylist1.get(0))-phash1.get(keylist1.get(1));
			 
			 //��̬ѡ��Ŀ�����Ӷ���
			 if(pdiff>p1diff){
				 correct=wikiurl+keylist.get(0);
			 }
			 else{
				 correct=wikiurl+keylist1.get(0);
			 }
		 }
    	return correct;
    }
    
  //��hashmap��������
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
  				          //ͬ��������Ӧ��keyֵ  
  				          String kk=keyList.get(j);
  				          keyList.set(j, keyList.get(i));  
  				          keyList.set(i, kk);   
  				      }  
  			}
  			
      return keyList;
  	}
}
