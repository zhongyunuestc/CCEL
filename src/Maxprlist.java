import java.util.HashMap;
import java.util.List;

import Deal.PreDeal;


public class Maxprlist {
 
	public HashMap<String,Double>Max(List<String>entitylist,List<String>graphnode,List<Double> prlist) {
		HashMap<String,Double>nprlist=new HashMap<String,Double>();
		HashMap<String,Double>reprlist=new HashMap<String,Double>();
		PreDeal pd=new PreDeal();
	    for(int i=0;i<prlist.size();i++){
       	   String nodename=graphnode.get(i);
   	       double scon=prlist.get(i);
   	       nprlist.put(nodename, scon);
         }//end for
	    
	    for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 for(String key:nprlist.keySet()){
				 //��ȡʵ��ָ�����Ӧ�ĺ�ѡʵ��Ŀ��Ŷ�
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), nprlist.get(key));
				 }
			 }//emd for
			 
			 /*
			  * ���ֻ��һ����ѡʵ��
			  */
			  if(phash.size()==1){
				  for(String key1:phash.keySet()){
					  if(phash.get(key1)!=0.0){
						  reprlist.put(key1, phash.get(key1));
					  }//end if 
				  }//end for
			  }//end if
			  
			  /*
			   * ����ж����ѡʵ��
			   */
			  if(phash.size()>1){
				  String ne=null;
				   double max=0.0;
				  for(String key1:phash.keySet()){
					  if(phash.get(key1)>max){
						  max=phash.get(key1);
						  ne=key1;
					  }//end if
				  } //end for
				  reprlist.put(ne, max);
			  }//end if
		 }//end for
		return reprlist;
	}//end max
	
}
