package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles;
	/*static{
		profTitles = new TreeSet<ProfTitle>();
	}*/
	public Collection<ProfTitle> findAll()throws SQLException {
    profTitles = new TreeSet<ProfTitle>();
	   Connection connection= JdbcHelper.getConn();
    //执行sql查询语句，并获得结果集
    Statement stmt=connection.createStatement();
    ResultSet resultSet=stmt.executeQuery("select * from profTitle");

    while(resultSet.next()) {
      ProfTitle profTitle = new ProfTitle(
        resultSet.getInt("id"),
        resultSet.getString("description"),
        resultSet.getString("no"),
        resultSet.getString("remarks"));
      profTitles.add(profTitle);
    }
    JdbcHelper.close(stmt,connection);
    return ProfTitleDao.profTitles;

	}

	public ProfTitle find(Integer id)throws SQLException{
	  ProfTitleDao.getInstance().findAll();
		ProfTitle desiredProfTitle = null;
		for (ProfTitle profTitle : profTitles) {
			if(id.equals(profTitle.getId())){
				desiredProfTitle =  profTitle;
				break;
			}
		}
		return desiredProfTitle;
	}

	public boolean update(ProfTitle profTitle)throws SQLException{
	 Connection connection=JdbcHelper.getConn();
    String updateProfTitle_sql="UPDATE profTitle SET description=?,no=?,remarks=? where id=?";
    PreparedStatement pstmt=connection.prepareStatement(updateProfTitle_sql);
    pstmt.setString(1,profTitle.getDescription());
    pstmt.setString(2,profTitle.getNo());
    pstmt.setString(3,profTitle.getRemarks());
    pstmt.setInt(4,profTitle.getId());
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("修改了"+ affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
	}

	public boolean add(ProfTitle profTitle)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String addProfTitle_sql="INSERT INTO profTitle (description,no,remarks) VALUES(?,?,?)";
    //在该连接上创建预编译语句对象
    PreparedStatement pstmt=connection.prepareStatement(addProfTitle_sql);
    pstmt.setString(1,profTitle.getDescription());
    pstmt.setString(2,profTitle.getNo());
    pstmt.setString(3,profTitle.getRemarks());
    //执行预编译对象的executeUpdate方法，获取添加的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("添加了"+ affectedRowNum + "条记录");
    //关闭pstmt对象
    //关闭connection对象
    JdbcHelper.close(pstmt,connection);
    return affectedRowNum>0;
	}

	public boolean delete(Integer id)throws SQLException{
		ProfTitle profTitle = this.find(id);
		return this.delete(profTitle);
	}

	public boolean delete(ProfTitle profTitle)throws SQLException{
    Connection connection=JdbcHelper.getConn();
    String deleteProfTitle_sql="DELETE FROM profTitle WHERE ID=?";
    PreparedStatement pstmt=connection.prepareStatement(deleteProfTitle_sql);
    pstmt.setInt(1,profTitle.getId());
    pstmt.executeUpdate();
    //执行预编译对象的executeUpdate方法，获取删除的记录行数
    int affectedRowNum=pstmt.executeUpdate();
    System.out.println("删除了" + affectedRowNum + "条记录");
    JdbcHelper.close(pstmt,connection);
    return profTitles.remove(profTitle);
	}
  public static void main(String[] args)throws SQLException {
    ProfTitleDao.getInstance().findAll();
    ProfTitle profTitle1=new ProfTitle("教授","01","");
    ProfTitleDao.getInstance().add(profTitle1);
    ProfTitle profTitle2=ProfTitleDao.getInstance().find(1);
    System.out.println(profTitle2);
    profTitle2.setDescription("副教授");
    System.out.println(profTitle2);
    ProfTitleDao.getInstance().update(profTitle2);
    System.out.println(profTitle1);

  }
}

