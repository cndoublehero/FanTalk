package project.fantalk.command;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.model.Datastore;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class SystemHandler extends BaseCommand {

    public SystemHandler() {
        super("sys","system","sysinfo");
    }

    public void doCommand(Message message, String argument) {
		JID sender = message.sender;
		String email = message.email;
        if (!XMPPUtils.isAdmin(email)) {
            return;
        }
		Datastore ds=Datastore.getInstance();
		int total=ds.getAllEmailCount();
		int active=ds.getFanfouBindCount();
		StringBuilder sb=new StringBuilder();
		sb.append("Total Accounts: ").append(total).append("\n")
		.append("Active Accounts: ").append(active).append("\n");
		XMPPUtils.sendMessage(sb.toString(), sender);
    }

    public String documentation() {
        return "-sys/sysinfo 查看统计信息";
    }
}
