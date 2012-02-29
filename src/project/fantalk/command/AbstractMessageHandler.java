package project.fantalk.command;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.fanfou.domain.Message;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;

public abstract class AbstractMessageHandler extends BaseCommand {

	public AbstractMessageHandler(String name, String... otherNames) {
		super(name, otherNames);
	}

	public abstract List<project.fantalk.api.fanfou.domain.Message> getMessages(
			FanfouService fanfou, int count, String lastId, String maxId,
			int page);

	public abstract String getMessage();
	
	@Override
	public void doCommand(project.fantalk.xmpp.Message message, String argument) {

		int count = 5;
		if (!Utils.isEmpty(argument)) {
			count = Utils.toInt(argument);
		}
		JID sender = message.sender;
		String email = message.email;
		Datastore datastore = Datastore.getInstance();
		Member m = (Member) datastore.getAndCacheMember(email);
		if (!Utils.canDo(m.getLastActive())) {
			if (!XMPPUtils.isAdmin(email)) {
				XMPPUtils.sendMessage("操作过于频繁，请10秒后再试！", sender);
				return;
			}
		}
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			m.setLastActive(Calendar.getInstance(
					TimeZone.getTimeZone("GMT+08:00")).getTime());// 更新最后一次API活动时间
			String lastMessageId = m.getLastMessageId();
			List<project.fantalk.api.fanfou.domain.Message> messages = getMessages(
					fanfou, count, lastMessageId, null, 0);
			if (messages == null || messages.isEmpty()) {// 如果无未读消息
				XMPPUtils.sendMessage(getMessage(), sender);
			} else {
				StringBuilder sb = processMessages(m, messages);
				String sendMessages = sb.toString();
				if (Utils.isEmpty(sendMessages)) {
					XMPPUtils.sendMessage(getMessage(), sender);
				} else {
					XMPPUtils.sendMessage(sendMessages, sender);
				}
			}
		} else {
			XMPPUtils.sendMessage("你还未绑定饭否帐号，无法查看主页消息！", sender);
		}
		m.put();
	}

	public abstract StringBuilder processMessages(Member m, List<Message> messages);

}
