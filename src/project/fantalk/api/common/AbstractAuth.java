package project.fantalk.api.common;

import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

import project.fantalk.api.IBaseMethod;
import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.model.Member;

public abstract class AbstractAuth implements IBaseMethod {
	private static final int Try_Times = 2;
	private String username;
	private String password;
	/** 日志工具 */
	private static final Logger logger = Logger.getLogger(AbstractAuth.class
			.getName());

	public AbstractAuth(String username, String password) {
		this.username = username;
		this.password = password;
		logger.info("UserInfo: (" + username + "," + password + ")");
	}

	private static final String JSON_ERROR_MESSAGE = "{fanTalkError:失败}";
	
	public abstract String getSource();

	public abstract String getSNSName();

	public abstract String getBindOkMessage();

	public abstract String getBindErrorMessage();

	public abstract Member processMember(Member member);

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * 执行无参数的GET请求
	 * 
	 * @param apiUrl
	 *            远程地址
	 * @return 返回HTTP响应
	 */
	public String doGet(String apiUrl) {
		return this.doGet(apiUrl, null);
	}

	/**
	 * 执行GET请求
	 * 
	 * @param apiUrl
	 *            远程地址
	 * @param params
	 *            GET参数
	 * @return 返回HTTP响应
	 */
	public String doGet(String apiUrl, String params) {
		return doGet(apiUrl, params,1);
	}
	
	/**
	 * 执行GET请求
	 * 
	 * @param apiUrl
	 *            远程地址
	 * @param params
	 *            GET参数
	 * @param count
	 * 			方法执行次数
	 * @return 返回HTTP响应
	 */
	private String doGet(String apiUrl, String params, int count) {
		if(count > Try_Times) return JSON_ERROR_MESSAGE;
		try {
			String url = apiUrl;
			if (!Utils.isEmpty(params)) {
				url = apiUrl + "?" + params;

			}
			return getContentFromResponse(signAndSendGetRequest(url));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			String result = doGet(apiUrl, params, ++count);
			if (!JSON_ERROR_MESSAGE.equals(result))
				return result;
			return JSON_ERROR_MESSAGE;
		}
	}


	/**
	 * 执行POST请求
	 * 
	 * @param apiUrl
	 *            远程地址
	 * @param params
	 *            POST参数
	 * @return 返回HTTP响应
	 */
	public String doPost(String apiUrl, String params) {
		return doPost(apiUrl, params, 1);
	}
	
	/**
	 * 执行POST请求
	 * 
	 * @param apiUrl
	 *            远程地址
	 * @param params
	 *            POST参数
	 * @param count
	 * 			方法执行次数
	 * @return 返回HTTP响应
	 */
	private String doPost(String apiUrl, String params, int count) {
		if (count > Try_Times)
			return JSON_ERROR_MESSAGE;
		try {
			return getContentFromResponse(signAndSendPostRequest(apiUrl, params));
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			String result = doPost(apiUrl, params, ++count);
			if (!JSON_ERROR_MESSAGE.equals(result))
				return result;
			return JSON_ERROR_MESSAGE;
		}
	}

	private String getContentFromResponse(Response response) {
		if (response.getCode() == HttpURLConnection.HTTP_OK
				|| response.getCode() == HttpURLConnection.HTTP_CREATED) {
			return response.getBody();
		} else {
			logger.log(Level.WARNING, "返回数据有误 ,code = " + response.getCode()
					+ ",body=" + response.getBody());
			return JSON_ERROR_MESSAGE;
		}
	}

	public abstract OAuthService getOAuthService();

	public OAuthRequest addRequestHeader(OAuthRequest oAuthRequest) {
		return oAuthRequest;
	}
	
	public Response signAndSendGetRequest(String url) {
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, url);
		addRequestHeader(oAuthRequest);
		OAuthService oAuthService = getOAuthService();
		if (oAuthService != null) {
			oAuthService.signRequest(new Token(getUsername(), getPassword()),
					oAuthRequest);
		}
		return oAuthRequest.send();
	}

	public Response signAndSendPostRequest(String url, String param) {
		OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST, url);
		addRequestHeader(oAuthRequest);
		if (!Utils.isEmpty(param)) {
			String[] queryStrs = param.split("&");

			for (String query : queryStrs) {
				String[] split = query.split("=");
				if (split.length == 2) {
					oAuthRequest.addBodyParameter(
							OAuthEncoder.decode(split[0]),
							OAuthEncoder.decode(split[1]));
				} else {
					oAuthRequest.addBodyParameter(
							OAuthEncoder.decode(split[0]), "");
				}
			}
		}

		OAuthService oAuthService = getOAuthService();
		if (oAuthService != null) {
			oAuthService.signRequest(new Token(getUsername(), getPassword()),
					oAuthRequest);
		}
		return oAuthRequest.send();
	}
	
	/**
	 * 判断操作是否成功，若返回的json数据（data）中包含有id字段，则表示成功了，返回true；否则表示失败了，返回false
	 * 
	 * @param data
	 *            json数据
	 * @return 成功就返回True；否则返回false
	 */
	public boolean isActionSuccess(String data) {
		if (!Utils.isEmpty(data)) {
			try {
				logger.info(data);
				JSONObject o = new JSONObject(data);
				if (o.has("id")) {
					return true;
				}
			} catch (JSONException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return false;
	}
    
	/**
	 * 判断操作是否成功，若返回的json数据（data）中包含有id字段，则表示成功了，返回ERROR_OK；否则表示失败了，返回ERROR_FALSE
	 * 
	 * @param data
	 *            json数据
	 * @return 成功就返回ERROR_OK；否则返回ERROR_FALSE
	 */
	public ReturnCode getActionCode(String data) {
		return isActionSuccess(data) ? ReturnCode.ERROR_OK
				: ReturnCode.ERROR_FALSE;
	}
}
