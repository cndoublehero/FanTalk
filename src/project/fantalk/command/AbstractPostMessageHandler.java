package project.fantalk.command;

import java.util.Map;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;

public abstract class AbstractPostMessageHandler extends BaseCommand {

	public AbstractPostMessageHandler(String name, String... otherNames) {
		super(name, otherNames);
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
		String[] args = argument.split("\\s+", 2);
		if (args.length < 2) {
			XMPPUtils.sendMessage(getErrorMsg(), sender);
			return;
		}
		Datastore ds = Datastore.getInstance();
		Map<Integer, String> map = getCache(email, ds);
		if (map != null && !map.isEmpty()) {
			String text = args[1];
			int id = Utils.toInt(args[0]);
			if (id >= 0 && id < map.size()) {
				Member m = ds.getAndCacheMember(email);
				FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
				if (fanfou != null) {
					if (executeMessage(fanfou, text, Utils.process(map.get(id)))) {
						XMPPUtils.sendMessage("回复成功", sender);
					} else {
						XMPPUtils.sendMessage("回复失败", sender);
					}
				} else {
					XMPPUtils.sendMessage("未绑定饭否帐号，回复失败", sender);
				}
			} else {
				XMPPUtils.sendMessage("无效ID，回复失败", sender);
			}
		} else {
			XMPPUtils.sendMessage("消息没有缓存，无法回复", sender);
		}
	}

	public abstract boolean executeMessage(FanfouService fanfou, String text,
			String... args);

	public abstract Map<Integer, String> getCache(String email, Datastore ds);

	public abstract String getErrorMsg();
}
