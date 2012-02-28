package project.fantalk.api.common.basicAuth;

import org.scribe.model.OAuthRequest;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

import project.fantalk.api.common.AbstractAuth;

public abstract class AbstractBasicAuth extends AbstractAuth {
	@Override
	public OAuthRequest addRequestHeader(OAuthRequest oAuthRequest) {
		String authString = getUsername() + ":" + getPassword();
		String basicAuth = OAuthEncoder.encode(authString);
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
