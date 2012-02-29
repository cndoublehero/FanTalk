package project.fantalk.command;

import java.util.Map;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Datastore;

public class ReplyHandler extends AbstractPostMessageHandler {

	public ReplyHandler() {
		super("reply", "re", "r", "@", "0", "00");
	}

	public String documentation() {
		return "-re id content\n回复某条消息，参数为消息ID(接受的消息后面带的那个ID)和回复内容";
	}

	@Override
	public boolean executeMessage(FanfouService fanfou, String text,
			String... args) {
		String statusId = args[0];
		String name = args[1];
		return fanfou.reply("@" + name + " " + text, statusId);
	}

	@Override
	public Map<Integer, String> getCache(String email, Datastore ds) {
		return ds.getStatusesCache(email);
	}

	@Override
	public String getErrorMsg() {
		return "格式错误，回复消息的格式为: -re id content.";
	}
}
