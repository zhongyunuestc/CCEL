
public class PreDeal {
	
	public String getMen(String node){
       String mention=node.substring(node.indexOf("<")+1, node.indexOf(","));
		return mention;
	}
	
	public String getCan(String node){
	  String  candidate=node.substring(node.indexOf(",")+1, node.indexOf(">"));
		return candidate;
	}
  public static void main(String[] args) {
	String str="<Li Na,Li Na (singer)>";
	PreDeal dl=new PreDeal();
	long startTime=System.currentTimeMillis();  //��ȡ��ʼʱ��
	System.out.println(dl.getMen(str));
	System.out.println(dl.getCan(str));
	long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
    double minute=(endTime-startTime)/1000.0;
    System.out.println("��������ʱ�䣺 "+minute+"s");
}
}
