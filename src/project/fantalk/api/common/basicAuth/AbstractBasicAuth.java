package project.fantalk.api.common.basicAuth;

import org.scribe.model.OAuthRequest;
import org.scribe.oauth.OAuthService;
import com.google.appengine.repackaged.com.google.common.util.Base64;

import project.fantalk.api.common.AbstractAuth;

public abstract class AbstractBasicAuth extends AbstractAuth {
	@Override
	public OAuthRequest addRequestHeader(OAuthRequest oAuthRequest) {
		String authString = getUsername() + ":" + getPassword();
		String basicAuth = Base64.encode(authString.getBytes());
		oAuthRequest.addHeader("Authorization", "Basic " + basicAuth);
		return oAuthRequest;
	}

	@Override
	public OAuthService getOAuthService() {
		return null;
	}

	public AbstractBasicAuth(String username, String password) {
		super(username, password);
	}
}
