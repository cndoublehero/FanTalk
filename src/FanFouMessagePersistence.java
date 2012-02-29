import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.repackaged.com.google.common.util.Base64;

import project.fantalk.api.fanfou.Parser;
import project.fantalk.api.fanfou.domain.Status;

public class FanFouMessagePersistence {
	private final static String UserName = "cndoublehero@gmail.com";
	private final static String PassWord = "";

	private String getUsername() {
		return UserName;
	}

	private String getPassword() {
		return PassWord;
	}

	public static void main(String[] args) throws IOException {
		FanFouMessagePersistence fouFouMessageService = new FanFouMessagePersistence();
		boolean tag = true;
		int pageNo = 1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		while (tag) {
			String str = fouFouMessageService.getFanFouMessage(pageNo++);
			if (str == null || "".equals(str) || "[]".equals(str)) {
				tag = false;
			} else {
				List<Status> statusList = Parser.parseTimeline(str);
				for (Status status : statusList) {
					String message = "\n"
							+ dateFormat.format(status.getCreatedAt()) + "    "
							+ status.getText() + "   By " + status.getSource()
							+ "\n";
					sb.append(message);
				}
			}
		}
		fouFouMessageService.saveFanFouMessage(sb.toString(), null);
	}

	private void saveFanFouMessage(String str, String fileName)
			throws IOException {
		if (fileName == null || "".equals(fileName)) {
			fileName = getFileName() + ".txt";
		}
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fileOutPutStream = new FileOutputStream(file);
		BufferedOutputStream bfOutPutStream = new BufferedOutputStream(
				fileOutPutStream);
		bfOutPutStream.write(str.getBytes());
		bfOutPutStream.flush();
		System.out.println(file.getAbsolutePath());
	}

	private String getFileName() {
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date.getTime());
	}

	private String getFanFouMessage(int pageNo) throws IOException {
		URL url = new URL(
				"http://api.fanfou.com/statuses/user_timeline.json?page="
						+ pageNo);
		HttpURLConnection request = (HttpURLConnection) url.openConnection();
		request.setDoOutput(true);
		request.setRequestMethod("GET");
		String basicAuth = Base64.encode((getUsername() + ":" + getPassword())
				.getBytes());
		request.addRequestProperty("Authorization", "Basic " + basicAuth);
		System.out.println("Sending request...");

		request.connect();
		System.out.println("Response: " + request.getResponseCode() + " "
				+ request.getResponseMessage());
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String b = null;
		StringBuffer sb = new StringBuffer();
		while ((b = reader.readLine()) != null) {
			sb.append(b);
		}
		return sb.toString();
	}
}
