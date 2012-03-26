package project.fantalk.xmpp;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import project.fantalk.model.Datastore;
import project.fantalk.model.Member;


import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.XMPPFailureException;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;
import com.google.appengine.api.xmpp.SendResponse.Status;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class XMPPUtils {
    private static final Logger logger = Logger.getLogger(XMPPUtils.class.getName());
    private static XMPPService xmpp = XMPPServiceFactory.getXMPPService();

    private XMPPUtils() {}

    /**
     * @param message 消息内容
     * @param userJID 消息接收者JID 服务器给指定用户发送消息
     */
    public static Set<JID>  sendMessage(String message, JID recipient) {
        return sendMessage(message, Lists.newArrayList(recipient));
    }

    public static Set<JID>  sendMessage(String message, String gmail) {
        return sendMessage(message, Lists.newArrayList(new JID(gmail)));
    }

    /**
     * @param message 消息内容
     * @param member 消息接收者 服务器给指定用户发送消息
     */
    public static Set<JID>  sendMessage(String message, Member member) {
        logger.severe("sendDirect to " + member.getEmail());
        return sendMessage(message, member.getEmail());
    }

    public static Set<JID>  broadcast(String message) {
        List<JID> jids = Datastore.getInstance().getAllJIDs();
        return sendMessage(message, jids);
    }

    /**
     * @param email 用户的XMPP帐号 邀请用户
     */
    public static void invite(String email) {
        xmpp.sendInvitation(new JID(email));
    }

    /**
     * @param message 消息内容
     * @param toJIDs 消息接受对象
     * @return 返回发送失败的JID集合
     */
    public static Set<JID> sendMessage(String message, List<JID> recipients) {
        if (recipients == null || recipients.isEmpty()) {
            return Collections.emptySet();
        }
        SendResponse response = null;
        try {
            response = xmpp.sendMessage(new MessageBuilder().withBody(message)
                    .withRecipientJids(recipients.toArray(new JID[] {}))
                    .build());
        } catch (RuntimeException e) {
            return Collections.emptySet();
        }

        if (response == null) {
            logger.severe("XMPP.sendMessage() response is null!");
            return Collections.emptySet();
        }

        Set<JID> errorJIDs = Sets.newHashSet();
        for (JID jid : recipients) {
            Status status = response.getStatusMap().get(jid);
            if (status != Status.SUCCESS) {
                errorJIDs.add(jid);
                StringBuilder sb = new StringBuilder().append(
                        "sendMessage unsuccessful! ").append("status: ")
                        .append(status.name()).append(" from: ").append(
                                " / to: ").append(jid.getId());
                logger.severe(sb.toString());
            }
        }
        return errorJIDs;
    }

    /**
     * @param userJID 用户XMPP帐号
     * @return 用户是否在线
     */
    public static boolean getPresence(JID user) {
        try {
            return xmpp.getPresence(user).isAvailable();
        } catch (XMPPFailureException e) {
            logger.log(Level.WARNING,
                    "got exception while getting presence for " + user, e);
            return false;
        }
    }

    /**
     * 工具函数 JID ---> Email
     * @param jid JID参数
     * @return 返回对应的Email地址
     */
    public static String JIDToEmail(JID jid) {
        return jid.getId().toLowerCase().split("/")[0];
    }

    /**
     * 判断某个用户是否是管理员
     * @param jid JID参数
     * @return 如果是管理员，返回true
     */
	public static boolean isAdmin(String email) {
		final String admin = System.getProperty("admin");
		final String[] users = admin.split(",");
		if (users != null && users.length != 0) {
			for (String user : users) {
				if (email.equalsIgnoreCase(user)) {
					return true;
				}
			}
		}
		return false;
	}

}
