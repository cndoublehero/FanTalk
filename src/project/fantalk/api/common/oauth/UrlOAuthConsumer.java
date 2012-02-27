package project.fantalk.api.common.oauth;

import java.net.HttpURLConnection;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.basic.UrlStringRequestAdapter;
import oauth.signpost.http.HttpRequest;

public class UrlOAuthConsumer extends AbstractOAuthConsumer {
	private static final long serialVersionUID = 123L;
	
	public UrlOAuthConsumer(String consumerKey, String consumerSecret) {
		super(consumerKey, consumerSecret);
	}

	@Override
	protected HttpRequest wrap(Object request) {

		if (!(request instanceof HttpURLConnection)) {
			throw new IllegalArgumentException(
					"The default consumer expects requests of type java.net.HttpURLConnection");
		}
		return new UrlStringRequestAdapter(((HttpURLConnection) request)
				.getURL().toString());

	}
}
