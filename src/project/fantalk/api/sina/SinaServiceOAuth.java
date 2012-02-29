package project.fantalk.api.sina;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.SinaWeiboApi;
import org.scribe.oauth.OAuthService;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.model.Member;

public class SinaServiceOAuth extends AbstractOAuth {
	private static final OAuthService SinaOAuthService = new ServiceBuilder()
			.provider(SinaWeiboApi.class).apiKey(SinaConstant.apiKey)
			.apiSecret(SinaConstant.secret).build();
	
	public SinaServiceOAuth(String username, String password) {
		super(username, password);
	}

	public SinaServiceOAuth(Member member) {
		super(member.getSinaUsername(), member.getSinaPassword());
	}

	@Override
	public String getSNSName() {
		return "sina微博";
	}

	@Override
	public Member processMember(Member member) {
		member.setSinaUsername(getUsername());
		member.setSinaPassword(getPassword());
		member.put();
		return member;
	}

	@Override
	public String getApiKey() {
		return SinaConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return SinaConstant.secret;
	}
	
	@Override
	public OAuthService getOAuthService() {
		return SinaOAuthService;
	}

	public ReturnCode update(String text) {
		String params = "status=" + Utils.encode(text) + "&source="
				+ Utils.encode(getSource());
		String data = doPost(API.UPDATE_STATUS.url(), params);
		return getActionCode(data);
	}

	public ReturnCode verify() {
		String params = "source=" + Utils.encode(getSource());
		String data = doGet(API.VERIFY_ACCOUNT.url(), params);
		return getActionCode(data);
	}

	public String getSource() {
		return SinaConstant.apiKey;
	}

	/**
	 * 枚举，腾讯微博API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
	static enum API {
		UPDATE_STATUS("statuses/update"), // POST 发布消息

		VERIFY_ACCOUNT("account/verify_credentials"), // GET 验证帐号密码
		;

		private String value;
		private static final String API_URL = "http://api.t.sina.com.cn/";// 新浪API地址
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
