package project.fantalk.api.renren;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.common.AbstractAuth;
import project.fantalk.api.common.oauth.PostParameter;
import project.fantalk.model.Member;

import com.google.appengine.api.urlfetch.HTTPRequest;


public class RenRenServiceOauth2 extends AbstractAuth {
	private static final Logger logger = Logger
			.getLogger(RenRenServiceOauth2.class.getName());

	private String code;

	public RenRenServiceOauth2(String username, String password) {
		super(username, password);
	}

	public RenRenServiceOauth2(Member member) {
		super(member.getRenrenUsername(), member.getRenrenPassword());
	}

	public RenRenServiceOauth2(Member member, String code) {
		super(member.getRenrenUsername(), member.getRenrenPassword());
		this.code = code;
	}

	@Override
	public String getBindErrorMessage() {
		return null;
	}

	@Override
	public String getBindOkMessage() {
		return null;
	}

	@Override
	public String getSNSName() {
		return "人人网";
	}

	@Override
	public String getSource() {
		return null;
	}

	@Override
	public HTTPRequest processRequeset(HTTPRequest request, String params) {
		return request;
	}

	@Override
	public void processRequestParams(HTTPRequest request, String params) {
	}

	@Override
	public Member processMember(Member member) {

		String sessionId = null;
		String userId = null;
		try {
			String url = RenRenConstant.sessionKeyUrl;
			String params = "oauth_token=" + Utils.encode(getAccessToken());
			String data = doGet(url, params);
			JSONObject o = new JSONObject(data);
			logger.log(Level.INFO, "json === " + o);

			JSONObject temp = o.getJSONObject("renren_token");
			logger.log(Level.INFO, "json Temp renren_token=== " + o);
			if (temp != null) {
				sessionId = temp.getString("session_key");
			}
			temp = o.getJSONObject("user");
			logger.log(Level.INFO, "json Temp user=== " + temp);
			if (temp != null) {
				userId = temp.getString("id");
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, "获取人人网session_key时出现了异常", e);
			throw new RuntimeException(e);
		}
		member.setRenrenUsername(userId);//renrenUsername保存的用户的id
		member.setRenrenPassword(sessionId);//renrenPassword保存的使用的sessionId
		return member;
	}

	private String getAccessToken() {
		String accessToken = null;
		try {
			String params = "client_id=" + RenRenConstant.apiKey
					+ "&client_secret=" + RenRenConstant.secret
					+ "&redirect_uri=" + RenRenConstant.callBackURL
					+ "&&grant_type=authorization_code&code=" + code;
			logger.log(Level.INFO, "params === " + params);
			String data = doGet(RenRenConstant.tokenURL, params);
			JSONObject o = new JSONObject(data);
			logger.log(Level.INFO, "json === " + o);
			accessToken = o.getString("access_token");
			if (!Utils.isEmpty(accessToken)) {
				return accessToken;
			}
		} catch (JSONException e) {
			logger.log(Level.WARNING, "获取人人网accessToken时出现了异常", e);
			throw new RuntimeException(e);
		}
		return accessToken;
	}

	public ReturnCode update(String text) {
		String sessionId = getPassword();
		if (!Utils.isEmpty(sessionId)) {
			String url = getUpdateParameters(text, sessionId);

			String data = doPost(RenRenConstant.apiUrl, url);
			JSONObject o;
			try {
				o = new JSONObject(data);
				logger.log(Level.INFO, "json === " + o);
				String result = o.getString("result");
				if (!Utils.isEmpty(result)) {
					return ReturnCode.ERROR_OK;
				}
			} catch (JSONException e) {
				logger.log(Level.WARNING, "更新人人网新鲜事时出现了异常", e);
			}
		}
		return ReturnCode.ERROR_FALSE;
	}

	private String getUpdateParameters(String text, String sessionId) {
		PostParameter a1 = new PostParameter("v", "1.0");
		PostParameter a2 = new PostParameter("api_key", RenRenConstant.apiKey);
		PostParameter a3 = new PostParameter("method", "notifications.send");
		PostParameter a4 = new PostParameter("call_id", System.nanoTime());
		PostParameter a5 = new PostParameter("session_key", Utils
				.encode(sessionId));
		PostParameter a6 = new PostParameter("to_ids", getUsername());
		PostParameter a7 = new PostParameter("notification", text);
		PostParameter a8 = new PostParameter("format", Utils.encode("JSON"));
		RenRenPostParameters ps = new RenRenPostParameters(
				RenRenConstant.secret);
		ps.addParameter(a1);
		ps.addParameter(a2);
		ps.addParameter(a3);
		ps.addParameter(a4);
		ps.addParameter(a5);
		ps.addParameter(a6);
		ps.addParameter(a7);
		ps.addParameter(a8);
		return ps.generateUrl();
	}

	public ReturnCode verify() {
		return ReturnCode.ERROR_OK;
	}

}
