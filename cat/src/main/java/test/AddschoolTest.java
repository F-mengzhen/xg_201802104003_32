//201802104003冯梦真
package test;
import util.JSONUtil;
import util.JdbcHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddschoolTest {
  public static void main(String[] args) {
    //将schoobiaol表中的no字段添加唯一约束性
    //ALTER TABLE school ADD UNIQUE(no);
    Connection connection=null;
    PreparedStatement preparedStatement=null;
    try{
      //获得当前数据库的连接对象
      connection= JdbcHelper.getConn();
      //设置自动提交为false,事务开始
      connection.setAutoCommit(false);
      preparedStatement=connection.prepareStatement("INSERT INTO school(description,no) VALUES(?,?)");
      preparedStatement.setString(1,"管理学院");
      preparedStatement.setString(2,"22");
      //执行第一条INSERT语句
      preparedStatement.executeUpdate();
      preparedStatement=connection.prepareStatement("INSERT INTO school(description,no) values(?,?)");
      preparedStatement.setString(1,"土木学院");
      preparedStatement.setString(2,"32");
      //执行第二条INSERT语句
      preparedStatement.executeUpdate();
      //事物以提交结束
      connection.commit();
    }
    catch (SQLException e){
      System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
      try{
        //回滚当前连接所做的操作
        if (connection!=null){
          connection.rollback();
        }
      }catch (SQLException e1){
        e1.printStackTrace();
      }finally {
        try{
          if (connection!=null){
            connection.setAutoCommit(true);

          }
        }catch (SQLException e2){
          e2.printStackTrace();
        }
        //关闭连接
        JdbcHelper.close(preparedStatement,connection);
      }
    }
  }
}
