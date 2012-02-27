package project.fantalk.api.twitter;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthServlet;

public class TwitterOauthServlet extends AbstractOauthServlet {
	private static final long serialVersionUID = -2420180291903617615L;

	@Override
	public String getAccessTokenURL() {
		return TwitterConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return TwitterConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return TwitterConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return TwitterConstant.authorizationURL;
	}

	@Override
	public String getCallBackUrl() {
		return TwitterConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return TwitterConstant.requestTokenURL;
	}

	@Override
	public String processCallBackUrl(String callBackUrl) {
		return callBackUrl.replaceFirst(
				"http(s?)://twitter.com/oauth/authorize", "/oauth/authorize");
	}

	@Override
	public OAuthProvider processProvider(OAuthProvider provider) {
		return provider;
	}

}
