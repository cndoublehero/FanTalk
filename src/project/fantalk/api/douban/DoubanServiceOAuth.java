package project.fantalk.api.douban;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.api.common.oauth.OAuth;
import project.fantalk.api.common.oauth.OAuthToken;
import project.fantalk.api.common.oauth.PostParameter;
import project.fantalk.model.Member;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;

public class DoubanServiceOAuth extends AbstractOAuth {
	private static final Logger logger = Logger
			.getLogger(DoubanServiceOAuth.class.getName());

	public DoubanServiceOAuth(String username, String password) {
		super(username, password);
	}

	public DoubanServiceOAuth(Member member) {
		super(member.getDoubanUsername(), member.getDoubanPassword());
	}

	@Override
	public String getApiKey() {
		return DoubanConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return DoubanConstant.secret;
	}

	@Override
	public String getSource() {
		return DoubanConstant.apiKey;
	}

	@Override
	public String getSNSName() {
		return "豆瓣我说";
	}

	@Override
	public HTTPRequest processRequeset(HTTPRequest request, String params) {
		OAuth oauth = new OAuth(getApiKey(), getApiSecret());
		long timestamp = System.currentTimeMillis() / 1000;
		long nonce = System.nanoTime();
		String url = request.getURL().toString();

		String authorization = null;

		logger.log(Level.INFO, "params==" + params + ",Method="
				+ request.getMethod().toString() + "URL = " + url);
		authorization = oauth.generateAuthorizationHeader(request.getMethod()
				.toString(), url, PostParameter.parseGetParameters(null),
				String.valueOf(nonce), String.valueOf(timestamp),
				new OAuthToken(getUsername(), getPassword()));
		logger.log(Level.INFO, "dssd==" + authorization);
		request.addHeader(new HTTPHeader("Authorization", authorization));
		if (HTTPMethod.POST == request.getMethod())
			request.addHeader(new HTTPHeader("Content-Type", "application/atom+xml"));
		return request;
	}

	public ReturnCode update(String text) {

		String params = createSaying(text);
		String data = doPost(API.UPDATE_STATUS.url(), params);
		logger.log(Level.INFO, "data===" + data);
		if (data != null && data.indexOf("xml") != -1) {
			return ReturnCode.ERROR_OK;
		}
		return ReturnCode.ERROR_FALSE;

	}

	public ReturnCode verify() {
		try {
			String params = "apikey=" + Utils.encode(getApiKey()) + "&alt=json";
			String data = doGet(API.VERIFY_ACCOUNT.url(), params);
			JSONObject o = new JSONObject(data);
			logger.log(Level.INFO, "json===" + o);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return ReturnCode.ERROR_FALSE;

	}

	private String createSaying(String content) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb
				.append("<entry xmlns:ns0=\"http://www.w3.org/2005/Atom\" xmlns:db=\"http://www.douban.com/xmlns/\">");
		sb.append("<content>"+content+"</content>");
		sb.append("</entry>");
		return sb.toString();
	}

	/**
	 * 枚举，豆瓣我说API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
	static enum API {
		UPDATE_STATUS("miniblog/saying"), // POST 发布我说消息

		VERIFY_ACCOUNT("people/%40me"), // GET 验证帐号密码
		;

		private String value;
		private static final String API_URL = "http://api.douban.com/";// API地址
		private static final String JSON = "";// JSON格式

		API(String value) {
			this.value = value;
		}

		public String url() {
			return new StringBuilder().append(API_URL).append(value).append(
					JSON).toString();
		}
	}

}
