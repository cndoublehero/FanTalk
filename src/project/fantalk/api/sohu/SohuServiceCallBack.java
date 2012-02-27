package project.fantalk.api.sohu;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.model.Member;

public class SohuServiceCallBack extends AbstractOauthCallback {
	private static final long serialVersionUID = -242231802919017615L;

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

	public String getRequestTokenURL() {
		return SohuConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setSohuUsername(accessToken);
		m.setSohuPassword(accessTokenSecret);
		m.put();
		return m;
	}

	@Override
	public OAuthProvider processProvider(OAuthProvider provider) {
		provider.setOAuth10a(true);
		return provider;
	}

	@Override
	public void rollback(Member m) {
		m.setSohuUsername("");
		m.setSohuPassword("");
		m.put();
	}
}
