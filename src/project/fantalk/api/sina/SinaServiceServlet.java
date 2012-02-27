package project.fantalk.api.sina;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthServlet;

public class SinaServiceServlet extends AbstractOauthServlet {

	private static final long serialVersionUID = -24201802919017615L;

	@Override
	public String getAccessTokenURL() {
		return SinaConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return SinaConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return SinaConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return SinaConstant.authorizationURL;
	}

	@Override
	public String getCallBackUrl() {
		return SinaConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return SinaConstant.requestTokenURL;
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
