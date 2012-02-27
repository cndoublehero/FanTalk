package project.fantalk.api.sohu;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthServlet;
public class SohuServiceServlet extends AbstractOauthServlet {
	private static final long serialVersionUID = -242018029017615L;

	@Override
	public String getAccessTokenURL() {
		return SohuConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return SohuConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return SohuConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return SohuConstant.authorizationURL;
	}

	@Override
	public String getCallBackUrl() {
		return SohuConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return SohuConstant.requestTokenURL;
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
