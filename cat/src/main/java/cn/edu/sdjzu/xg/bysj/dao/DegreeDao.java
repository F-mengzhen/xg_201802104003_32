//201802104003冯梦真
package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;
import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;
public final class DegreeDao {
  private static DegreeDao degreeDao=
    new DegreeDao();
  private DegreeDao(){}
  public static DegreeDao getInstance(){
    return degreeDao;
  }
  private static Collection<Degree> degrees;

  public Collection<Degree> findAll()throws SQLException{
    degrees = new TreeSet<Degree>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from degree");

    while(resultSet.next()) {
      Degree degree = new Degree(resultSet.getInt("id"),
        resultSet.getString("description"),
        resultSet.getString("no"),
        resultSet.getString("remarks"));
      degrees.add(degree);
    }
    JdbcHelper.close(stmt,connection);
    return DegreeDao.degrees;

  }

  public Degree find(Integer id)throws SQLException{
    DegreeDao.getInstance().findAll();
    Degree desiredDegree = null;
    for (Degree degree : degrees) {
      if(id.equals(degree.getId())){
        desiredDegree =  degree;
        break;
      }
    }
    return desiredDegree;
  }

  public boolean update(Degree degree)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String updatedegree_sql="UPDATE degree SET no=?,description=?,remarks=?"+"where id=?";
    PreparedStatement pstmt=connection.prepareStatement(updatedegree_sql);
    pstmt.setString(1,degree.getNo());
    pstmt.setString(2,degree.getDescription());
    pstmt.setString(3,"");
    pstmt.setInt(4,degree.getId());
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("改变了"+affectedRowNum+"条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
  }

  public boolean add(Degree degree)throws SQLException {
    Connection connection= JdbcHelper.getConn();
    //创建SQL语句
    String addDegree_sql="INSERT INTO degree (no,description,remarks) VALUES" + "(?,?,?)";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(addDegree_sql);
    //为预编译参数赋值
    pstmt.setString(1,degree.getNo());
    pstmt.setString(2,degree.getDescription());
    pstmt.setString(3,degree.getRemarks());
    //执行预编译对象的executeUpdate方法，获取添加的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("添加了"+ affectedRowNum + "条记录");
    //关闭pstmt对象
    //关闭connection对象
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
  }

  public boolean delete(Integer id)throws SQLException{
    Degree degree = this.find(id);

    return this.delete(degree);

  }

  public boolean delete(Degree degree)throws SQLException{
    Connection connection= JdbcHelper.getConn();
    //创建SQL语句
    String deletedegree_sql="DELETE FROM degree WHERE id=?";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(deletedegree_sql);
    //为预编译参数赋值
    pstmt.setInt(1,degree.getId());
    //执行预编译对象的executeUpdate方法，获取删除的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("删除了" + affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    // return affectedRowNum>0;
    return degrees.remove(degree);
  }
  public static void main(String[] args)throws SQLException {
    DegreeDao.getInstance().findAll();
    Degree degree1=DegreeDao.getInstance().find(12);
    System.out.println(degree1);
    degree1.setDescription("士");
    //Degree degree=new Degree(12,"硕士","04","");
    DegreeDao.getInstance().update(degree1);
    Degree degree2=DegreeDao.getInstance().find(12);
    System.out.println(degree2);
  }

}

