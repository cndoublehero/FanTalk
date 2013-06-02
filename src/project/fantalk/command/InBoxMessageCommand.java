package project.fantalk.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Message;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;

public class InBoxMessageCommand extends AbstractMessageHandler {

	public InBoxMessageCommand() {
		super("inbox", "in", "6");
	}

	public String documentation() {
		return "-inbox or -in \n 显示获取收件箱私信列表的消息，默认为20条";
	}

	@Override
	public List<Message> getMessages(FanfouService fanfou, int count,
			String lastId, String maxId, int page) {
		return fanfou.inbox(count, lastId, maxId, page);
	}

	@Override
	public String getMessage() {
		return "收件箱私信列表中没有新的消息！";
	}

	@Override
	public StringBuilder processMessages(Member m, List<Message> messages) {
		return  processInBoxMessages(m, messages);
	}

	public StringBuilder processInBoxMessages(Member m, List<Message> messages) {
		StringBuilder sb = new StringBuilder();
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			 if (i == 0) {
			        m.setLastMessageId(message.getId());
			        m.put();
			    }
			String senderName = message.getSenderScreenName();
			String text = message.getText();
			map.put(i, message.process());
			String time = Utils.getInterval(message.getCreatedAt());
			sb.append(senderName).append(" 说：\n").append(text).append("  [ ID:")
					.append(i)
					/** .append(id) */
					.append(",  Time:").append(time).append(" ]\n\n");
		}
		if (map!=null && !map.isEmpty()) {
			Datastore.getInstance().addMessageCache(m.getEmail(), map);
		}
		if (!Utils.isEmpty(sb.toString())) {
			sb.append("------------------------------\n");
		}
		return sb;
	}
}
