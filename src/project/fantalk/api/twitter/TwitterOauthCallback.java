package project.fantalk.api.twitter;

import oauth.signpost.OAuthProvider;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.model.Member;

public class TwitterOauthCallback extends AbstractOauthCallback {
	private static final long serialVersionUID = -5126341471110972209L;

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
	public String getRequestTokenURL() {
		return TwitterConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setTwitterUsername(accessToken);
		m.setTwitterPassword(accessTokenSecret);
		m.put();
		return m;
	}

	@Override
	public OAuthProvider processProvider(OAuthProvider provider) {
		return provider;
	}

    @Override
    public void rollback(Member m) {
        m.setTwitterUsername("");
        m.setTwitterPassword("");
        m.put();
    }

}
