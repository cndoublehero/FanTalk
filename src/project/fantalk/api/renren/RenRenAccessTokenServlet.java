package project.fantalk.api.renren;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.fantalk.api.Utils;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


public class RenRenAccessTokenServlet extends HttpServlet {
	/** 日志工具 */
	private static final Logger logger = Logger
			.getLogger(RenRenAccessTokenServlet.class.getName());

	private static final long serialVersionUID = -2420180291902217615L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		logger.info("code =====" + code);
		if (!Utils.isEmpty(code)) {
			/** 获取UseService，读取Member，将数据写入Datastore和缓存 */
			UserService userService = UserServiceFactory.getUserService();
			String email = userService.getCurrentUser().getEmail();
			Member m = Datastore.getInstance().getAndCacheMember(email);
			RenRenServiceOauth2 renren = new RenRenServiceOauth2(m, code);
			m = renren.processMember(m);
			m.put();
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out
					.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD>");
			out
					.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
			out
					.println("<meta http-equiv=\"refresh\" content=\"5;url=/web/index.jsp\">");
			out
					.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/main.css\" />");
			out.println("<TITLE>FanTalk - 绑定帐号</TITLE>");
			out.println("</HEAD>");
			out.println("  <BODY>");
			out.println("<h1>FanTalk</h1>");
			out.println("<p class=\"center-large\">");
			out.println("帐号绑定成功!<br />");
			out
					.print("<p class=\"center\"><a href=\"/web/index.jsp\">5秒钟后自动返回首页......</p>");
			out.println("</p>");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
	}
}
