import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class test {
  public static void main(String[] args) {
	 Pattern p= Pattern.compile("[A-Z]+[A-Z ]*[A-Z]+");
	  String str="WORLD CUP";
	 Matcher m=p.matcher(str);
	 if(m.matches()){
		 
		 str=str.charAt(0)+str.substring(1).toLowerCase();
	 }
	 System.out.println(str);
}
}
