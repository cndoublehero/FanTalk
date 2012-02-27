package project.fantalk.api.twitter;

import java.util.Properties;

import winterwell.jtwitter.OAuthSignpostClient;
import winterwell.jtwitter.Twitter;

public class Test {
    private String accessToken;
    private String accessTokenSecret;
    private static final String CONSUMER_KEY = "PVl0gidfnem43RPcekihA";
    private static final String CONSUMER_SECRET = "aswT4o5gdAtD7N3IAYnrROYlic4nyWfNoY3EaJTgNq4";

    public Test(String username, String password) {

        Properties props = System.getProperties();
        props.setProperty("proxySet", "true");
        props.setProperty("http.proxyHost", "127.0.0.1");
        props.setProperty("http.proxyPort", "8580");

        this.accessToken = username;
        this.accessTokenSecret = password;
        OAuthSignpostClient client = new OAuthSignpostClient(CONSUMER_KEY,
                CONSUMER_SECRET, accessToken, accessTokenSecret);
        Twitter twitter = new Twitter("mcxiaoke", client);
        twitter.updateStatus("test");

    }

    public boolean update(String text) {
        return false;
    }

    public static void main(String[] args) {
        new Test("14623194-hRSfzbHbxIUdfcziuAefrPCCtax5vUuqM2ZhqJG6t",
                "C3YUGg27dlwRRsNxAgTe96nPq5nMQY3n9WY5isBWc");
    }

}
