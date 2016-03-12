import com.spreada.utils.chinese.ZHConverter;
/*
 * 中文字体的繁体与简体互换
 */
public class Typetrans {
   
 /*
  * 简体转繁体
  */
   public static String SimToTra(String s){
	   ZHConverter converter=ZHConverter.getInstance(ZHConverter.TRADITIONAL);
	   String tran=converter.convert(s);
	   return tran;
   }
   
   /*
    * 繁体转简体
    */
   public static String TraToSim(String s){
	   ZHConverter converter=ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	   String simple=converter.convert(s);
	   return simple;
   }
   
   public static void main(String[] args) {
	  System.out.println("'海峽兩岸貨品貿易協議'的简体是："+TraToSim("海峡兩岸貨品貿易協議"));
	  System.out.println("'海峡两岸货品贸易协议'的繁体是："+SimToTra("海峡两岸货品贸易协议"));
}
 
}
