package project.fantalk.command;

import java.util.List;

import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.domain.Status;


public final class PubliclineCommand extends StatusesHandler {

	public PubliclineCommand() {
		super("look", "see", "public","5");
	}

	public String documentation() {
		return "-look \n显示随便看看的消息，默认为20条";
	}

	@Override
	public List<Status> getStatuses(FanfouService fanfou, int count,
			String lastId, String maxId, int page) {
		return fanfou.lookAround();
	}

	@Override
	public String getMessage() {
		return "随便看看中没有消息！";
	}

}
