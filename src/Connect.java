import java.sql.*;
public class Connect{
  public static void main(String[] args) throws SQLException{
	//声明Connection对象
      Connection con;
      //驱动程序名
      String driver = "com.mysql.jdbc.Driver";
      //URL指向要访问的数据库名mydata
      String url = "jdbc:mysql://localhost:3306/zhongyun";
      //MySQL配置时的用户名
      String user = "root";
      //MySQL配置时的密码
      String password = "133356";
      try {
          //加载驱动程序
          Class.forName(driver);
          //1.getConnection()方法，连接MySQL数据库！！
          con = DriverManager.getConnection(url,user,password);
          if(!con.isClosed())
          {   
        	 System.out.println("Succeeded connecting to the Database!");
             //statment类，用来链接
             Statement statement = con.createStatement();
            //要执行的SQL语句
             String sql = "select * from category";
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("ID" + "\t" + " Name");  
           while(rs.next()){
              //获取Name这列数据
             String name = rs.getString("name");
              //获取ID这列数据
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
