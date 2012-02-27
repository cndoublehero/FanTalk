package project.fantalk.api.sina;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.model.Member;

public class SinaServiceCallBack extends AbstractOauthCallback {
	private static final long serialVersionUID = -242231802919017615L;

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
	public String getRequestTokenURL() {
		return SinaConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setSinaUsername(accessToken);
		m.setSinaPassword(accessTokenSecret);
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
        m.setSinaUsername("");
        m.setSinaPassword("");
        m.put();
        
    }
}
