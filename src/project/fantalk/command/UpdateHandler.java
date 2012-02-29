package project.fantalk.command;

import java.util.logging.Logger;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.digu.DiGuService;
import project.fantalk.api.douban.DoubanServiceOAuth;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.api.nets.NetsServiceOAuth;
import project.fantalk.api.sina.SinaServiceOAuth;
import project.fantalk.api.sohu.SohuServiceOAuth;
import project.fantalk.api.tencent.TencentServiceOAuth;
import project.fantalk.api.twitter.TwitterService;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;


import com.google.appengine.api.xmpp.JID;

public class UpdateHandler implements CommandHandler {
    private static final Logger logger = Logger.getLogger("UpdateCommand");

    public void doCommand(Message message) {
        logger.info("Message Content: " + message.content);
        JID sender = message.sender;
        String email = message.email;
        String content = message.content;
        if (Utils.isEmpty(content)) {
            return;
        }
        if(Utils.isOverLimit(content)){
        	XMPPUtils.sendMessage("消息超过140字限制，当前字数："+content.length()+"\n", sender);
        	return;
        }
        Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
        StringBuilder sbOK = new StringBuilder();
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			if (!fanfou.update(content).isOk()) {
				XMPPUtils.sendMessage("服务器连接异常，发送失败\n", sender);
			} else {
				sbOK.append("饭否  ");
			}
		}
        if (m.isSyncOn()) {
            if (!Utils.isEmpty(m.getTwitterUsername())) {
                TwitterService ts = new TwitterService(m);
                ReturnCode rc = ts.update(content);
                switch (rc) {
                    case ERROR_REPETITION:
                        XMPPUtils.sendMessage("消息重复，发送失败", sender);
                        break;
                    case ERROR_WORDS_LIMIT_ERROR:
                        XMPPUtils.sendMessage("字数超过限制，发送失败", sender);
                        break;
                    case ERROR_SERVER_ERROR:
                    case ERROR_FALSE:
                        XMPPUtils.sendMessage("服务器故障，发送失败", sender);
                        break;
                    case ERROR_OK:
                        sbOK.append("推特  ");
                    default:
                        break;
                }
            }
            if (!Utils.isEmpty(m.getSinaUsername())) {
                SinaServiceOAuth sinaOAuth = new SinaServiceOAuth(m);// 改为只支持OAuth验证
                ReturnCode rc = sinaOAuth.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("新浪微博API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("新浪微博  ");
                }
            }
            if (!Utils.isEmpty(m.getNeteaseUsername())) {
                NetsServiceOAuth nets = new NetsServiceOAuth(m);
                ReturnCode rc = nets.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("网易微博API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("网易微博  ");
                }
            }
            if (!Utils.isEmpty(m.getTencentUsername())) {
                TencentServiceOAuth qq = new TencentServiceOAuth(m);
                ReturnCode rc = qq.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("腾讯微博API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("腾讯微博  ");
                }
            }
            if (!Utils.isEmpty(m.getSohuPassword())) {
                SohuServiceOAuth sohu = new SohuServiceOAuth(m);
                ReturnCode rc = sohu.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("豆瓣API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("搜狐微博  ");
                }
            }
            if (!Utils.isEmpty(m.getDiguUsername())) {
                DiGuService digu = new DiGuService(m);
                ReturnCode rc = digu.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("嘀咕API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("嘀咕  ");
                }
            }
            if (!Utils.isEmpty(m.getDoubanPassword())) {
                DoubanServiceOAuth douban = new DoubanServiceOAuth(m);
                ReturnCode rc = douban.update(content);
                if (!rc.isOk()) {
                    // XMPP.sendMessage("豆瓣API服务器超时，同步失败", sender);
                } else {
                    sbOK.append("豆瓣我说  ");
                }
            }
        }
        if (!Utils.isEmpty(sbOK.toString())) {
            XMPPUtils.sendMessage("发送成功：" + sbOK.toString(), sender);
        }
    }

    public String documentation() {
        return null;
    }

    public boolean matches(Message message) {
        return true;
    }
}
