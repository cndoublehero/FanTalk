package project.fantalk.api.fanfou.oauth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FanFouApi;
import org.scribe.model.OAuthRequest;
import org.scribe.oauth.OAuthService;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Member;

public class FanFouServiceOAuth extends FanfouService {

	private static final OAuthService FanFouOAuthService = new ServiceBuilder()
			.provider(FanFouApi.class).apiKey(FanFouConstant.apiKey)
			.apiSecret(FanFouConstant.secret).build();

	public FanFouServiceOAuth(Member member) {
		super(member.getFanfouUserName(), member.getFanfouPassWord());
	}
	
	public FanFouServiceOAuth(String username, String password) {
    	super(username, password);
    }

	public String getApiKey() {
		return FanFouConstant.apiKey;
	}

	public String getApiSecret() {
		return FanFouConstant.secret;
	}

	@Override
	public OAuthService getOAuthService() {
		return FanFouOAuthService;
	}
	
	@Override
	public OAuthRequest addRequestHeader(OAuthRequest oAuthRequest) {
		return oAuthRequest;
	}
}
