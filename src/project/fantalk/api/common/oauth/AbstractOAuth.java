package project.fantalk.api.common.oauth;

import project.fantalk.api.common.AbstractAuth;
import project.fantalk.model.Member;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;


public abstract class AbstractOAuth extends AbstractAuth {

	@Override
	public String getBindErrorMessage() {
		return null;
	}

	@Override
	public String getBindOkMessage() {
		return null;
	}

	@Override
	public Member processMember(Member member) {
		return member;
	}

	public abstract String getApiKey();

	public abstract String getApiSecret();

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

	@Override
	public void processRequestParams(HTTPRequest request, String params) {
	}

	public AbstractOAuth(String username, String password) {
		super(username, password);
	}
}
