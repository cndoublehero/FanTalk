package project.fantalk.api.nets;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.model.Member;

public class NetsServiceCallBack extends AbstractOauthCallback{
	private static final long serialVersionUID = -242231802919017615L;

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
	public String getRequestTokenURL() {
		return NetsConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setNeteaseUsername(accessToken);
		m.setNeteasePassword(accessTokenSecret);
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
        m.setNeteaseUsername("");
        m.setNeteasePassword("");
        m.put();
        
    }
}
