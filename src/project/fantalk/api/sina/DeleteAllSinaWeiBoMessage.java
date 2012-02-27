package project.fantalk.api.sina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;

public class DeleteAllSinaWeiBoMessage {
	private static final String SECRET = "1239b560c817c8c8fde18a2efcb71491";
	private static final String TOKEN = "f916d603d790556c516402202ad1dbc2";

	public static void main(String[] args) throws Exception {
		System.setProperty("debug", "debug");
		List<String> messageIdList = getTimeLine();
		while (!messageIdList.isEmpty()) {
			for (String id : messageIdList) {
				deleteMessageById(id);
			}
			messageIdList = getTimeLine();
		}
	}

	private static void deleteMessageById(String id)
			throws MalformedURLException, IOException, ProtocolException,
			OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(SinaConstant.apiKey,
				SinaConstant.secret);
		consumer.setTokenWithSecret(TOKEN, SECRET);
		URL url = new URL("http://api.t.sina.com.cn/statuses/destroy/" + id
				+ ".json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		HttpParameters para = new HttpParameters();
		consumer.setAdditionalParameters(para);
		consumer.sign(request);
		System.out.println("Sending request...");
		request.connect();
		System.out.println("Response: " + request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String b = null;
		while ((b = reader.readLine()) != null) {
			sb.append(b);
		}
		reader.close();
	}

	private static List<String> getTimeLine() throws MalformedURLException,
			IOException, ProtocolException, OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			JSONException {
		OAuthConsumer consumer = new DefaultOAuthConsumer(SinaConstant.apiKey,
				SinaConstant.secret);
		consumer.setTokenWithSecret(TOKEN, SECRET);
		URL url = new URL(
				"http://api.t.sina.com.cn/statuses/user_timeline.json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setRequestMethod("GET");
		consumer.sign(request);
		System.out.println("Sending request...");
		request.connect();
		System.out.println("Response: " + request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuffer sb = new StringBuffer();
		String b = null;
		while ((b = reader.readLine()) != null) {
			sb.append(b);
		}
		reader.close();
		List<String> messageIdList = new ArrayList<String>();
		JSONArray a = new JSONArray(sb.toString());
		for (int i = 0; i < a.length(); i++) {
			JSONObject o = a.getJSONObject(i);
			messageIdList.add(o.getString("id"));
		}
		return messageIdList;
	}
}
