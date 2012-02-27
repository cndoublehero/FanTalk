package project.fantalk.api.fanfou.oauth;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.model.Member;

public class FanFouServiceCallBack extends AbstractOauthCallback {
	private static final long serialVersionUID = -24223100919017615L;

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
	public String getRequestTokenURL() {
		return FanFouConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setFanfouUserName(accessToken);
		m.setFanfouPassWord(accessTokenSecret);
		m.setUsername("");
		m.setPassword("");
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
		m.setFanfouUserName("");
		m.setFanfouPassWord("");
		m.put();

	}
}
