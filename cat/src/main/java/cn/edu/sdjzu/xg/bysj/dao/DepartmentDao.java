package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {
  private static Collection<Department> departments;

  private static DepartmentDao departmentDao=new DepartmentDao();
  private DepartmentDao(){}

  public static DepartmentDao getInstance(){
    return departmentDao;
  }

  public Collection<Department> findAll()throws SQLException {
    departments = new TreeSet<Department>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from department");
    while(resultSet.next()) {
      Department department = new Department(resultSet.getInt("id"),
        resultSet.getString("description"),
        resultSet.getString("no"),
        resultSet.getString("remarks"),
        SchoolService.getInstance().find(resultSet.getInt("school_id"))
      );

      departments.add(department);
    }
    JdbcHelper.close(stmt,connection);
    return DepartmentDao.departments;
  }
  //根据school_id查找该学院下的所有系
  public Collection<Department> findAllBySchool(Integer schoolId)throws SQLException{
    //获得所有系
    DepartmentService.getInstance().findAll();
    //创建一个Department类型Collection对象；
    Collection<Department> desiredDepartment=new TreeSet<Department>();
    //for循环找出所有school_id与参数schoolId相等的系
    for (Department department: departments){
      if (schoolId.equals(department.getSchool().getId())){
        System.out.println(department);
        desiredDepartment.add(department);
      }
    }
    return desiredDepartment;
  }

  public Department find(Integer id)throws SQLException{
    DepartmentDao.getInstance().findAll();
    Department desiredDepartment = null;
    for (Department department : departments) {
      if(id.equals(department.getId())){
        desiredDepartment =  department;
        break;
      }
    }
    return desiredDepartment;
  }

  public boolean update(Department department)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String updateDepartment_sql="UPDATE department SET description=?,no=?,remarks=?,school_id=? where id=?";
    PreparedStatement pstmt=connection.prepareStatement(updateDepartment_sql);
    pstmt.setString(1,department.getDescription());
    pstmt.setString(2,department.getNo());
    pstmt.setString(3,department.getRemarks());
    pstmt.setInt(4,department.getSchool().getId());
    pstmt.setInt(5,department.getId());
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("修改了"+ affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;

  }

  public boolean add(Department department)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String addDepartment_sql="INSERT INTO department (description,no,remarks,school_id) VALUES(?,?,?,?)";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(addDepartment_sql);
    pstmt.setString(1,department.getDescription());
    pstmt.setString(2,department.getNo());
    pstmt.setString(3,department.getRemarks());
    pstmt.setInt(4,department.getSchool().getId());
    //执行预编译对象的executeUpdate方法，获取添加的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("添加了"+ affectedRowNum + "条记录");
    //关闭pstmt对象
    //关闭connection对象
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
  }

  public boolean delete(Integer id)throws SQLException{
    Department department = this.find(id);
    return this.delete(department);
  }

  public boolean delete(Department department)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String deleteDepartment_sql="DELETE FROM department WHERE ID=?";
    PreparedStatement pstmt=connection.prepareStatement(deleteDepartment_sql);
    pstmt.setInt(1,department.getId());
    pstmt.executeUpdate();
    //执行预编译对象的executeUpdate方法，获取删除的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("删除了" + affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return departments.remove(department);
  }

  public static void main(String[] args)throws SQLException {
    DepartmentDao.getInstance().findAll();
    School school=SchoolService.getInstance().find(2);
    //Department d1=new Department("土管","01","",school);
    //DepartmentDao.getInstance().add(d1);
    Department department1=departmentDao.find(2);
    System.out.println(department1);
    department1.setDescription("园林");
    //department1.setSchool(school);
    DepartmentDao.getInstance().update(department1);
    Department department2=departmentDao.find(2);
    System.out.println(department2);
  }
}

