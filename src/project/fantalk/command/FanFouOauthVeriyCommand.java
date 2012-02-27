package project.fantalk.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.oauth.FanFouConstant;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class FanFouOauthVeriyCommand extends BaseCommand {
	private static final Logger logger = Logger
			.getLogger(FanFouOauthVeriyCommand.class.getName());

	public FanFouOauthVeriyCommand() {
		super("oauthVeriy");
	}

	public String documentation() {
		return "-oauthVeriy 参数\n  功能说明：获取返回的oauth_token参数，完成fanfou的oauth验证过程";
	}

	@Override
	public void doCommand(Message message, String argument) {
		String email = message.email;
		JID sender = message.sender;
		String oauth_verifier = argument;
		if (oauth_verifier == null) {
			oauth_verifier = "";
		}
		Member m = Datastore.getInstance().getAndCacheMember(email);
		String token = m.getToken();
		String tokenSecret = m.getTokenSecret();
		if (Utils.isEmpty(token) || Utils.isEmpty(tokenSecret)) {
			XMPPUtils.sendMessage("执行此操作前请先执行-oauthFanfou命令", sender);
			return;
		}
		OAuthConsumer consumer = getConsumer(token, tokenSecret);
		OAuthProvider provider = getProvider();
		try {
			provider.retrieveAccessToken(consumer, oauth_verifier);
			String accessToken = consumer.getToken();
			String accessTokenSecret = consumer.getTokenSecret();
			m.setFanfouUserName(accessToken);
			m.setFanfouPassWord(accessTokenSecret);
			m.setUsername("");
			m.setPassword("");
			m.setToken("");
			m.setTokenSecret("");
			m.put();
			XMPPUtils.sendMessage("绑定fanfou账号成功，可以执行-me命令或直接发送消息查看是否成功绑定",
					sender);
		} catch (Exception e) {
			XMPPUtils.sendMessage("帐号绑定失败!", email);
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public OAuthProvider getProvider() {
		OAuthProvider provider = new DefaultOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		provider.setOAuth10a(true);
		return provider;
	}

	public OAuthConsumer getConsumer(String token, String tokenSecret) {
		OAuthConsumer consumer = new DefaultOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setTokenWithSecret(token, tokenSecret);
		return consumer;
	}

	public String getAccessTokenURL() {
		return FanFouConstant.accessTokenURL;
	}

	public String getApiKey() {
		return FanFouConstant.apiKey;
	}

	public String getApiSecret() {
		return FanFouConstant.secret;
	}

	public String getAuthorizeURL() {
		return FanFouConstant.authorizationURL;
	}

	public String getRequestTokenURL() {
		return FanFouConstant.requestTokenURL;
	}
}
