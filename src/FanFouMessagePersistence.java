import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Status;

public class FanFouMessagePersistence {
	private final static String UserName = "";
	private final static String PassWord = "";
	private final static String UserId = null;

	public static void main(String[] args) throws IOException {
		FanFouMessagePersistence fouFouMessageService = new FanFouMessagePersistence();
		int pageNo = 1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		FanfouService fanfouService = new FanfouService(UserName, PassWord);
		List<Status> statusList = fanfouService.timeline(UserId, 20, null,
				null, pageNo);
		while (statusList != null && !statusList.isEmpty()) {
			for (Status status : statusList) {
				String message = "\n"
						+ dateFormat.format(status.getCreatedAt()) + "    "
						+ status.getText() + "   By " + status.getSource()
						+ "\n";
				sb.append(message);
			}
			statusList = fanfouService.timeline(UserId, 20, null, null,
					++pageNo);
		}
		fouFouMessageService.saveFanFouMessage(sb.toString(), UserId);
	}

	private void saveFanFouMessage(String str, String userId)
			throws IOException {
		if (userId == null || "".equals(userId)) {
			userId = getFileName();
		}
		File file = new File(userId + ".txt");
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream fileOutPutStream = new FileOutputStream(file);
		BufferedOutputStream bfOutPutStream = new BufferedOutputStream(
				fileOutPutStream);
		bfOutPutStream.write(str.getBytes());
		bfOutPutStream.flush();
		System.out.println(file.getAbsolutePath());
		bfOutPutStream.close();
		fileOutPutStream.close();
	}

	private String getFileName() {
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date.getTime());
	}
}
