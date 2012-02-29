package project.fantalk.command;

import java.util.List;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Message;
import project.fantalk.model.Member;


public class OutBoxMessageCommand extends AbstractMessageHandler {

	public OutBoxMessageCommand() {
		super("outbox", "out");
	}

	public String documentation() {
		return "-outbox or -out \n 显示获取发件箱私信列表的消息，默认为20条";
	}

	@Override
	public List<Message> getMessages(FanfouService fanfou, int count,
			String lastId, String maxId, int page) {
		return fanfou.outbox();
	}

	@Override
	public String getMessage() {
		return "发件箱私信列表中没有新的消息！";
	}

	@Override
	public StringBuilder processMessages(Member m, List<Message> messages) {
		return processOutBoxMessages(m, messages);
	}

	public static StringBuilder processOutBoxMessages(Member m,
			List<project.fantalk.api.fanfou.domain.Message> messages) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			String senderName = message.getSenderScreenName();
			String text = message.getText();
			String time = Utils.getInterval(message.getCreatedAt());
			sb.append(senderName).append(" 说：\n").append(text).append("  [ ID:")
					.append(i)
					/** .append(id) */
					.append(",  Time:").append(time).append(" ]\n\n");
		}
		if (!Utils.isEmpty(sb.toString())) {
			sb.append("------------------------------\n");
		}
		return sb;
	}
}
