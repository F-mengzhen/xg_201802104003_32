package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class SchoolDao {
  private static SchoolDao schoolDao = new SchoolDao();
  private static Collection<School> schools;
  /*static{
    schools = new TreeSet<School>();
  }*/
  public SchoolDao(){}

  public static SchoolDao getInstance(){
    return schoolDao;
  }

  public Collection<School> findAll()throws SQLException{
    schools = new TreeSet<School>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from school");

    while(resultSet.next()) {
      School school = new School(resultSet.getInt("id"),
        resultSet.getString("description"),
        resultSet.getString("no"),
        resultSet.getString("remarks"));
      schools.add(school);
    }
    JdbcHelper.close(stmt,connection);
    return SchoolDao.schools;


  }

  public School find(Integer id)throws SQLException{
    SchoolDao.getInstance().findAll();
    School desiredSchool = null;
    for (School school : schools) {
      if(id.equals(school.getId())){
        desiredSchool =  school;
        break;
      }
    }
    return desiredSchool;
  }

  public boolean update(School school)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String updateSchool_sql="UPDATE school SET description=?,no=?,remarks=? where id=?";
    PreparedStatement pstmt=connection.prepareStatement(updateSchool_sql);
    pstmt.setString(1,school.getDescription());
    pstmt.setString(2,school.getNo());
    pstmt.setString(3,school.getRemarks());
    pstmt.setInt(4,school.getId());
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("修改了"+ affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
  }

  public boolean add(School school)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String addSchool_sql="INSERT INTO school(description,no,remarks) VALUES(?,?,?)";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(addSchool_sql);
    pstmt.setString(1,school.getDescription());
    pstmt.setString(2,school.getNo());
    pstmt.setString(3,school.getRemarks());
    //执行预编译对象的executeUpdate方法，获取添加的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("添加了"+ affectedRowNum + "条记录");
    //关闭pstmt对象
    //关闭connection对象
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
  }

  public boolean delete(Integer id)throws SQLException{
    School school =this.find(id);
    return this.delete(school);
  }

  public boolean delete(School school)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String deleteSchool_sql="DELETE FROM school WHERE ID=?";
    PreparedStatement pstmt=connection.prepareStatement(deleteSchool_sql);
    pstmt.setInt(1,school.getId());
    pstmt.executeUpdate();
    //执行预编译对象的executeUpdate方法，获取删除的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("删除了" + affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return schools.remove(school);
  }

	public School addWithSP(School school)throws SQLException,ClassNotFoundException {
    //增加驱动程序
    Class.forName("com.mysql.jdbc.Driver");
    //url为数据库连接字串，其中jdbc为协议，mysql为子协议
    //local host：3306为数据库服务器的地址和端口
    String url="jdbc:mysql://localhost:3306/bysj"+"?useUnicode=true&characterEncoding=utf8"
      +"&serverTimezone=Asia/Shanghai";
    String username="root";
    String password="f18454005912";
    //获得连接对象
    Connection connection= DriverManager.getConnection(url,username,password);
    //根据连接对象准备可调用语句对象，sp_addSchool为存储过程，后面为4个参数
    CallableStatement callableStatement=connection.prepareCall("{CALL sp_addSchool (?,?,?,?)}");
    callableStatement.setString(1,school.getDescription());
    callableStatement.setString(2,school.getNo());
    callableStatement.setString(3,school.getRemarks());
    //将第四个参数设置为输出参数，类型为长整型（数据库的数据类型）
    callableStatement.registerOutParameter(4, Types.BIGINT);
    //执行可调用语句callableStatement
    callableStatement.execute();
    //获得第四个参数值，数据库为该记录自动生成的id
    int id=callableStatement.getInt(4);
    //为参数school的id字段赋值
    school.setId(id);
    callableStatement.close();
    connection.close();
    return school;

  }}
