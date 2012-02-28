package project.fantalk.api.fanfou.oauth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FanFouApi;
import org.scribe.model.OAuthRequest;
import org.scribe.oauth.OAuthService;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Member;

public class FanFouServiceOAuth extends FanfouService {

	public FanFouServiceOAuth(Member member) {
		super(member.getFanfouUserName(), member.getFanfouPassWord());
	}

	public String getApiKey() {
		return FanFouConstant.apiKey;
	}

	public String getApiSecret() {
		return FanFouConstant.secret;
	}

	@Override
	public OAuthService getOAuthService() {
		return new ServiceBuilder().provider(FanFouApi.class)
				.apiKey(FanFouConstant.apiKey).apiSecret(FanFouConstant.secret)
				.build();
	}
	
	@Override
	public OAuthRequest addRequestHeader(OAuthRequest oAuthRequest) {
		return oAuthRequest;
	}
}
