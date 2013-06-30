package project.fantalk.command;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
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

public class SingleUpdateCommand extends BaseCommand {
	SingleUpdateCommand() {
		super("fanfou", "fan", "f", "twitter", "douban", "nets", "tencent",
				"sohu", "sina");
	}

	public String documentation() {
		return "-fanfou content\n仅发送消息到饭否(不发送到其它微博)，也可使用-twitter content、-douban content、-nets content、-tencent content、-sohu content、-sina content单独发送到各个微博，其中 content表示你要发送的消息内容";
	}

	@Override
	public void doCommand(Message message, String argument) {
	}

	@Override
	public void doCommand(Message message, String commandName, String content) {
		JID sender = message.sender;
		String email = message.email;
		if (Utils.isEmpty(content)) {
			XMPPUtils.sendMessage("消息内容不能为空", sender);
			return;
		}
		if (Utils.isOverLimit(content)) {
			XMPPUtils.sendMessage("消息超过140字限制，字数：" + content.length() + "\n",
					sender);
			return;
		}
		Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
		StringBuilder sbOK = new StringBuilder();
		if (isStartsWith(commandName, "fanfou", "fan", "f")) {
			FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
			if (fanfou != null) {
				if (!fanfou.update(content).isOk()) {
					XMPPUtils.sendMessage("服务器连接异常，发送失败\n", sender);
				} else {
					sbOK.append("饭否  ");
				}
			}
		} else if (isStartsWith(commandName, "twitter")) {
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
		} else if (isStartsWith(commandName, "douban")) {
			if (!Utils.isEmpty(m.getDoubanPassword())) {
				DoubanServiceOAuth douban = new DoubanServiceOAuth(m);
				ReturnCode rc = douban.update(content);
				if (!rc.isOk()) {
				} else {
					sbOK.append("豆瓣我说  ");
				}
			}
		} else if (isStartsWith(commandName, "nets")) {
			if (!Utils.isEmpty(m.getNeteaseUsername())) {
				NetsServiceOAuth nets = new NetsServiceOAuth(m);
				ReturnCode rc = nets.update(content);
				if (!rc.isOk()) {
				} else {
					sbOK.append("网易微博  ");
				}
			}
		} else if (isStartsWith(commandName, "tencent")) {
			if (!Utils.isEmpty(m.getTencentUsername())) {
				TencentServiceOAuth qq = new TencentServiceOAuth(m);
				ReturnCode rc = qq.update(content);
				if (!rc.isOk()) {
				} else {
					sbOK.append("腾讯微博  ");
				}
			}
		} else if (isStartsWith(commandName, "sohu")) {
			if (!Utils.isEmpty(m.getSohuPassword())) {
				SohuServiceOAuth sohu = new SohuServiceOAuth(m);
				ReturnCode rc = sohu.update(content);
				if (!rc.isOk()) {
				} else {
					sbOK.append("搜狐微博  ");
				}
			}
		} else if (isStartsWith(commandName, "sina")) {
			if (!Utils.isEmpty(m.getSinaUsername())) {
				SinaServiceOAuth sinaOAuth = new SinaServiceOAuth(m);// 改为只支持OAuth验证
				ReturnCode rc = sinaOAuth.update(content);
				if (!rc.isOk()) {
				} else {
					sbOK.append("新浪微博  ");
				}
			}
		} else {
			XMPPUtils.sendMessage("未知微博，无法更新消息", sender);
		}
		if (!Utils.isEmpty(sbOK.toString())) {
			XMPPUtils.sendMessage("发送成功：" + sbOK.toString(), sender);
		}
	}

	private boolean isStartsWith(String commandName, String... names) {
		if (!Utils.isEmpty(commandName)) {
			for (String name : names) {
				if (commandName.startsWith(name)) {
					return true;
				}
			}
		}
		return false;
	}
}
