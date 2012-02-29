package project.fantalk.command;

import java.util.Map;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Datastore;

public class DirectMessageHandler extends AbstractPostMessageHandler {
	public DirectMessageHandler() {
		super("dierct", "private", "pm", "dm");
	}

	public String documentation() {
		return "-direct id content 或者 -private id contentor \n回复某条私信，参数为消息ID(接受的私信后面带的那个ID)和回复内容";
	}

	@Override
	public boolean executeMessage(FanfouService fanfou, String text,
			String... args) {
		String senderID = args[0];
		String messageId = args[2];
		return fanfou.replyMessage(senderID, messageId, text);
	}

	@Override
	public Map<Integer, String> getCache(String email, Datastore ds) {
		return ds.getMessageCache(email);
	}

	@Override
	public String getErrorMsg() {
		return "格式错误，回复私信消息的格式为: -direct id content 或者 -private id contentor.";
	}
}
