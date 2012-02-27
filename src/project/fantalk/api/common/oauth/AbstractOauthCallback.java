package project.fantalk.api.common.oauth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public abstract class AbstractOauthCallback extends HttpServlet {

    private static final long serialVersionUID = -5126341471110972209L;

    private static final Logger logger = Logger
            .getLogger(AbstractOauthCallback.class.getName());

    public abstract String getApiKey();

    public abstract String getApiSecret();

    public abstract String getRequestTokenURL();

    public abstract String getAccessTokenURL();

    public abstract String getAuthorizeURL();

    public abstract Member processToken(String accessToken,
            String accessTokenSecret, Member m);

    public abstract void rollback(Member m);

    public abstract OAuthProvider processProvider(OAuthProvider provider);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String oauth_verifier = request.getParameter("oauth_verifier");
        logger.info("\n===Oauth=== oauth_verifier: " + oauth_verifier);
        if (oauth_verifier == null)
            oauth_verifier = "";
        String token = (String) request.getSession().getAttribute("token");
        String tokenSecret = (String) request.getSession().getAttribute(
                "tokenSecret");
        OAuthConsumer consumer = getConsumer(token, tokenSecret);
        OAuthProvider provider = getProvider();
        provider = processProvider(provider);
        UserService userService = UserServiceFactory.getUserService();
        String email = userService.getCurrentUser().getEmail();
        Member m = Datastore.getInstance().getAndCacheMember(email);
        try {
            provider.retrieveAccessToken(consumer, oauth_verifier);
            String accessToken = consumer.getToken();
            String accessTokenSecret = consumer.getTokenSecret();
            logger.info("\n===Oauth=== access-token: " + accessToken);
            logger.info("\n===Oauth=== access-token-secret: "
                    + accessTokenSecret);
            processToken(accessToken, accessTokenSecret, m);
        } catch (Exception e) {
            // rollback(m);
            logger.warning(e.getMessage());
            // message = "帐号绑定失败!";
            // printResult(response, message);
        }
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

    public OAuthProvider getProvider() {
        OAuthProvider provider = new DefaultOAuthProvider(getRequestTokenURL(),
                getAccessTokenURL(), getAuthorizeURL());
        return provider;
    }

    public OAuthConsumer getConsumer(String token, String tokenSecret) {
        OAuthConsumer consumer = new DefaultOAuthConsumer(getApiKey(),
                getApiSecret());
        consumer.setTokenWithSecret(token, tokenSecret);
        return consumer;
    }
}
