package project.fantalk.api.tencent;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.signature.QueryStringSigningStrategy;
import project.fantalk.api.common.oauth.AbstractOauthCallback;
import project.fantalk.api.common.oauth.UrlOAuthConsumer;
import project.fantalk.api.common.oauth.UrlOAuthProvider;
import project.fantalk.model.Member;



public class TencentServiceCallBack extends AbstractOauthCallback {
    private static final long serialVersionUID = -242231802919017615L;
    @Override
    public String getAccessTokenURL() {
        return TencentConstant.accessTokenURL;
    }

    @Override
    public String getApiKey() {
        return TencentConstant.apiKey;
    }

    @Override
    public String getApiSecret() {
        return TencentConstant.secret;
    }

    @Override
    public String getAuthorizeURL() {
        return TencentConstant.authorizationURL;
    }

    public String getRequestTokenURL() {
        return TencentConstant.requestTokenURL;
    }

    @Override
    public Member processToken(String accessToken, String accessTokenSecret,
            Member m) {
        m.setTencentUsername(accessToken);
        m.setTecentPassword(accessTokenSecret);
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
        m.setTencentUsername("");
        m.setTecentPassword("");
        m.put();
    }
}
