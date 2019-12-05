//201802104003冯梦真
package test;

import cn.edu.sdjzu.xg.bysj.dao.SchoolDao;
import cn.edu.sdjzu.xg.bysj.domain.School;

import java.sql.SQLException;

public class SchoolDaoTest {
  public static void main(String[] args)throws SQLException,ClassNotFoundException {
    //创建school对象
    School schoolToAdd=new School("信电学院","121","");
    //创建Dao对象
    SchoolDao schoolDao=new SchoolDao();
    //执行Dao对象的方法
    School addSchool=schoolDao.addWithSP(schoolToAdd);
    //打印添加后返回的对象
    System.out.println(addSchool);
    System.out.println("添加school成功");
  }
}
