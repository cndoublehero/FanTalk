package project.fantalk.api.sina;

import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.basicAuth.AbstractBasicAuth;
import project.fantalk.model.Member;


@Deprecated
/**
 * 为新浪微博提供的实现类，采用Basic验证方式，容易泄漏密码，不推荐使用
 * 
 * */
public class SinaServiceBasicAuth extends AbstractBasicAuth {
	/** 日志工具 */
	private static final Logger logger = Logger
			.getLogger(SinaServiceBasicAuth.class.getName());

	public SinaServiceBasicAuth(String username, String password) {
		super(username, password);
	}

	public SinaServiceBasicAuth(Member member) {
		super(member.getSinaUsername(), member.getSinaPassword());
	}

	public ReturnCode verify() {
		try {
			String params = "source=" + Utils.encode(getSource());
			String data = doGet(API.VERIFY_ACCOUNT.url(), params);
			JSONObject o = new JSONObject(data);
			if (o.has("id")) {
				return ReturnCode.ERROR_OK;
			}
		} catch (JSONException e) {
		}
		return ReturnCode.ERROR_FALSE;
	}

	public ReturnCode update(String text) {
		try {
			String params = "status=" + Utils.encode(text) + "&source="
					+ Utils.encode(getSource());
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

	public Member processMember(Member member) {
		member.setSinaUsername(getUsername());
		member.setSinaPassword(getPassword());
		member.put();
		return member;
	}

	@Override
	public String getSNSName() {
		return "sina";
	}

	public String getBindErrorMessage() {
		return "新浪微博帐号绑定失败(如果是中文用户名，请使用邮箱或手机号进行绑定操作)，请检查帐号和密码是否正确!";
	}

	public String getBindOkMessage() {
		return "新浪微博帐号绑定成功!";
	}

	public static final String DETAIL = "http://api.t.sina.com.cn/users/show.json";

	public String detail() {
		String url = "source=" + Utils.encode(getSource())
				+ "&user_id=1912699125";
		String data = doGet(DETAIL, url);
		StringBuilder sb = new StringBuilder();
		try {
			JSONObject o = new JSONObject(data);
			sb.append(o.getString("name"));
			sb.append(o.getString("description"));
		} catch (JSONException e) {
			sb.append(e.getMessage());
			e.printStackTrace();
		}
		return sb.toString();

	}

	@Override
	public String getSource() {
		return SinaConstant.apiKey;
	}

	/**
	 * 枚举，新浪微博API方法接口URL列表
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
