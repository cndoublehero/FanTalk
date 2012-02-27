package project.fantalk.api.nets;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthServlet;

public class NetsServiceServlet extends AbstractOauthServlet {
	private static final long serialVersionUID = -242018029017615L;

	@Override
	public String getAccessTokenURL() {
		return NetsConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return NetsConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return NetsConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return NetsConstant.authorizationURL;
	}

	@Override
	public String getCallBackUrl() {
		return NetsConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return NetsConstant.requestTokenURL;
	}

	@Override
	public String processCallBackUrl(String callBackUrl) {
		return callBackUrl;
	}

	@Override
	public OAuthProvider processProvider(OAuthProvider provider) {
		provider.setOAuth10a(true);
		return provider;
	}
}
