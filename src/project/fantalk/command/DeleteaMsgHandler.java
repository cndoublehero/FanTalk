package project.fantalk.command;

import java.util.Map;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.XMPPUtils;

public class DeleteaMsgHandler extends BaseCommand {
	public DeleteaMsgHandler() {
		super("delete", "d");
	}

	@Override
	public void doCommand(project.fantalk.xmpp.Message message, String argument) {
		String email = message.email;
		JID sender = message.sender;
		if (Utils.isEmpty(argument)) {
			// 暂时只支持使用短ID
			XMPPUtils.sendMessage(getErrorMsg(), sender);
			return;
		}
		int index = 0;
        if (!Utils.isEmpty(argument)) {
        	index = Utils.toInt(argument);
        }
		Datastore ds = Datastore.getInstance();
		Map<Integer, String> map = ds.getStatusesCache(email);
		if (map != null && !map.isEmpty()) {
			int id = index;
			if (id >= 0 && id < map.size()) {
				Member m = ds.getAndCacheMember(email);
				FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
				if (fanfou != null) {
					String[] msgArgs = Utils.process(map.get(id));
					if (fanfou.delete(msgArgs[0])) {
						XMPPUtils.sendMessage(getOkMsg(msgArgs[2]), sender);
					} else {
						XMPPUtils.sendMessage("删除失败", sender);
					}
				} else {
					XMPPUtils.sendMessage("未绑定饭否帐号，删除失败", sender);
				}
			} else {
				XMPPUtils.sendMessage("删除失败", sender);
			}
		} else {
			XMPPUtils.sendMessage("消息没有缓存，无法回复", sender);
		}
	}

	public boolean executeMessage(FanfouService fanfou, String text,
			String... args) {
		return fanfou.delete(args[0]);
	}

	public Map<Integer, String> getCache(String email, Datastore ds) {
		return ds.getStatusesCache(email);
	}

	public String getErrorMsg() {
		return "格式错误，删除某条消息的格式为: -delete or -d id.";
	}

	public String getOkMsg(String text) {
		return "删除消息成功，消息内容为 ：      " + Utils.encodeHtmlToText(text);
	}

	@Override
	public String documentation() {
		return "-delete or d id 删除参数为id的消息";
	}
}
