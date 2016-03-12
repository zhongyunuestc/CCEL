import com.spreada.utils.chinese.ZHConverter;
/*
 * ��������ķ�������廥��
 */
public class Typetrans {
   
 /*
  * ����ת����
  */
   public static String SimToTra(String s){
	   ZHConverter converter=ZHConverter.getInstance(ZHConverter.TRADITIONAL);
	   String tran=converter.convert(s);
	   return tran;
   }
   
   /*
    * ����ת����
    */
   public static String TraToSim(String s){
	   ZHConverter converter=ZHConverter.getInstance(ZHConverter.SIMPLIFIED);
	   String simple=converter.convert(s);
	   return simple;
   }
   
   public static void main(String[] args) {
	  System.out.println("'���{�ɰ�؛Ʒ�Q�ׅf�h'�ļ����ǣ�"+TraToSim("��Ͽ�ɰ�؛Ʒ�Q�ׅf�h"));
	  System.out.println("'��Ͽ������Ʒó��Э��'�ķ����ǣ�"+SimToTra("��Ͽ������Ʒó��Э��"));
}
 
}
