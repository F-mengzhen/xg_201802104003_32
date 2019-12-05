package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
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

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
  /**
   * Post http://49.234.231.13:8080/bysj1803_24/teacher.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //根据request对象，获得代表参数的JSON字串
      String degree_json = JSONUtil.getJSON(request);

      //将JSON字串解析为teacher对象
      Teacher teacherToAdd = JSON.parseObject(degree_json, Teacher.class);
      //用大于4的随机数给teacherToAdd的id赋值
      teacherToAdd.setId(4 + (int) (1000 * Math.random()));
      //创建JSON对象
      JSONObject resp = new JSONObject();
      try {
        //增加加Teacher对象
        TeacherService.getInstance().add(teacherToAdd);
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

  /**
   * Put http://49.234.231.13:8080/bysj1803_24/teacher.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String teacher_json = JSONUtil.getJSON(request);
    //将JSON字串解析为Teacher对象
    Teacher teacherToAdd = JSON.parseObject(teacher_json,Teacher.class);
    //创建JSON对象message，以便往前端响应信息
    JSONObject message = new JSONObject();
    try{
      //增加加Teacher对象
      TeacherService.getInstance().update(teacherToAdd);
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

  /**
   *Delete http://49.234.231.13:8080/bysj1803_24/teacher.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //读取参数id
    String id_str = request.getParameter("id");
    int id = Integer.parseInt(id_str);
    //创建JSON对象，以便于向前台响应信息
    JSONObject resp = new JSONObject();
    try {
      //删除教师
      TeacherService.getInstance().delete(id);
      //加入数据信息
      resp.put("MSG", "删除成功");
    }catch (SQLException e){
      //加入数据信息
      resp.put("MSG","数据库操作异常");
    }catch (Exception e){
      resp.put("MSG","网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }

  /**
   * Get http://49.234.231.13:8080/bysj1803_24/teacher.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id_str=request.getParameter("id");
        JSONObject resp=new JSONObject();
        try{
          if (id_str==null){
            responseTeacher(response);
          }else {
            int id=Integer.parseInt(id_str);
            responseTeacher(id,response);
          }
          resp.put("MSG","查找成功");
        }catch (SQLException e){
          //加入数据信息
          resp.put("MSG","数据库操作异常");
        }catch (Exception e){
          resp.put("MSG","网络异常");
        }
    }
  //响应一个教师对象
  private void responseTeacher(int id, HttpServletResponse response)
    throws SQLException, IOException {
    //根据id查找教师
    Teacher teacher = TeacherService.getInstance().find(id);
    String teacher_json = JSON.toJSONString(teacher);
    //响应teacher_json到前端
    response.getWriter().println(teacher_json);
  }

  //响应所有教师对象
  private void responseTeacher(HttpServletResponse response)
    throws SQLException, IOException {
    //获得所有教师
    Collection<Teacher> teachers = TeacherService.getInstance().findAll();
    String teachers_json = JSON.toJSONString(teachers);
    //响应teachers_json到前端
    response.getWriter().println(teachers_json);
  }
}
