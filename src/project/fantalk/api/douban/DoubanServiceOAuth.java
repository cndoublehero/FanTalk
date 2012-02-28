package project.fantalk.api.douban;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DouBanApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.model.Member;

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
	public OAuthService getOAuthService() {
		return new ServiceBuilder().provider(DouBanApi.class)
				.apiKey(DoubanConstant.apiKey).apiSecret(DoubanConstant.secret)
				.debugStream(System.out).build();
	}
	
	@Override
	public Response signAndSendPostRequest(String url, String param) {
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, url);
		OAuthService oAuthService = getOAuthService();
		if (oAuthService != null) {
			oAuthService.signRequest(new Token(getUsername(), getPassword()),
					oAuthRequest);
		}
		oAuthRequest.addHeader("Content-Type", "application/atom+xml");
		oAuthRequest.addPayload(param);
		return oAuthRequest.send();
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
