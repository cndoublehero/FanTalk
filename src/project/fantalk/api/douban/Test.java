package project.fantalk.api.douban;

import java.util.Scanner;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.QueryStringSigningStrategy;
import project.fantalk.api.common.oauth.OAuth;
import project.fantalk.api.common.oauth.OAuthToken;
import project.fantalk.api.common.oauth.UrlOAuthConsumer;
import project.fantalk.api.common.oauth.UrlOAuthProvider;

public class Test {

	public static String getAccessTokenURL() {
		return DoubanConstant.accessTokenURL;
	}

	public static String getApiKey() {
		return DoubanConstant.apiKey;
	}

	public static String getApiSecret() {
		return DoubanConstant.secret;
	}

	public static String getAuthorizeURL() {
		return DoubanConstant.authorizationURL;
	}

	public static String getRequestTokenURL() {
		return DoubanConstant.requestTokenURL;
	}

	public static void main(String[] args) throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {
//		 test1();
		test2();
	}

	private static void test1() {
		System.setProperty("debug", "debug");
		UrlOAuthConsumer consumer = new UrlOAuthConsumer(getApiKey(),
				getApiSecret());
		consumer.setSigningStrategy(new QueryStringSigningStrategy());
		UrlOAuthProvider provider = new UrlOAuthProvider(getRequestTokenURL(),
				getAccessTokenURL(), getAuthorizeURL());
		provider.setOAuth10a(true);
		try {
			String authURL = provider.retrieveRequestToken(consumer,
					oauth.signpost.OAuth.OUT_OF_BAND);
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
			providerconsumerTemp.retrieveAccessToken(consumerTemp, verify
					.trim());
			String accessToken = consumerTemp.getToken();
			String accessTokenSecret = consumerTemp.getTokenSecret();
			System.out.println(accessToken);
			System.out.println(accessTokenSecret);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void test2() throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException {
		OAuth oauth = new OAuth(getApiKey(), getApiSecret());
		long timestamp = System.currentTimeMillis() / 1000;
		long nonce = System.nanoTime();
		String url = "http://api.douban.com/people/%40me?apikey=08bbd5621f14b9281a3380265d3e2b78&alt=json";
		String authorization = null;

		authorization = oauth.generateAuthorizationHeader("GET", url, null, String
				.valueOf(nonce), String.valueOf(timestamp), new OAuthToken(
				"20d6e7b3bf961f186284b71b3ec25d48", "05ca97dc0f6e8abe"));
		if (url.indexOf("?") != -1) {
			authorization = "&" + authorization;
		} else {
			authorization = "?" + authorization;
		}
		System.out.println("32332=" + authorization);
		// authorization = authorization.replace("\"", "");
		// authorization = authorization.replace(",", "&");
		System.out.println("test====" + authorization);
		System.out.println("test====" + url + authorization);
	}
}
