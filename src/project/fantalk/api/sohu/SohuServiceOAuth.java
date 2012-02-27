package project.fantalk.api.sohu;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.oauth.AbstractUrlOauth;
import project.fantalk.model.Member;


public class SohuServiceOAuth extends AbstractUrlOauth {
	private static final Logger logger = Logger
			.getLogger(SohuServiceOAuth.class.getName());

	public SohuServiceOAuth(String username, String password) {
		super(username, password);
	}

	public SohuServiceOAuth(Member member) {
		super(member.getSohuUsername(), member.getSohuPassword());
	}

	@Override
	public Member processMember(Member member) {
		member.setSohuUsername(getUsername());
		member.setSohuPassword(getPassword());
		member.put();
		return member;
	}

	@Override
	public String getApiKey() {
		return SohuConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return SohuConstant.secret;
	}

	@Override
	public String getSource() {
		return SohuConstant.apiKey;
	}

	@Override
	public String getSNSName() {
		return "搜狐微博";
	}
	
	public ReturnCode update(String text) {

		try {
			String params = "status=" + Utils.encode(text);
			String data = doPost(API.UPDATE_STATUS.url(), params);
			JSONObject o = new JSONObject(data);
			logger.info(o.toString());
			if (o.has("text")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return ReturnCode.ERROR_FALSE;

	}

	public ReturnCode verify() {
		try {
			String data = doGet(API.VERIFY_ACCOUNT.url());
			JSONObject o = new JSONObject(data);
			logger.info(o.toString());
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return ReturnCode.ERROR_FALSE;

	}

	/**
	 * 枚举，搜狐微博API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
	static enum API {
		UPDATE_STATUS("statuses/update"), // POST 发布消息

		VERIFY_ACCOUNT("users/show"), // GET 验证帐号密码
		;

		private String value;
		private static final String API_URL = "http://api.t.sohu.com/";// 腾讯API地址
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
