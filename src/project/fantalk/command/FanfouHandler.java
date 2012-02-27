package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class FanfouHandler extends BaseCommand {

	public FanfouHandler() {
		super("fanfou", "fan", "f");
	}

	@Override
	public void doCommand(Message message, String argument) {
		JID sender = message.sender;
		String email = message.email;
		if(Utils.isEmpty(argument)){
			XMPPUtils.sendMessage("消息内容不能为空", sender);
			return;
		}
        if(Utils.isOverLimit(argument)){
        	XMPPUtils.sendMessage("消息超过140字限制，字数："+argument.length()+"\n", sender);
        	return;
        }
		Member m = (Member) Datastore.getInstance().getAndCacheMember(email);
		FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
		if (fanfou != null) {
			if (!fanfou.update(argument).isOk()) {
				XMPPUtils.sendMessage("发送失败，请检查绑定的饭否帐号\n", sender);
			} else {
				XMPPUtils.sendMessage("成功发送到饭否\n", sender);
			}
		} else {
			XMPPUtils.sendMessage("未绑定饭否帐号",
					sender);
		}
	}

	public String documentation() {
		return "-fanfou content\n仅发送消息到饭否(不发送到其它微博)，也可使用-fanfou content， content表示你要发送的消息内容";
	}

}
