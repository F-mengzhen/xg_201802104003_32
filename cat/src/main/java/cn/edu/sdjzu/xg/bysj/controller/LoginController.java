package cn.edu.sdjzu.xg.bysj.controller;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login.ctl")
public class LoginController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String uaername = request.getParameter("username");
      String password = request.getParameter("password");
      JSONObject message = new JSONObject();
      try {
        User loggeduser = UserService.getInstance().login(uaername, password);
        if (loggeduser!=null){
          message.put("message","登录成功");
          HttpSession session=request.getSession();
          //十分钟没有操作，则使session无效
          session.setMaxInactiveInterval(10*60);
          session.setAttribute("currentUser",loggeduser);
          response.getWriter().println(message);
          return;
        }else {
          message.put("message","用户名或密码错误");
        }
      }catch (SQLException e){
        message.put("message","数据库操作异常");
      }catch (Exception e){
        message.put("message","网络异常");
      }
    }


    }
