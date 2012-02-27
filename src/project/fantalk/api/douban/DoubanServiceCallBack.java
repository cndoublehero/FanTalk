package project.fantalk.api.douban;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.signature.QueryStringSigningStrategy;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.api.common.oauth.UrlOAuthConsumer;
import project.fantalk.api.common.oauth.UrlOAuthProvider;
import project.fantalk.model.Member;

public class DoubanServiceCallBack extends AbstractOauthCallback{
	private static final long serialVersionUID = -242231802229017615L;


	@Override
	public String getAccessTokenURL() {
		return DoubanConstant.accessTokenURL;
	}

	@Override
	public String getApiKey() {
		return DoubanConstant.apiKey;
	}

	@Override
	public String getApiSecret() {
		return DoubanConstant.secret;
	}

	@Override
	public String getAuthorizeURL() {
		return DoubanConstant.authorizationURL;
	}

	@Override
	public String getRequestTokenURL() {
		return DoubanConstant.requestTokenURL;
	}

	@Override
	public Member processToken(String accessToken, String accessTokenSecret,
			Member m) {
		m.setDoubanUsername(accessToken);
		m.setDoubanPassword(accessTokenSecret);
		m.put();
		return m;
	}

	@Override
	public OAuthConsumer getConsumer(String token, String tokenSecret) {
		UrlOAuthConsumer consumer = new UrlOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		consumer.setTokenWithSecret(token, tokenSecret);
		return consumer;
	}

	@Override
	public OAuthProvider getProvider() {
		UrlOAuthProvider provider = new UrlOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		return provider;
	}
	
	@Override
	public OAuthProvider processProvider(OAuthProvider provider) {
		provider.setOAuth10a(true);
		return provider;
	}

    @Override
    public void rollback(Member m) {
        m.setDoubanUsername("");
        m.setDoubanPassword("");
        m.put();
        
    }
}
