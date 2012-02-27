package project.fantalk.api.fanfou.oauth;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthServlet;

public class FanFouServiceServlet extends AbstractOauthServlet {

	private static final long serialVersionUID = -24201802919000615L;

	@Override
	public String getAccessTokenURL() {
		return FanFouConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return FanFouConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return FanFouConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return FanFouConstant.authorizationURL;
	}

	@Override
	public String getCallBackUrl() {
		return FanFouConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return FanFouConstant.requestTokenURL;
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
