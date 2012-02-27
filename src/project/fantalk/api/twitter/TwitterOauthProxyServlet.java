package project.fantalk.api.twitter;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.fantalk.api.Utils;


import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class TwitterOauthProxyServlet extends HttpServlet {
	private static final long serialVersionUID = -7678423147404212090L;
	private static final String AUTHORIZE_URL = "https://twitter.com/oauth/authorize";
	private static final String AUTHORIZE_URL_LOCAL = "/oauth/authorize";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String queryString = req.getQueryString();

		URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest request;
		if (Utils.isEmpty(queryString)) {
			request = new HTTPRequest(new URL(AUTHORIZE_URL), HTTPMethod.GET);
		} else {
			request = new HTTPRequest(
					new URL(AUTHORIZE_URL + "?" + queryString), HTTPMethod.GET);
		}
		try {
			HTTPResponse response = urlFetch.fetch(request);
			String result = new String(response.getContent(), "UTF-8");
			result = result.replaceAll(AUTHORIZE_URL, AUTHORIZE_URL_LOCAL);
			resp.getOutputStream().write(result.getBytes("UTF-8"));
			for (HTTPHeader h : response.getHeaders()) {
				if (h.getName().equalsIgnoreCase("Set-Cookie")) {
					resp.setHeader("Set-Cookie", h.getValue().replaceAll(
							".twitter.com", req.getServerName()));
				} else if (!h.getName().equalsIgnoreCase("Content-length")) {
					resp.setHeader(h.getName(), h.getValue());
				}
			}
			if (response.getResponseCode() != 200)
				resp.sendError(response.getResponseCode());
		} catch (IOException e) {
			resp
					.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
							"Twitter is temporarily unavailable, Please try again later.");
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String queryString = req.getQueryString();
		URLFetchService urlFetch = URLFetchServiceFactory.getURLFetchService();
		HTTPRequest request;
		if (Utils.isEmpty(queryString)) {
			request = new HTTPRequest(new URL(AUTHORIZE_URL), HTTPMethod.POST);
		} else {
			request = new HTTPRequest(
					new URL(AUTHORIZE_URL + "?" + queryString), HTTPMethod.POST);
		}
		byte[] postpayload = new byte[req.getContentLength()];
		req.getInputStream().read(postpayload);
		request.setPayload(postpayload);
		try {
			HTTPResponse httpresp = urlFetch.fetch(request);
			String result = new String(httpresp.getContent(), "UTF-8");
			result = result.replaceAll(AUTHORIZE_URL, AUTHORIZE_URL_LOCAL);
			resp.getOutputStream().write(result.getBytes("UTF-8"));
			for (HTTPHeader h : httpresp.getHeaders()) {
				if (h.getName().equalsIgnoreCase("Set-Cookie")) {
					resp.setHeader("Set-Cookie", h.getValue().replaceAll(
							".twitter.com", req.getServerName()));
				} else if (!h.getName().equalsIgnoreCase("Content-length")) {
					resp.setHeader(h.getName(), h.getValue());
				}
			}
			if (httpresp.getResponseCode() != 200)
				resp.sendError(httpresp.getResponseCode());
		} catch (IOException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

}
