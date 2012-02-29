package project.fantalk.command;

import java.util.List;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Status;
import project.fantalk.model.Member;


public final class HomeHandler extends StatusesHandler {

	public HomeHandler() {
		super("home");
	}

	public String documentation() {
		return "-home count\n查看主页的最近未读消息，count为消息数量，无参数默认为5条，最多20条";
	}

	@Override
	public List<Status> getStatuses(FanfouService fanfou, int count,
			String lastId, String maxId, int page) {
		return fanfou.home(count, lastId, null, 0);
	}

	@Override
	public String getMessage() {
		return  "无未读主页消息！";
	}

	@Override
	public String getLastId(Member m) {
		return m.getLastStatusId();
	}
	
	@Override
	public void updateMember(Member m, String lastId) {
		m.setLastStatusId(lastId);
		m.put();
	}
}
