package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class UnBindHandler extends BaseCommand {

    public UnBindHandler() {
        super("unbind", "ub");
    }

    @Override
    public void doCommand(Message message, String argument) {
        String email = message.email;
        JID sender = message.sender;
        if (Utils.isEmpty(argument)) {
            XMPPUtils
                    .sendMessage(
                            "请指定要解除绑定的帐号类型：fanfou/twitter/sina/nets/digu/tecent/douban/sohu",
                            sender);
            return;
        }
        Member m = Datastore.getInstance().getAndCacheMember(email);
        if (argument.equalsIgnoreCase("fanfou")) {
            m.setUsername("");
            m.setPassword("");
            m.setFanfouUserName("");
            m.setFanfouPassWord("");
            XMPPUtils.sendMessage("饭否帐号已解除绑定", sender);
        } else if (argument.equalsIgnoreCase("twitter")) {
            m.setTwitterUsername("");
            m.setTwitterPassword("");
            XMPPUtils.sendMessage("Twitter帐号已解除绑定", sender);
        } else if ("sina".equalsIgnoreCase(argument)) {
            m.setSinaUsername("");
            m.setSinaPassword("");
            XMPPUtils.sendMessage("新浪微博帐号已解除绑定", sender);
        } else if ("nets".equalsIgnoreCase(argument)) {
            m.setNeteaseUsername("");
            m.setNeteasePassword("");
            XMPPUtils.sendMessage("网易微博帐号已解除绑定", sender);
        } else if ("digu".equalsIgnoreCase(argument)) {
            m.setDiguUsername("");
            m.setDiguPassword("");
            XMPPUtils.sendMessage("嘀咕帐号已解除绑定", sender);
        } else if ("tencent".equalsIgnoreCase(argument)) {
            m.setTencentUsername("");
            m.setTecentPassword("");
            XMPPUtils.sendMessage("腾讯微博帐号已解除绑定", sender);
        } else if ("douban".equalsIgnoreCase(argument)) {
            m.setDoubanUsername("");
            m.setDoubanPassword("");
            XMPPUtils.sendMessage("豆瓣帐号已解除绑定", sender);
        }else if ("sohu".equalsIgnoreCase(argument)) {
        	m.setSohuUsername("");
        	m.setSohuPassword("");
            XMPPUtils.sendMessage("搜狐微博帐号已解除绑定", sender);
        }  else {
            XMPPUtils.sendMessage("参数无效", sender);
        }
        m.put();
    }

    public String documentation() {
        return "-unbind fanfou/twitter/sina/nets/digu/douban/tencent/sohu\n取消绑定的饭否/推特/新浪/网易/嘀咕/豆瓣/腾讯/搜狐微博帐号";
    }

}
