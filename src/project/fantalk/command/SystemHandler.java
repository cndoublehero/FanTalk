package project.fantalk.command;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.model.Datastore;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class SystemHandler extends BaseCommand {

	public SystemHandler() {
		super("sys", "system", "sysinfo");
	}

	public void doCommand(Message message, String argument) {
		JID sender = message.sender;
		String email = message.email;
		if (!XMPPUtils.isAdmin(email)) {
			return;
		}
		Datastore ds = Datastore.getInstance();
		int totalUserNum = ds.getAllEmailCount();
		int fanfouUserNum = ds.getFanfouBindCount();
		int activeUserNum = ds.getActiveFanFouUserCount();
		StringBuilder sb = new StringBuilder();
		sb.append("总用户数: ").append(totalUserNum).append("\n")
				.append("已绑定fanfou的用户数: ").append(fanfouUserNum).append("\n")
				.append("活跃用户数: ").append(activeUserNum).append("\n");
		XMPPUtils.sendMessage(sb.toString(), sender);
	}

	public String documentation() {
		return "-sys/sysinfo 查看统计信息";
	}
}
