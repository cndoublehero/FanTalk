package project.fantalk.api.common;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import project.fantalk.api.IBaseMethod;
import project.fantalk.api.Utils;
import project.fantalk.model.Member;


import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public abstract class AbstractAuth implements IBaseMethod {
	private static final int Try_Times = 2;
	private String username;
	private String password;
	/** 网址抓取服务 */
	private static final URLFetchService fetchService = URLFetchServiceFactory
			.getURLFetchService();
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
			HTTPResponse response = fetchService.fetch(this.getRequest(url,
					HTTPMethod.GET, null));
			
			return getContentFromResponse(response);
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
		if(count > Try_Times) return JSON_ERROR_MESSAGE;
		try {
			HTTPResponse response = fetchService.fetch(this.getRequest(apiUrl,
					HTTPMethod.POST, params));
			return getContentFromResponse(response);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			String result = doPost(apiUrl, params, ++count);
			if (!JSON_ERROR_MESSAGE.equals(result))
				return result;
			return JSON_ERROR_MESSAGE;
		}
	}

	private String getContentFromResponse(HTTPResponse response)
			throws UnsupportedEncodingException {
		
		if (response.getResponseCode() == HttpURLConnection.HTTP_OK
				|| response.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
			return new String(response.getContent(), "UTF-8");
		} else {
			logger.log(Level.WARNING, "返回数据有误 ,code = "
					+ response.getResponseCode() + ",ss="
					+ new String(response.getContent(), "UTF-8"));
			return JSON_ERROR_MESSAGE;
		}
	}

	/**
	 * 获取对应的HTTP请求
	 * 
	 * @param apiUrl
	 *            远程服务器地址
	 * @param method
	 *            请求方法：GET或POST
	 * @param params
	 *            请求参数：GET的参数添加到URL尾部，POST的参数使用setPayload();
	 * @return 返回设置好的HTTPRequest
	 */
	public HTTPRequest getRequest(String apiUrl, HTTPMethod method,
			String params) {
		HTTPRequest request = null;
		try {
			URL url = new URL(apiUrl);
			request = new HTTPRequest(url, method);
			StringBuilder sb = new StringBuilder();
			
			if (method == HTTPMethod.POST) {
				processRequestParams(request, params);
				request.setPayload(params.getBytes("UTF-8"));
				sb.append("Params: ").append(params);
			}
			request = processRequeset(request,params);
			
			sb.append("HTTP Request Content:\n").append("Method:").append(
					request.getMethod().toString()).append("\n").append("URL:")
					.append(request.getURL()).append("\n");
			logger.info(sb.toString());
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			throw new RuntimeException("创建request失败 ", e);
		}
		return request;
	}
	
	public abstract HTTPRequest processRequeset(HTTPRequest request, String params);
	
	public abstract void processRequestParams(HTTPRequest request, String params);
}
