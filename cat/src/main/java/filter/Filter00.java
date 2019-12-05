//201802104003冯梦真
package filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter(filterName = "Filter00",urlPatterns = "/*"/*该过滤器适用于所有资源*/)
public class Filter00 implements Filter {
  private Set<String> exclude=new HashSet<>();
  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    //exclude.add("/login");
    System.out.println("Filter00开始");
    //强制类型转换，把请求和响应转换为http类型
    HttpServletRequest request=(HttpServletRequest)req;
    HttpServletResponse response=(HttpServletResponse) resp;
    //获得被请求资源的URL
    //String path=request.getRequestURI();
    //如果path中包含/login
    /*if (exclude.contains(path)){
      System.out.println("请求为/login目录下，不做处理");
    }*/
    //path中不包含/login，执行以下逻辑
    //else {
      //如果请求的方法是post，或put，设置请求和响应编码
      if (request.getMethod().equals("POST")||request.getMethod().equals("PUT")){
        request.setCharacterEncoding("UTF-8");
      }
      //设置响应编码
    response.setContentType("text/html;charset=UTF-8");

 //   }
    //执行其他过滤器，若过滤器已执行完毕，则执行原请求
    chain.doFilter(req, resp);
    System.out.println("Filter00结束");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void destroy() {

  }
}
