package project.fantalk.api.digu;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.basicAuth.AbstractBasicAuth;
import project.fantalk.model.Member;


public class DiGuService extends AbstractBasicAuth {
	/** 日志工具 */
	private static final Logger logger = Logger.getLogger(DiGuService.class
			.getName());

	public DiGuService(String username, String password) {
		super(username, password);
	}

	public DiGuService(Member member) {
		super(member.getDiguUsername(), member.getDiguPassword());
	}

	public String getBindErrorMessage() {
		return "嘀咕帐号绑定失败(如果是中文用户名，请使用邮箱或手机号进行绑定操作)，请检查帐号和密码是否正确!";
	}

	public String getBindOkMessage() {
		return "嘀咕帐号绑定成功!";
	}

	@Override
	public String getSource() {
		return null;
	}

	@Override
	public String getSNSName() {
		return "digu";
	}

	@Override
	public Member processMember(Member member) {
		member.setDiguUsername(getUsername());
		member.setDiguPassword(getPassword());
		member.put();
		return member;
	}

	public ReturnCode update(String text) {

		try {
			String params = "content=" + Utils.encode(text);
			String data = doPost(API.UPDATE_STATUS.url(), params);
			JSONObject o = new JSONObject(data);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return ReturnCode.ERROR_FALSE;

	}

	public ReturnCode verify() {
		try {
			String params = "isAllInfo=false";
			String data = doGet(API.VERIFY_ACCOUNT.url(), params);
			JSONObject o = new JSONObject(data);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
		return ReturnCode.ERROR_FALSE;
	}

	/**
	 * 枚举，嘀咕微博API方法接口URL列表
	 * 
	 * @author mcxiaoke
	 */
	static enum API {
		UPDATE_STATUS("statuses/update"), // POST 发布消息

		VERIFY_ACCOUNT("account/verify"), // GET 验证帐号密码
		;

		private String value;
		private static final String API_URL = "http://api.minicloud.com.cn/";// 嘀咕API地址
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
