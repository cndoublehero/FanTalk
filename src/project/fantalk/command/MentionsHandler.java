package project.fantalk.command;

import java.util.List;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Status;
import project.fantalk.model.Member;


public final class MentionsHandler extends StatusesHandler {

	public MentionsHandler() {
		super("mentions", "tome");
	}

	public String documentation() {
		return "-mentions count\n查看最近的未读@消息，count为消息数量，无参数默认为5条，最多20条";
	}

	@Override
	public List<Status> getStatuses(FanfouService fanfou, int count,
			String lastId, String maxId, int page) {
		return fanfou.mentions(count, lastId, null, 0);
	}

	@Override
	public String getMessage() {
		return "无未读@消息！";
	}

	@Override
	public String getLastId(Member m) {
		return m.getLastMentionId();
	}

	@Override
	public void updateMember(Member m, String lastId) {
		m.setLastMentionId(lastId);
		m.put();
	}
}
