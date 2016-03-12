import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class ttt {
 public static void main(String[] args) {
	 HashMap<String,Double>hash=new HashMap<String,Double>();
	    ValueComparator bvc = new ValueComparator(hash);
	    TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);

		  hash.put("Illinois River",0.41702882811414954);
		  hash.put("Illinois Territory",0.41702882811414954);
		  hash.put("Illinois Country",0.36702882811414954);
		  hash.put("Illinois Township, Pope County, Arkansas",0.3611575592573077);
		  hash.put("Illinois",0.7223151185146154);
		  hash.put("Illinois College",0.41702882811414954);
		  hash.put("USS Illinois (BB-65)",0.41702882811414954);
	    System.out.println("unsorted map: " + hash);
	    sorted_map.putAll(hash);
	    System.out.println("results: " + sorted_map);
}
}

class ValueComparator implements Comparator {

Map<String, Double> base;

public ValueComparator(Map<String, Double> base) {
    this.base = base;
}

// Note: this comparator imposes orderings that are inconsistent with equals.
public int compare(Object a, Object b) {
	// TODO Auto-generated method stub
    if (base.get(a) >= base.get(b)) {
        return -1;
    } else {
        return 1;
    } // returning 0 would merge keys
}
}
