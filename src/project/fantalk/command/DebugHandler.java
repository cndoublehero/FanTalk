package project.fantalk.command;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import project.fantalk.api.Utils;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class DebugHandler extends BaseCommand {
    private static final String CMD_CLEAR_CACHE = "cc";
    private static final String CMD_REMOVE_USER = "rm";
    private static final String CMD_REMOVE_USER_CACHE = "rmc";
    private static final String CMD_SHOW_USER_INFO = "su";
    private static final String CMD_IS_CACHED = "hc";
    private static final String CMD_SHOW_STATUSES_CACHE = "ss";
    private static final String CMD_SHOW_ALL = "showall";
    
    public DebugHandler() {
        super("debug");
    }

    @Override
    public void doCommand(Message message, String argument) {
        String email = message.email;
        JID sender = message.sender;
        if (!XMPPUtils.isAdmin(email)) {
            return;
        }
        if (Utils.isEmpty(argument)) {
            showHelp(sender);
            return;
        }
        Datastore datastore = Datastore.getInstance();
        String[] args = argument.split("\\s+", 2);
        String command = args[0];
        if (CMD_CLEAR_CACHE.equalsIgnoreCase(command)) {
            datastore.clearCache();
            XMPPUtils.sendMessage("已清除所有缓存", sender);
            return;
        }
        if (CMD_SHOW_ALL.equalsIgnoreCase(command)) {
			List<String> emails = datastore.getAllEmails();
			for (String str : emails) {
				Member m = datastore.getAndCacheMember(str);
				if (m != null) {
					StringBuilder sb = getDetailMember(m);
					XMPPUtils.sendMessage(sb.toString(), sender);
				}
			}
			return;
		}
        String user = args[1];
        if (Utils.isEmpty(user)) {
            showHelp(sender);
            return;
        } else if (CMD_REMOVE_USER.equalsIgnoreCase(command)) {
            Member m = datastore.getMember(user);
            if (m != null) {
                datastore.delete(user);
                datastore.removeCache(user);
                XMPPUtils.sendMessage("已从数据库删除用户: " + user, sender);
            } else {
                XMPPUtils.sendMessage("此用户不存在: " + user, sender);
            }
        } else if (CMD_REMOVE_USER_CACHE.equalsIgnoreCase(command)) {
            datastore.removeCache(user);
            XMPPUtils.sendMessage("已清除此用户的缓存: " + user, sender);
        } else if (CMD_IS_CACHED.equalsIgnoreCase(CMD_IS_CACHED)) {
            if (datastore.getFromCache(user) != null) {
                XMPPUtils.sendMessage("此用户的信息已缓存: " + user, sender);
            } else {
                XMPPUtils.sendMessage("此用户的信息没有缓存: " + user, sender);
            }
        } else if (CMD_SHOW_USER_INFO.equalsIgnoreCase(command)) {
            Member m = datastore.getMember(user);
            if (m != null) {
            	StringBuilder sb = getDetailMember(m);
                XMPPUtils.sendMessage(sb.toString(), sender);
            } else {
                XMPPUtils.sendMessage("此用户不存在: " + user, sender);
            }
        } else if (CMD_SHOW_STATUSES_CACHE.equalsIgnoreCase(command)) {
            Map<Integer, String> map = datastore.getStatusesCache(user);
            if (map!=null && !map.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                Iterator<Map.Entry<Integer, String>> iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<Integer, String> entry = (Map.Entry<Integer, String>) iter.next();
                    String msg = ((String) (entry.getValue())).replaceAll(
                    		Utils.SPLIT_STR, " ");
                    sb.append(entry.getKey()).append("--> ID:")
                            .append(msg).append("\n\n");
                }
                XMPPUtils.sendMessage(sb.toString(), sender);
            } else {
                XMPPUtils.sendMessage("没有此用户的消息缓存", sender);
            }
		}
    }

    public String documentation() {
        return null;
    }

	private StringBuilder getDetailMember(Member m) {
		StringBuilder sb = new StringBuilder();
		if (m != null) {
			sb.append("Email=" + m.getEmail())
					.append("\nSync Status=" + (m.isSyncOn() ? "On" : "Off"))
					.append("\nPush Type=" + m.getPushType())
					.append("\nFanfou Username=" + m.getUsername())
					.append("\nFanfou Password=" + m.getPassword())
					.append("\nFanfou OAuthUsername=" + m.getFanfouUserName())
					.append("\nFanfou OAuthPassword=" + m.getFanfouPassWord())
					.append("\nTwitter Token=" + m.getTwitterUsername())
					.append("\nTwitter Secret=" + m.getTwitterPassword())
					.append("\nSina Token=" + m.getSinaUsername())
					.append("\nSina Secret=" + m.getSinaPassword())
					.append("\nTecent Token=" + m.getTencentUsername())
					.append("\nTecent Secret=" + m.getTecentPassword())
					.append("\nDouban Token=" + m.getDoubanUsername())
					.append("\nDouabn Secret=" + m.getDoubanPassword())
					.append("\nFollow5 Username=" + m.getFollow5Username())
					.append("\nFollow5 Password=" + m.getFollow5Password())
					.append("\nDigu Usename=" + m.getDiguUsername())
					.append("\nDigu Password=" + m.getDiguPassword())
					.append("\nNetease Usename=" + m.getNeteaseUsername())
					.append("\nNeteasePassword=" + m.getNeteasePassword())
					.append("\n");
		}
		return sb;
	}
    
    private void showHelp(JID sender) {
        StringBuilder sb = new StringBuilder();
        sb.append("调试命令列表：\n").append("-d cc 清除所有缓存\n").append(
                "-debug rm email 从数据库删除某个用户的帐号信息\n").append(
                "-debug rmc email 清除缓存中某个用户的缓存数据\n").append(
                "-debug su email 显示数据库中某个用户的字段信息\n").append(
                "-debug hc email 显示缓存中是否有某个用户的信息\n").append(
                "-debug ss email 显示某个用户的推送消息缓存\n").append(
                "-debug showall 显示所有用户的信息\n");
        XMPPUtils.sendMessage(sb.toString(), sender);
    }

}
