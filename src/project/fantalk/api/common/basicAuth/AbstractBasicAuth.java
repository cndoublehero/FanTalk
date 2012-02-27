package project.fantalk.api.common.basicAuth;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;


import project.fantalk.api.common.AbstractAuth;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.repackaged.com.google.common.util.Base64;

public abstract class AbstractBasicAuth extends AbstractAuth {
    private static final Logger logger = Logger.getLogger(AbstractBasicAuth.class
            .getName());
	@Override
	public HTTPRequest processRequeset(HTTPRequest request,String params) {
		String authString=getUsername() + ":" + getPassword();
		logger.info("Username and Password:"+authString);
		String basicAuth;
		try {
			basicAuth = Base64.encode(authString.getBytes("UTF-8"));
			request
			.addHeader(new HTTPHeader("Authorization", "Basic " + basicAuth));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return request;
	}

	@Override
	public void processRequestParams(HTTPRequest request, String params) {

	}

	public AbstractBasicAuth(String username, String password) {
		super(username, password);
	}
}
