package project.fantalk.api.common.oauth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import project.fantalk.api.Utils;

public abstract class AbstractOauthServlet extends HttpServlet {
	/** 日志工具 */
	private static final Logger logger = Logger.getLogger(AbstractOauthServlet.class
			.getName());

	private static final long serialVersionUID = -2420180291903617615L;

	public abstract String getApiKey();

	public abstract String getApiSecret();

	public abstract String getRequestTokenURL();

	public abstract String getAccessTokenURL();

	public abstract String getAuthorizeURL();

	public abstract String getCallBackUrl();

	public abstract String processCallBackUrl(String callBackUrl);

	public abstract OAuthProvider processProvider(OAuthProvider provider);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");

		OAuthConsumer consumer = getConsumer();
		OAuthProvider provider = getProvider();
		provider = processProvider(provider);
		try {
			String callbackUrl = Utils.getBaseURL(request) + getCallBackUrl();
			String authURL = provider.retrieveRequestToken(consumer,
					callbackUrl);
			request.getSession().setAttribute("token", consumer.getToken());
			request.getSession().setAttribute("tokenSecret",
					consumer.getTokenSecret());
			authURL = processCallBackUrl(authURL);
			response.sendRedirect(authURL);
			return;
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public OAuthProvider getProvider() {
		OAuthProvider provider = new DefaultOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		return provider;
	}

	public OAuthConsumer getConsumer() {
		OAuthConsumer consumer = new DefaultOAuthConsumer(getApiKey(),
				getApiSecret());
		return consumer;
	}

}
