package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
  //http://49.234.231.13:8080/bysj1803_25/myschool
    //Post http://49.234.231.13:8080/addCollege
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //根据request对象，获得代表参数的JSON字串
      String school_json = JSONUtil.getJSON(request);

      //将JSON字串解析为School对象
      School schoolToAdd = JSON.parseObject(school_json,School.class);
      //用大于4的随机数给schoolToAdd的id赋值
      schoolToAdd.setId(4 + (int)(1000*Math.random()));

      //创建JSON对象
      JSONObject resp = new JSONObject();
      try {
        //增加加School对象
        SchoolService.getInstance().add(schoolToAdd);
        //加入数据信息
        resp.put("MSG", "添加成功");
      }catch (SQLException e){
        //加入数据信息
        resp.put("MSG","数据库操作异常");
      }catch (Exception e){
        resp.put("MSG","网络异常");
      }
      //响应
      response.getWriter().println(resp);
    }
    //http://49.234.231.13:8080/bysj1803_25/myschool
// http://49.234.231.13:8080/updateCollege
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String school_json = JSONUtil.getJSON(request);
    //将JSON字串解析为School对象
    School schoolToAdd = JSON.parseObject(school_json,School.class);

    //创建JSON对象message，以便往前端响应信息
    JSONObject message = new JSONObject();
    try{
      //增加加School对象
      SchoolService.getInstance().update(schoolToAdd);
      //加入响应信息
      message.put("message", "更新成功");
    }catch (SQLException e){
      //加入数据信息
      message.put("MSG","数据库操作异常");
    }catch (Exception e){
      message.put("MSG","网络异常");
    }
    //响应message到前端
    response.getWriter().println(message);
  }
  //http://49.234.231.13:8080/bysj1803_25/myschool
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //读取参数id
    String id_str = request.getParameter("id");
    int id = Integer.parseInt(id_str);
    JSONObject resp = new JSONObject();

    try {
      //删除学院
     SchoolService.getInstance().delete(id);
      //加入数据信息
      resp.put("MSG", "删除成功");
    } catch (SQLException e){
      //加入数据信息
      resp.put("MSG","数据库操作异常");
    }catch (Exception e){
      resp.put("MSG","网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }
// http://49.234.231.13:8080/bysj1803_25/myschool
  //http://49.234.231.13:8080/school

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String id_str=request.getParameter("id");
    System.out.println("aaaaaa");
      JSONObject resp =new JSONObject();
      try {
        if (id_str==null){
          responseSchool(response);
        }
        else {
          int id=Integer.parseInt(id_str);
          responseSchool(id,response);
        }
        resp.put("MSG","查找成功");
       }
      catch (SQLException e){
        //加入数据信息
        resp.put("MSG","数据库操作异常");
      }catch (Exception e){
        resp.put("MSG","网络异常");
      }
    }
  //响应一个学院对象
  private void responseSchool(int id, HttpServletResponse response)
    throws SQLException, IOException {
    //根据id查找学院
    School school = SchoolService.getInstance().find(id);
    String school_json = JSON.toJSONString(school);
    //响应school_json到前端
    response.getWriter().println(school_json);
  }

  //响应所有学院对象
  private void responseSchool(HttpServletResponse response)
    throws SQLException, IOException {
    //获得所有学院
    Collection<School> schools = SchoolService.getInstance().findAll();
    String schools_json = JSON.toJSONString(schools);
    //响应schools_json到前端
    response.getWriter().println(schools_json);
  }
}
