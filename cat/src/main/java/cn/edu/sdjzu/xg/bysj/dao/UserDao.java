package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}

	public Collection<User> findAll()throws SQLException{
	  Collection<User> users=new TreeSet<User>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from user");
    while (resultSet.next()){
      User user=new User(resultSet.getInt("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
      users.add(user);
    }
    JdbcHelper.close(stmt,connection);
    return users;
	}

	public User find(Integer id)throws SQLException{
    Collection<User> users=new TreeSet<User>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from user");
    while (resultSet.next()){
      User user=new User(resultSet.getInt("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
      users.add(user);
    }
    JdbcHelper.close(stmt,connection);
		User desiredUser = null;
		for (User user : users) {
			if(id.equals(user.getId())){
				desiredUser =  user;
				break;
			}
		}
		return desiredUser;
	}
  public User findByUsername(String username)throws SQLException{
    Collection<User> users=new TreeSet<User>();
    Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from user");
    while (resultSet.next()){
      User user=new User(resultSet.getInt("id"),
        resultSet.getString("username"),
        resultSet.getString("password"),
        TeacherService.getInstance().find(resultSet.getInt("teacher_id")));
      users.add(user);
    }
    JdbcHelper.close(stmt,connection);
    User desiredUser = null;
    for (User user : users) {
      if(username.equals(user.getUsername())){
        desiredUser =  user;
        break;
      }
    }
    return desiredUser;
  }
	public boolean changPassword(User user)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String updateTeacher_sql="UPDATE user SET password=? where id=?";
    PreparedStatement pstmt=connection.prepareStatement(updateTeacher_sql);
    pstmt.setString(1,user.getPassword());
    pstmt.setInt(2,user.getId());
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("修改了"+ affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
	}

	public boolean add(User user)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String addTeacher_sql="INSERT INTO user(username,password,teacher_id) VALUES(?,?,?)";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(addTeacher_sql);
    pstmt.setString(1,user.getUsername());
    pstmt.setString(2,user.getPassword());
    pstmt.setInt(3,user.getTeacher().getId());
    //执行预编译对象的executeUpdate方法，获取添加的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("添加了"+ affectedRowNum + "条记录");
    //关闭pstmt对象
    //关闭connection对象
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
	}

	public boolean delete(Integer id)throws SQLException{
	  User user=this.find(id);
	  return this.delete(user);
	}

	public boolean delete(User user)throws SQLException{
	  Connection connection=JdbcHelper.getConn();
	  String delete_sql="DELETE FROM user WHERE id=?";
	  PreparedStatement pstmt=connection.prepareStatement(delete_sql);
	  pstmt.setInt(1,user.getId());
	  int affectedRowNum=pstmt.executeUpdate();
    System.out.println("删除了"+ affectedRowNum+ "条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
	}


	public static void main(String[] args)throws SQLException{
		/*UserDao dao = new UserDao();
      Collection<User> users = dao.findAll();
      display(users);*/
    System.out.println(UserDao.getInstance().login("1","2"));

	}
  public User login(String username, String password)throws SQLException{
    Collection<User> users = this.findAll();
    User desiredUser = null;
    for(User user:users){
      if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
        desiredUser = user;
      }
    }
    return desiredUser;
  }

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}



}
