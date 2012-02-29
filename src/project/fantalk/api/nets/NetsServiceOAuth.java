package project.fantalk.api.nets;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.NetsApi;
import org.scribe.oauth.OAuthService;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.model.Member;

public class NetsServiceOAuth extends AbstractOAuth {
	private static final OAuthService NetsOAuthService = new ServiceBuilder()
			.provider(NetsApi.class).apiKey(NetsConstant.apiKey)
			.apiSecret(NetsConstant.secret).build();
	
	public NetsServiceOAuth(String username, String password) {
		super(username, password);
	}

	public NetsServiceOAuth(Member member) {
		super(member.getNeteaseUsername(), member.getNeteasePassword());
	}

	@Override
	public String getApiKey() {
		return NetsConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return NetsConstant.secret;
	}

	@Override
	public String getSource() {
		return NetsConstant.apiKey;
	}

	@Override
	public String getSNSName() {
		return "网易微博";
	}

	@Override
	public OAuthService getOAuthService() {
		return NetsOAuthService;
	}
	
	public ReturnCode update(String text) {
		String params = "status=" + Utils.encode(text);
		String data = doPost(API.UPDATE_STATUS.url(), params);
		return getActionCode(data);
	}

	public ReturnCode verify() {
		String data = doGet(API.VERIFY_ACCOUNT.url());
		return getActionCode(data);
	}

	/**
	 * 枚举，网易微博API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
	static enum API {
		UPDATE_STATUS("statuses/update"), // POST 发布消息

		VERIFY_ACCOUNT("account/verify_credentials"), // GET 验证帐号密码
		;

		private String value;
		private static final String API_URL = "http://api.t.163.com/";// 网易API地址
		private static final String JSON = ".json";// JSON格式

		API(String value) {
			this.value = value;
		}

		public String url() {
			return new StringBuilder().append(API_URL).append(value).append(
					JSON).toString();
		}
	}
}
