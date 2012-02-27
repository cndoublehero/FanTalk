package project.fantalk.api.douban;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.signature.QueryStringSigningStrategy;
import project.fantalk.api.common.oauth.AbstractOauthServlet;
import project.fantalk.api.common.oauth.UrlOAuthConsumer;
import project.fantalk.api.common.oauth.UrlOAuthProvider;

public class DoubanServiceServlet extends AbstractOauthServlet {
	private static final long serialVersionUID = -242018029017615L;

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
	public String getCallBackUrl() {
		return DoubanConstant.callBackURL;
	}

	@Override
	public String getRequestTokenURL() {
		return DoubanConstant.requestTokenURL;
	}

	@Override
	public String processCallBackUrl(String callBackUrl) {
		return callBackUrl;
	}

	@Override
	public OAuthConsumer getConsumer() {
		UrlOAuthConsumer consumer = new UrlOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
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
}
