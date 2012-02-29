package project.fantalk.command;

import java.util.Map;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.model.Datastore;

public class RetweetHandler extends AbstractPostMessageHandler {

	public RetweetHandler() {
		super("retweet", "rt");
	}

	public String documentation() {
		return "-retweet or -rt id content 转发某条消息，参数为消息ID(推送过来的消息后面带的那个ID)和评论内容";
	}

	@Override
	public boolean executeMessage(FanfouService fanfou, String text,
			String... args) {
		String statusId = args[0];
		String name = args[1];
		String origText = args[2];
		return fanfou.repost(text + "->@" + name + " " + origText, statusId);
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
