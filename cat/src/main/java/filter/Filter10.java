//201802104003冯梦真
package filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "Filter10",urlPatterns = "/*"/*该过滤器适用于所有资源*/)
public class Filter10 implements Filter {
  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    System.out.println("Filter10开始");
    //强制类型转换，把请求转换为http类型
    HttpServletRequest request=(HttpServletRequest)req;
    //获得被请求资源的URL
    String path=request.getRequestURI();
    //打印出被请求资源名称和请求时间
    System.out.println(path+"@"+new Date());
    //执行其他过滤器，若过滤器已执行完毕，则执行原请求
    chain.doFilter(req, resp);
    System.out.println("Filter10结束");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void destroy() {

  }
}
