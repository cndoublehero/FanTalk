package project.fantalk.api.renren;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RenRenAuthorizeServlet extends HttpServlet {
	/** 日志工具 */
	private static final Logger logger = Logger
			.getLogger(RenRenAuthorizeServlet.class.getName());

	private static final long serialVersionUID = -2420180233303617615L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String authURL = null;
		// https://graph.renren.com/oauth/authorize?client_id=881f3ee481354ffb802753cc2b4e8f18&response_type=token&redirect_uri=http://graph.renren.com/oauth/login_success.html
		StringBuilder sb = new StringBuilder();
		sb.append(RenRenConstant.authorizationURL).append("?client_id=")
				.append(RenRenConstant.apiKey).append(
						"&response_type=code&redirect_uri=").append(
						RenRenConstant.callBackURL);
		authURL = sb.toString();
		logger.log(Level.INFO, "资源Url为：" + authURL);
		response.sendRedirect(authURL);
	}
}
