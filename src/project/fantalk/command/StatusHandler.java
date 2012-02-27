package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.fanfou.Notification;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class StatusHandler extends BaseCommand {

    public StatusHandler() {
        super("status", "me");
    }

    @Override
    public void doCommand(Message message, String argument) {
        JID sender = message.sender;
        String email = message.email;
        Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
        StringBuilder sb = new StringBuilder();
        String pushType = m.getPushType();
        String pushName;
        if (pushType.equalsIgnoreCase("home")) {
            pushName = "主页消息推送开启";
        } else if (pushType.equalsIgnoreCase("mentions")) {
            pushName = "@消息推送开启";
        } else {
            pushName = "消息推送已关闭";
        }
        sb.append("你的帐号: ").append(m.getEmail()).append("\n").append(
                "消息同步: " + (m.isSyncOn() ? "开启" : "关闭")).append("\n").append(
                "消息推送: " + pushName).append("\n");
        // .append("Binding Account: ");
        StringBuilder bindingOkSb = new StringBuilder();
//        StringBuilder bindingErrorSb = new StringBuilder();
        if(!Utils.isEmpty(m.getFanfouUserName())) {
        	 bindingOkSb.append("饭否OAuth  ");
        }
        if (!Utils.isEmpty(m.getTwitterPassword())) {
            bindingOkSb.append("推特  ");
        }
        if (!Utils.isEmpty(m.getSinaPassword())) {
            bindingOkSb.append("新浪微博  ");
        }
        if (!Utils.isEmpty(m.getTecentPassword())) {
            bindingOkSb.append("腾讯微博  ");
        }
        if (!Utils.isEmpty(m.getSohuPassword())) {
        	bindingOkSb.append("搜狐微博  ");
        }
        if (!Utils.isEmpty(m.getNeteasePassword())) {
            bindingOkSb.append("网易微博  ");
        }
        if (!Utils.isEmpty(m.getDoubanPassword())) {
            bindingOkSb.append("豆瓣我说 ");
        }
        if (!Utils.isEmpty(m.getDiguPassword())) {
            bindingOkSb.append("嘀咕  ");
        }
        if (Utils.isEmpty(bindingOkSb.toString())) {
            sb.append("未绑定任何账号\n");
        } else {
            sb.append("绑定的账号：").append(bindingOkSb).append("\n");
        }
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			Notification notificationData = fanfou.notification();
			if (notificationData != null) {
				sb.append("饭否上未读的mentions消息有")
						.append(notificationData.getMentions())
						.append("条，未读的私信信息有")
						.append(notificationData.getDirect_messages())
						.append("条，关注请求数量有")
						.append(notificationData.getFriend_requests())
						.append("个\n");
			}
		}
        XMPPUtils.sendMessage(sb.toString(), sender);

    }

    public String documentation() {
        return "-status\n查看个人的设置和状态信息，也可使用-me";
    }

}
