import java.sql.*;
public class Connect{
  public static void main(String[] args) throws SQLException{
	//����Connection����
      Connection con;
      //����������
      String driver = "com.mysql.jdbc.Driver";
      //URLָ��Ҫ���ʵ����ݿ���mydata
      String url = "jdbc:mysql://localhost:3306/zhongyun";
      //MySQL����ʱ���û���
      String user = "root";
      //MySQL����ʱ������
      String password = "133356";
      try {
          //������������
          Class.forName(driver);
          //1.getConnection()����������MySQL���ݿ⣡��
          con = DriverManager.getConnection(url,user,password);
          if(!con.isClosed())
          {   
        	 System.out.println("Succeeded connecting to the Database!");
             //statment�࣬��������
             Statement statement = con.createStatement();
            //Ҫִ�е�SQL���
             String sql = "select * from category";
            //3.ResultSet�࣬������Ż�ȡ�Ľ��������
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("ID" + "\t" + " Name");  
           while(rs.next()){
              //��ȡName��������
             String name = rs.getString("name");
              //��ȡID��������
              String id = rs.getString("id");
              System.out.println(id + "\t" + name);
           }
          rs.close();
          con.close();}
         }catch (ClassNotFoundException e){
        	 System.out.println("Sorry,can`t find the Driver!");   
             e.printStackTrace(); 
         } 
}
}
