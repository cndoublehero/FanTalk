package project.fantalk.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.oauth.FanFouConstant;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class FanfouOAuthURLCommand extends BaseCommand {
	private static final Logger logger = Logger
			.getLogger(FanfouOAuthURLCommand.class.getName());

	private final static String BASE_URL = "http://" + Utils.getAppID()
			+ ".appspot.com";

	public FanfouOAuthURLCommand() {
		super("oauthFanfou");
	}

	@Override
	public void doCommand(Message message, String argument) {
		String email = message.email;
		OAuthConsumer consumer = getConsumer();
		OAuthProvider provider = getProvider();
		try {
			String callbackUrl = BASE_URL + getCallBackUrl();
			String authURL = provider.retrieveRequestToken(consumer,
					callbackUrl);
			logger.info("authURL: " + authURL);
			Member m = Datastore.getInstance().getAndCacheMember(email);
			m.setToken(consumer.getToken());
			m.setTokenSecret(consumer.getTokenSecret());
			m.put();
			XMPPUtils
					.sendMessage(
							"authURL地址为"
									+ authURL
									+ "，请复制此url后在浏览器访问，经过fanfou验证后把浏览器地址栏中的oauth_token参数拷贝下来，再使用“-oauthVeriy 参数”完成整个验证过程",
							email);
		} catch (Exception e) {
			XMPPUtils.sendMessage("命令执行时出错了，", email);
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public String documentation() {
		StringBuilder sb = new StringBuilder();
		sb.append("-oauthFanfou\n获取fanfou oauth绑定时的authURL，复制此url后在浏览器访问，经过fanfou验证后把浏览器地址栏中的oauth_token参数拷贝下来，再使用“-oauthVeriy 参数”完成整个验证过程");
		return sb.toString();
	}

	public OAuthProvider getProvider() {
		OAuthProvider provider = new DefaultOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		provider.setOAuth10a(true);
		return provider;
	}

	public OAuthConsumer getConsumer() {
		OAuthConsumer consumer = new DefaultOAuthConsumer(getApiKey(),
				getApiSecret());
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

	public String getCallBackUrl() {
		return FanFouConstant.callBackURL;
	}

	public String getRequestTokenURL() {
		return FanFouConstant.requestTokenURL;
	}
}