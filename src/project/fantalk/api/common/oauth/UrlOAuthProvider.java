package project.fantalk.api.common.oauth;

import java.net.HttpURLConnection;
import java.net.URL;

import oauth.signpost.AbstractOAuthProvider;
import oauth.signpost.basic.HttpURLConnectionResponseAdapter;
import oauth.signpost.basic.UrlStringRequestAdapter;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

public class UrlOAuthProvider extends AbstractOAuthProvider {
	private static final long serialVersionUID = 1323232L;
	
	public UrlOAuthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl,
				authorizationWebsiteUrl);
	}

	@Override
	protected HttpRequest createRequest(String url) throws Exception {
		return new UrlStringRequestAdapter(url);
	}

	@Override
	protected HttpResponse sendRequest(HttpRequest request) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) new URL(
				(String) request.unwrap()).openConnection();
		connection.setRequestMethod(request.getMethod());
		connection.connect();
		return new HttpURLConnectionResponseAdapter(connection);
	}

}
