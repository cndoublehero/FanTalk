package project.fantalk.api.common.oauth;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;

public abstract class AbstractUrlOauth extends AbstractOAuth {
	private static final Logger logger = Logger
			.getLogger(AbstractUrlOauth.class.getName());

	public AbstractUrlOauth(String username, String password) {
		super(username, password);
	}

	@Override
	public HTTPRequest getRequest(String apiUrl, HTTPMethod method,
			String params) {

		HTTPRequest request = null;
		try {
			logger.log(Level.INFO, "未签名之前的的API为==" + apiUrl);
			OAuth oauth = new OAuth(getApiKey(), getApiSecret());
			long timestamp = System.currentTimeMillis() / 1000;
			long nonce = System.nanoTime();
			String authorization = null;
			authorization = oauth.generateAuthorizationUrl(method.toString(),
					apiUrl, PostParameter.parseGetParameters(params), String
							.valueOf(nonce), String.valueOf(timestamp),
					new OAuthToken(getUsername(), getPassword()));
			if (apiUrl.indexOf("?") != -1) {
				authorization = "&" + authorization;
			} else {
				authorization = "?" + authorization;
			}
			apiUrl = apiUrl + authorization;

			logger.log(Level.INFO, "签名的API为==" + apiUrl);
			URL url;
			url = new URL(apiUrl);
			request = new HTTPRequest(url, method);
			StringBuilder sb = new StringBuilder();

			if (method == HTTPMethod.POST) {
				processRequestParams(request, params);
				request.setPayload(params.getBytes());
				sb.append("Params: ").append(params);
			}

			sb.append("HTTP Request Content:\n").append("Method:").append(
					request.getMethod().toString()).append("\n").append("URL:")
					.append(request.getURL()).append("\n");
			logger.info(sb.toString());
		} catch (Exception e) {
			logger.log(Level.INFO, e.getMessage(), e);
			throw new RuntimeException("创建request过程失败 ", e);
		}
		return request;

	}
}
