package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.service.DegreeService;
import cn.edu.sdjzu.xg.bysj.service.ProfTitleService;
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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
  /**
   * Post http://49.234.231.13:8080/bysj1803_24/profTitle.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //根据request对象，获得代表参数的JSON字串
    String profTitle_json = JSONUtil.getJSON(request);

    //将JSON字串解析为ProfTitle对象
    ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
    //用大于4的随机数给degreeToAdd的id赋值
    profTitleToAdd.setId(4 + (int) (1000 * Math.random()));
    //创建JSON对象
    JSONObject resp = new JSONObject();
    try {
      //增加加ProfTitle对象
      ProfTitleService.getInstance().add(profTitleToAdd);
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
   * Put http://49.234.231.13:8080/bysj1803_24/profTitle.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doPut(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    String profTitle_json = JSONUtil.getJSON(request);
    //将JSON字串解析为ProfTitle对象
    ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json,ProfTitle.class);
    //创建JSON对象message，以便往前端响应信息
    JSONObject message = new JSONObject();
    try{
    //修改ProfTitle表的记录
    ProfTitleService.getInstance().update(profTitleToAdd);
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
   * Delete http://49.234.231.13:8080/bysj1803_24/profTitle.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    //读取参数id
    String id_str = request.getParameter("id");
    int id = Integer.parseInt(id_str);
    //创建json对象，以便向前台响应信息
    JSONObject resp = new JSONObject();
    try {
      //删除学位
      DegreeService.getInstance().delete(id);
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

  /**
   * Get http://49.234.231.13:8080/bysj1803_24/profTitle.ctl
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String id_str=request.getParameter("id");
    JSONObject resp=new JSONObject();
    try {
      if (id_str==null){
        responseProfTitle(response);
      }
      else {
        int id=Integer.parseInt(id_str);
        responseProfTitle(id,response);
      }
      resp.put("MSG","查找成功");
    } catch (SQLException e){
      //加入数据信息
      resp.put("MSG","数据库操作异常");
    }catch (Exception e){
      resp.put("MSG","网络异常");
    }
  }
  //响应一个职称对象
  private void responseProfTitle(int id, HttpServletResponse response)
    throws SQLException, IOException {
    //根据id查找职称
    ProfTitle profTitle = ProfTitleService.getInstance().find(id);
    String profTitle_json = JSON.toJSONString(profTitle);
    //响应profTitle_json到前端
    response.getWriter().println(profTitle_json);
  }

  //响应所有职称对象
  private void responseProfTitle(HttpServletResponse response)
    throws SQLException, IOException {
    //获得所有职称
    Collection<ProfTitle> profTitles = ProfTitleService.getInstance().findAll();
    String profTitles_json = JSON.toJSONString(profTitles);
    //响应profTitles_json到前端
    response.getWriter().println(profTitles_json);
  }
}
