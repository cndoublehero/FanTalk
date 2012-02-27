package project.fantalk.api.nets;

import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractOAuth;
import project.fantalk.model.Member;


public class NetsServiceOAuth extends AbstractOAuth {
	private static final Logger logger = Logger
			.getLogger(NetsServiceOAuth.class.getName());

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

	public ReturnCode update(String text) {

		try {
			String params = "status=" + Utils.encode(text);
			String data = doPost(API.UPDATE_STATUS.url(), params);
			JSONObject o = new JSONObject(data);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.warning(e.getMessage());
		} catch (RuntimeException e) {
			logger.warning(e.getMessage());

		}
		return ReturnCode.ERROR_FALSE;

	}

	public ReturnCode verify() {
		try {
			String data = doGet(API.VERIFY_ACCOUNT.url());
			JSONObject o = new JSONObject(data);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
		}
		return ReturnCode.ERROR_FALSE;

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
