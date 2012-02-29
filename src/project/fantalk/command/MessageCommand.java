package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.fanfou.domain.User;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;


import com.google.appengine.api.xmpp.JID;

public class MessageCommand extends BaseCommand {
	public MessageCommand() {
		super("message");
	}

	@Override
	public void doCommand(Message message, String argument) {
		String email = message.email;
		JID sender = message.sender;
		if (Utils.isEmpty(argument)) {
			// 暂时只支持使用短ID
			XMPPUtils
					.sendMessage(
							"格式错误，回复私信消息的格式为: -message id content 或者 -message id contentor.",
							sender);
			return;
		}
		String[] args = argument.split("\\s+", 2);
		if (args.length < 2) {
			XMPPUtils
					.sendMessage(
							"格式错误，回复消息的格式为: -message id content 或者 -message id contentor.",
							sender);
			return;
		}
		Datastore ds = Datastore.getInstance();
		Member m = (Member) ds.getAndCacheMember(email);
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			String reply = args[1];
			String userId = args[0];
			User user = fanfou.showUser(userId);
			if (user == null) {
				XMPPUtils.sendMessage("无效用户ID，回复失败", sender);
			} else {
				if (!fanfou.sendMessage(userId, reply)) {
					XMPPUtils.sendMessage("回复失败", sender);
				}
			}
		} else {
			XMPPUtils.sendMessage("未绑定饭否帐号，回复失败", sender);
		}

	}

	public String documentation() {
		// StringBuilder sb = new StringBuilder();
		// sb.append(
		// "-direct userId content 或者 -private userId contentor \n给用户发送私信，参数为用户Id(即进入用户控件，浏览器左上角括弧中的内容)和回复内容");
		// return sb.toString();
		return null;
	}
}
