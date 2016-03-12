package Deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Normalize {
	//归一化函数
	public List<Double>normalize(List<String>entitylist,List<String>graphnode,List<Double> vlist){
		HashMap<String,Double>nlist=new HashMap<String,Double>();
		HashMap<String,Double>tlist=new HashMap<String,Double>();
		List<Double>rlist=new ArrayList<Double>();
		PreDeal pd=new PreDeal();
	    for(int i=0;i<vlist.size();i++){
       	   String nodename=graphnode.get(i);
   	       double scon=vlist.get(i);
   	       nlist.put(nodename, scon);
         }//end for
	 //   System.out.println(nlist);
	    
	    for(int i=0;i<entitylist.size();i++){
			 String mention=entitylist.get(i);
			 HashMap<String,Double>phash=new HashMap<String,Double>();
			 double sum=0.0;
			 for(String key:nlist.keySet()){
				 //获取实体指称项对应的候选实体的值
				 if(mention.equals(pd.getMen(key))){
					 phash.put(pd.getCan(key), nlist.get(key));
					 sum+=nlist.get(key);
				 };
			 }//emd for
			 
			 //归一计算
			 for(String key:phash.keySet()){
				 double nn=phash.get(key)/sum;
				 String node="<"+mention+","+key+">";
				 tlist.put(node,nn);
				 
			 }//end for
			 		 
	    } //end for
	 //   System.out.println(tlist);
	    
	    for(int j=0;j<graphnode.size();j++){
	    	
			 rlist.add(tlist.get(graphnode.get(j)));
		 }
		return rlist;
	}
}
