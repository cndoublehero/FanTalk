package project.fantalk.api.fanfou.oauth;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;

import project.fantalk.api.common.oauth.OAuth;
import project.fantalk.api.common.oauth.OAuthToken;
import project.fantalk.api.common.oauth.PostParameter;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Member;

public class FanFouServiceOAuth extends FanfouService {

	public FanFouServiceOAuth(Member member) {
		super(member.getFanfouUserName(), member.getFanfouPassWord());
	}

	public String getApiKey() {
		return FanFouConstant.apiKey;
	}

	public String getApiSecret() {
		return FanFouConstant.secret;
	}

	@Override
	public HTTPRequest processRequeset(HTTPRequest request, String params) {
		OAuth oauth = new OAuth(getApiKey(), getApiSecret());
		long timestamp = System.currentTimeMillis() / 1000;
		long nonce = System.nanoTime();
		String url = request.getURL().toString();

		String authorization = null;

		authorization = oauth.generateAuthorizationHeader(request.getMethod()
				.toString(), url, PostParameter.parseGetParameters(params),
				String.valueOf(nonce), String.valueOf(timestamp),
				new OAuthToken(getUsername(), getPassword()));
		request.addHeader(new HTTPHeader("Authorization", authorization));
		return request;
	}

}
