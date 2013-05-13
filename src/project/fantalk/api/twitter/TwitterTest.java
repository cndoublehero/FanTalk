package project.fantalk.api.twitter;

import java.util.Scanner;

import oauth.signpost.OAuth;
import oauth.signpost.signature.QueryStringSigningStrategy;
import project.fantalk.api.common.oauth.UrlOAuthConsumer;
import project.fantalk.api.common.oauth.UrlOAuthProvider;

public class TwitterTest {

	public static String getAccessTokenURL() {
		return TwitterConstant.accessTokenURL;
	}

	public static String getApiKey() {
		return TwitterConstant.apiKey;
	}

	public static String getApiSecret() {
		return TwitterConstant.secret;
	}

	public static String getAuthorizeURL() {
		return TwitterConstant.authorizationURL;
	}

	public static String getRequestTokenURL() {
		return TwitterConstant.requestTokenURL;
	}

	public static void main(String[] args) {
		//test1();
		test2();
	}

	private static void test1() {
		System.setProperty("debug", "debug");
		UrlOAuthConsumer consumer = new UrlOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		UrlOAuthProvider provider = new UrlOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		// provider.setOAuth10a(true);
		try {
			String authURL = provider.retrieveRequestToken(consumer,
					"http://cndoubleherodfas.com.cn/callback/twitter");
			System.out.println(authURL);
			System.out.println("Input your verification code：");
			Scanner in = new Scanner(System.in);
			String verify = in.nextLine();

			System.out.println("GetAccessToken......");
			provider.retrieveAccessToken(consumer, verify.trim());
			String accessToken = consumer.getToken();
			String accessTokenSecret = consumer.getTokenSecret();
			System.out.println(accessToken);
			System.out.println(accessTokenSecret);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void test2() {
		System.setProperty("debug", "debug");
		UrlOAuthConsumer consumer = new UrlOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		UrlOAuthProvider provider = new UrlOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		provider.setOAuth10a(true);
		try {
			String authURL = provider.retrieveRequestToken(consumer,
					OAuth.OUT_OF_BAND);
			System.out.println(authURL);
			System.out.println("Input your verification code：");
			Scanner in = new Scanner(System.in);
			String verify = in.nextLine();

			System.out.println("GetAccessToken......");

			String accessTokenTemp = consumer.getToken();
			String accessTokenSecretTemp = consumer.getTokenSecret();
			UrlOAuthConsumer consumerTemp = new UrlOAuthConsumer(getApiKey(),
					getApiSecret());
			consumerTemp.setTokenWithSecret(accessTokenTemp,
					accessTokenSecretTemp);
			consumerTemp.setSigningStrategy(new QueryStringSigningStrategy());

			UrlOAuthProvider providerconsumerTemp = new UrlOAuthProvider(
					getRequestTokenURL(), getAccessTokenURL(),
					getAuthorizeURL());
			providerconsumerTemp.setOAuth10a(true);
			providerconsumerTemp.retrieveAccessToken(consumerTemp,
					verify.trim());
			String accessToken = consumerTemp.getToken();
			String accessTokenSecret = consumerTemp.getTokenSecret();
			System.out.println(accessToken);
			System.out.println(accessTokenSecret);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
