package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
@WebServlet("/degree.ctl")

/**
 * Post http://49.234.231.13:8080/bysj1803_24/degree.ctl
 * 增加一个学位对象：将来自前端请求的JSON对象，增加到数据库表中
 * @param request
 * @param response
 * @throws ServletException
 * @throws IOException
 */
public class DegreeController extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //根据request对象，获得代表参数的JSON字串
    String degree_json = JSONUtil.getJSON(request);

    //将JSON字串解析为Degree对象
    Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
    //用大于4的随机数给degreeToAdd的id赋值
    degreeToAdd.setId(4 + (int) (1000 * Math.random()));
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try {
      //增加加Degree对象
      DegreeService.getInstance().add(degreeToAdd);
      //加入数据信息
      resp.put("MSG", "添加成功");
    } catch (SQLException e) {
      resp.put("MSG","数据库操异常");
    }catch (Exception e){
      resp.put("message", "网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }

  /**
   * Delete http://49.234.231.13:8080/bysj1803_24/degree.ctl
   * 删除学位
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */

  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //读取参数id
    String id_str = request.getParameter("id");
    int id = Integer.parseInt(id_str);
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try {
      //删除学位
      DegreeService.getInstance().delete(id);
      //加入数据信息
      resp.put("MSG", "删除成功");
    } catch (SQLException e) {
      resp.put("MSG","数据库操作异常");

    }catch (Exception e){
      resp.put("message", "网络异常");
    }
    //响应
    response.getWriter().println(resp);
  }

  /**
   * Put http://49.234.231.13:8080/bysj1803_24/degree.ctl
   * 修改学院
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doPut(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
    String degree_json = JSONUtil.getJSON(request);
    //将JSON字串解析为Degree对象
    Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
    //创建JSON对象message，以便往前端响应信息
    JSONObject message = new JSONObject();
    try {
      //增加加Degree对象
      DegreeService.getInstance().update(degreeToAdd);
      //加入数据信息
      message.put("message", "更新成功");
    }catch (SQLException e){
      message.put("MSG","数据库操作异常");
      e.printStackTrace();
    }catch (Exception e){
      message.put("message", "网络异常");
    }
    //响应message到前端
    response.getWriter().println(message);

  }

  /**
   * Get http://49.234.231.13:8080/bysj1803_24/degree.ctl
   * 查找学院
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //读取参数id
    String id_str = request.getParameter("id");
    //创建JSON对象message，以便往前端响应信息
    JSONObject message = new JSONObject();
    try {
      if(id_str==null){
      respondseDegrees(response);
      }
      else {
        int id=Integer.parseInt(id_str);
        respondseDegrees(id,response);
      }
      message.put("message","查找成功");

    } catch (SQLException e) {
      message.put("message","数据库操作异常");
    }catch (Exception e){
      message.put("message","网络异常");
    }
  }

  protected void respondseDegrees(Integer id, HttpServletResponse response) throws SQLException, IOException {

    Degree degree = DegreeService.getInstance().find(id);
    String degree_json = JSON.toJSONString(degree);
    //响应message到前端
    response.getWriter().println(degree_json);

  }

  protected void respondseDegrees(HttpServletResponse response) throws SQLException, IOException {
    //获得所有学院
    Collection<Degree> degrees = DegreeService.getInstance().findAll();
    String degrees_json = JSON.toJSONString(degrees);

    //响应message到前端
    response.getWriter().println(degrees_json);
  }
}
