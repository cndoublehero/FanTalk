package project.fantalk.command;

import java.util.Map;
import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;


import com.google.appengine.api.xmpp.JID;

public class DirectMessageHandler extends BaseCommand {
	public DirectMessageHandler() {
		super("dierct", "private","pm","dm");
	}

	@Override
	public void doCommand(Message message, String argument) {
		String email = message.email;
		JID sender = message.sender;
		if (Utils.isEmpty(argument)) {
			// 暂时只支持使用短ID
			XMPPUtils
					.sendMessage(
							"格式错误，回复私信消息的格式为: -direct id content 或者 -private id contentor.",
							sender);
			return;
		}
		String[] args = argument.split("\\s+", 2);
		if (args.length < 2) {
			XMPPUtils
					.sendMessage(
							"格式错误，回复消息的格式为: -direct id content 或者 -private id contentor.",
							sender);
			return;
		}
		Datastore ds = Datastore.getInstance();
		Map<Integer, String> map = ds.getMessageCache(email);
		if (map!=null && !map.isEmpty()) {
			String reply = args[1];
			int id = Utils.toInt(args[0]);
			if (id >= 0 && id < map.size()) {
				String s = map.get(id);
				String[] ss = Utils.process(s);
				String senderID = ss[0];
				String messageId = ss[2];
				Member m = (Member) ds.getAndCacheMember(email);
				FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
				if (fanfou != null) {
					if (!fanfou.replyMessage(senderID, messageId, reply)) {
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

	public String documentation() {
//		StringBuilder sb = new StringBuilder();
//		sb
//				.append(
//						"-direct id content 或者 -private id contentor \n回复某条私信，参数为消息ID(接受的私信后面带的那个ID)和回复内容");
//		return sb.toString();
		 return null;
	}
}
