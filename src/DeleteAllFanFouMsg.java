import java.util.List;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Status;
import project.fantalk.api.fanfou.oauth.FanFouServiceOAuth;

public class DeleteAllFanFouMsg {
	private final static String UserName = "";
	private final static String PassWord = "";

	public static void main(String[] args) {
		FanfouService fanfouService = new FanFouServiceOAuth(UserName, PassWord);
		int pageNo = 1;
		List<Status> statusList = fanfouService.timeline(pageNo);
		while (statusList != null && !statusList.isEmpty()) {
			for (Status status : statusList) {
				fanfouService.delete(status.getId());
			}
			statusList = fanfouService.timeline(++pageNo);
		}
	}
}
