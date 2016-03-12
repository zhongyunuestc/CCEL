import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class paixu {
	/*
	 * 排序
	 */
	public static List<String> sort(HashMap<String,Double>hash){
		//HashMap<String,Double>subhash=new HashMap<String,Double>();
		List<String> keyList = new ArrayList<String>();  
		keyList.addAll(hash.keySet());  
		List<Double> valueList = new ArrayList<Double>();  
		valueList.addAll(hash.values());  
//		System.out.println( keyList);
//		  System.out.println(valueList);
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
	//	System.out.println( keyList);
//		System.out.println(valueList);
		
    return keyList;
}
	
	public static void main(String[] args) {
	  HashMap<String,Double>hash=new HashMap<String,Double>();
	  hash.put("Illinois River",0.51702882811414954);
	  hash.put("Illinois Territory",0.41702882811414954);
	  hash.put("Illinois Country",0.66702882811414954);
	  hash.put("Illinois Township, Pope County, Arkansas",0.3611575592573077);
	  hash.put("Illinois",0.7223151185146154);
	  hash.put("Illinois College",0.41702882811414954);
	  hash.put("USS Illinois (BB-65)",0.41702882811414954);
	  System.out.println(sort(hash));
		
	}
}
