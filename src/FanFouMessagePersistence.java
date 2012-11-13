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

	public static void main(String[] args) throws IOException {
		FanFouMessagePersistence fouFouMessageService = new FanFouMessagePersistence();
		int pageNo = 1;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sb = new StringBuffer();
		FanfouService fanfouService = new FanfouService(UserName, PassWord);
		List<Status> statusList = fanfouService.timeline(pageNo);
		while (statusList != null && !statusList.isEmpty()) {
			for (Status status : statusList) {
				String message = "\n"
						+ dateFormat.format(status.getCreatedAt()) + "    "
						+ status.getText() + "   By " + status.getSource()
						+ "\n";
				sb.append(message);
			}
			statusList = fanfouService.timeline(++pageNo);
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
		bfOutPutStream.close();
		fileOutPutStream.close();
	}

	private String getFileName() {
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date.getTime());
	}
}
