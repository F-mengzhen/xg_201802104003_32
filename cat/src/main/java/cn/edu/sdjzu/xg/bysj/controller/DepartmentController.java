package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
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

@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
  /**
   * Post http://49.234.231.13:8080/bysj1803_24/department.ctl
   * 添加系
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //根据request对象，获得代表参数的JSON字串
    String department_json = JSONUtil.getJSON(request);

    //将JSON字串解析为Department对象
    Department departmentToAdd = JSON.parseObject(department_json, Department.class);
    //用大于4的随机数给DepartmentToAdd的id赋值
    departmentToAdd.setId(4 + (int) (1000 * Math.random()));
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try {
      //增加加Depatrment对象
      DepartmentService.getInstance().add(departmentToAdd);
      //加入数据信息
      resp.put("Message", "添加成功");
    }catch (SQLException e){
      //加入数据信息
      resp.put("Message","数据库操作异常");
    }catch (Exception e){
      resp.put("Message","网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }

  /**
   * Delete http://49.234.231.13:8080/bysj1803_24/department.ctl
   * 删除系
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
    //读取参数id
    String id_str = request.getParameter("id");
    int id = Integer.parseInt(id_str);
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try{
      //删除专业
      DepartmentService.getInstance().delete(id);
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
   * Put http://49.234.231.13:8080/bysj1803_24/department.ctl
   * 修改系
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doPut(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    String department_json = JSONUtil.getJSON(request);
    //将JSON字串解析为School对象
    Department departmentToAdd = JSON.parseObject(department_json,Department.class);
    System.out.println("schoolToAdd="+departmentToAdd);
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try{
      //增加加School对象
      DepartmentService.getInstance().update(departmentToAdd);
      //加入数据信息
      resp.put("MSG","更新成功");
    }
    catch (SQLException e){
      //加入数据信息
      resp.put("MSG","数据库操作异常");
    }catch (Exception e){
      resp.put("MSG","网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }

  /**
   * Get http://49.234.231.13:8080/bysj1803_24/department.ctl
   * 查找系
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String paraType=request.getParameter("paraType");
    String id_str=request.getParameter("id");
    JSONObject resp=new JSONObject();
    try {
      if (id_str != null) {
        int id = Integer.parseInt(id_str);
        if (paraType!=null){
          if (paraType.equals("school")) {
            responseDepartment1(id, response);
          }
        }
        else {
          responseDepartment(id,response);
        }
      }
      else{
        responseDepartment(response);
      }
      resp.put("MSG","查找成功");
    }catch (SQLException e){
      //加入数据信息
      resp.put("MSG","数据库操作异常");
    }catch (Exception e){
      resp.put("MSG","网络异常");
    }

  }
  //响应一个系对象
  private void responseDepartment(int id, HttpServletResponse response)
    throws SQLException, IOException {
    //根据id查找学院
    Department department = DepartmentService.getInstance().find(id);
    String department_json = JSON.toJSONString(department);
    response.getWriter().println(department_json);
  }
  private void responseDepartment1(int id,HttpServletResponse response)
    throws SQLException,IOException {
    //根据school_id查找department
    Collection<Department> departments=DepartmentService.getInstance().findAllBySchool(id);
    //获得JSON字串
    String departments_json =JSON.toJSONString(departments);
    //响应department_json到前端
    response.getWriter().println(departments_json);
  }
  //响应所有系对象
  private void responseDepartment(HttpServletResponse response)
    throws SQLException, IOException {
    //获得所有学院
    Collection<Department> departments = DepartmentService.getInstance().findAll();
    String departments_json = JSON.toJSONString(departments);
    //响应departments_json到前端
    response.getWriter().println(departments_json);
  }
}
