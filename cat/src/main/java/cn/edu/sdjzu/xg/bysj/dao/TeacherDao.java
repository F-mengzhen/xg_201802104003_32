package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.*;
import cn.edu.sdjzu.xg.bysj.service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
  private static TeacherDao teacherDao = new TeacherDao();

  private TeacherDao() {
  }

  public static TeacherDao getInstance() {
    return teacherDao;
  }

  private static Collection<Teacher> teachers;

  public Collection<Teacher> findAll() throws SQLException {
    teachers = new TreeSet<Teacher>();
    Connection connection = JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt = connection.createStatement();
    ResultSet resultSet = stmt.executeQuery("select * from teacher");

    while (resultSet.next()) {
      Teacher teacher = new Teacher(resultSet.getInt("id"),
        resultSet.getString("no"),
        resultSet.getString("name"),
        ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
        DegreeService.getInstance().find(resultSet.getInt("degree_id")),
        DepartmentService.getInstance().find(resultSet.getInt("department_id"))
      );
      teachers.add(teacher);
    }
    JdbcHelper.close(stmt, connection);
    return TeacherDao.teachers;

  }

  public Teacher find(Integer id) throws SQLException {
    TeacherDao.getInstance().findAll();
    Teacher desiredTeacher = null;
    for (Teacher teacher : teachers) {
      if (id.equals(teacher.getId())) {
        desiredTeacher = teacher;
        break;
      }
    }
    return desiredTeacher;
  }

  public boolean update(Teacher teacher) throws SQLException {
    Connection connection = JdbcHelper.getConn();
    String updateTeacher_sql = "UPDATE teacher SET no=?,name=?,profTitle_id=?,degree_id=?,department_id=? where id=?";
    PreparedStatement pstmt = connection.prepareStatement(updateTeacher_sql);
    pstmt.setString(1, teacher.getNo());
    pstmt.setString(2, teacher.getName());
    pstmt.setInt(3, teacher.getTitle().getId());
    pstmt.setInt(4, teacher.getDegree().getId());
    pstmt.setInt(5, teacher.getDepartment().getId());
    pstmt.setInt(6, teacher.getId());
    int affectedRowNum = pstmt.executeUpdate();
    System.out.println("修改了" + affectedRowNum + "条记录");
    JdbcHelper.close(pstmt, connection);
    return affectedRowNum > 0;
  }

  public boolean add(Teacher teacher) throws SQLException {
    Connection connection = null;
    PreparedStatement pstmt = null;
    int affectedRowNum = 0;
    try {
      connection = JdbcHelper.getConn();
      //设置自动提交为false,事务开始
      connection.setAutoCommit(false);
      String addTeacher_sql = "INSERT INTO teacher(no,name,profTitle_id,degree_id,department_id) VALUES(?,?,?,?,?)";
      //在该连接上创建预编译语句对象
      pstmt = connection.prepareStatement(addTeacher_sql);
      pstmt.setString(1, teacher.getNo());
      pstmt.setString(2, teacher.getName());
      pstmt.setInt(3, teacher.getTitle().getId());
      pstmt.setInt(4, teacher.getDegree().getId());
      pstmt.setInt(5, teacher.getDepartment().getId());
      //执行第一条sql语句
      pstmt.executeUpdate();
      //String teacher_id_sql= " select id from teacher order by id desc limit 1";
      String teacher_id_sql = "select max(id) from teacher";

      ResultSet resultSet = pstmt.executeQuery(teacher_id_sql);
      int id = 0;
      if (resultSet.next()) {
        id = resultSet.getInt("max(id)");
      }
      //teacher.setId(id);
      String addUaer_aql = "INSERT INTO user(username,password,teacher_id) VALUES(?,?,?)";
      pstmt = connection.prepareStatement(addUaer_aql);
      pstmt.setString(1, teacher.getNo());
      pstmt.setString(2, teacher.getNo());
      pstmt.setInt(3, id);
      //UserService.getInstance().addUser(new User(teacher.getNo(),teacher.getNo(),teacher));
      //执行第二条insert语句
      affectedRowNum = pstmt.executeUpdate();
      //提交
      connection.commit();
    } catch (SQLException e) {
      System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
      try {
        //回滚当前连接所做的操作
        if (connection != null) {
          connection.rollback();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      } finally {
        try {
          if (connection != null) {
            connection.setAutoCommit(true);
          }
        } catch (SQLException e2) {
          e2.printStackTrace();
        }

      }
    }
    System.out.println("添加了" + affectedRowNum + "条记录");
    //关闭连接
    JdbcHelper.close(pstmt, connection);
    return affectedRowNum > 0;

  }

  public boolean delete(Integer id) throws SQLException {
    Teacher teacher = this.find(id);
    return this.delete(teacher);
  }

  public boolean delete(Teacher teacher) throws SQLException {
    Connection connection = null;
    PreparedStatement pstmt = null;
    int affectedRowNum = 0;
    try {

      connection = JdbcHelper.getConn();
      connection.setAutoCommit(false);
      String deleteUser_sql = "DELETE FROM user WHERE teacher_id=?";
      pstmt = connection.prepareStatement(deleteUser_sql);
      pstmt.setInt(1, teacher.getId());
      pstmt.executeUpdate();
      String deleteTeacher_sql = "DELETE FROM teacher WHERE ID=?";
      pstmt = connection.prepareStatement(deleteTeacher_sql);
      pstmt.setInt(1, teacher.getId());
      affectedRowNum = pstmt.executeUpdate();
      //pstmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println(e.getMessage() + "\n errorCode=" + e.getErrorCode());
      try {
        //回滚当前连接所做的操作
        if (connection != null) {
          connection.rollback();
        }
      } catch (SQLException e1) {
        e1.printStackTrace();
      } finally {
        try {
          if (connection != null) {
            connection.setAutoCommit(true);
          }
        } catch (SQLException e2) {
          e2.printStackTrace();
        }
      }
    }
    System.out.println("删除了" + affectedRowNum + "条记录");
    //关闭连接
    JdbcHelper.close(pstmt, connection);
    return teachers.remove(teacher);
  }


  public static void main(String[] args) throws SQLException {
    TeacherDao.getInstance().findAll();
    ProfTitle profTitle=ProfTitleService.getInstance().find(1);
    Department department=DepartmentService.getInstance().find(9);
    Degree degree=DegreeService.getInstance().find(20);
    Teacher teacher1=new Teacher(1,"2","筱筱",profTitle,degree,department);
    TeacherDao.getInstance().add(teacher1);
    /*Teacher teacher2=TeacherDao.getInstance().find(2);
    System.out.println(teacher2);
    teacher2.setName("王明");
    System.out.println(teacher2);
    TeacherDao.getInstance().update(teacher2);
    System.out.println(teacher2);*/
    //Teacher teacher=TeacherService.getInstance().find(34);
    //System.out.println(teacher);
    //TeacherService.getInstance().delete(19);

  }

}
