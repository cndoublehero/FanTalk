package project.fantalk.api.sohu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.http.HttpParameters;

public class Test {
//	public static void main(String[] args) throws Exception {
//		System.setProperty("debug", "debug");
//		OAuthConsumer consumer = new DefaultOAuthConsumer(
//				"YmRF4HDvikvwDxYafsaK",
//				"r)q7L!4X$j$nTS0lXAjC=al9Xf*cLOdyFJsy%2OE");
//		consumer.setTokenWithSecret("5722da60fee79ef9efc2d383f871d550",
//				"39142f31ad8a7e6ff7b87f36cc9e8f10");
//		URL url = new URL("http://api.t.sohu.com/users/show.json");
//		HttpURLConnection request = (HttpURLConnection) url.openConnection();
//		request.setRequestMethod("GET");
//		consumer.sign(request);
//		System.out.println("Sending request...");
//		request.connect();
//		System.out.println("Response: " + request.getResponseCode() + " "
//				+ request.getResponseMessage());
//		BufferedReader reader = new BufferedReader(new InputStreamReader(
//				request.getInputStream()));
//		String b = null;
//		while ((b = reader.readLine()) != null) {
//			System.out.println(b);
//		}
//	}

	
public static void main(String[] args) throws Exception {

		System.setProperty("debug", "debug");
		OAuthConsumer consumer = new DefaultOAuthConsumer(
				"YmRF4HDvikvwDxYafsaK",
				"r)q7L!4X$j$nTS0lXAjC=al9Xf*cLOdyFJsy%2OE");
		consumer.setTokenWithSecret("5722da60fee79ef9efc2d383f871d550",
				"39142f31ad8a7e6ff7b87f36cc9e8f10");
		URL url = new URL("http://api.t.sohu.com/statuses/update.json");
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);
		request.setRequestMethod("POST");
		HttpParameters para = new HttpParameters();
		para.put("status", URLEncoder.encode("中 文", "utf-8").replaceAll("\\+",
				"%20"));
		consumer.setAdditionalParameters(para);
		consumer.sign(request);
		OutputStream ot = request.getOutputStream();
		ot.write(("status=" + URLEncoder.encode("1中 文", "utf-8")).replaceAll(
				"\\+", "%20").getBytes());
		ot.flush();
		ot.close();
		System.out.println("Sending request...");
		request.connect();
		System.out.println("Response: " + request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String b = null;
		while ((b = reader.readLine()) != null) {
			System.out.println(b);
		}
	}
}
