package project.fantalk.command;

import java.util.Map;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class RetweetHandler extends BaseCommand {

    public RetweetHandler() {
        super("retweet", "rt");
    }

    @Override
    public void doCommand(Message message, String argument) {
        String email = message.email;
        JID sender = message.sender;
        if (Utils.isEmpty(argument)) {
            // 暂时只支持使用短ID
            XMPPUtils.sendMessage("格式错误，回复消息的格式为: -re id content.", sender);
            return;
        }
        String[] args = argument.split("\\s+", 2);
        if (args.length < 2) {
            XMPPUtils.sendMessage("格式错误，回复消息的格式为: -re id content.", sender);
            return;
        }
        Datastore ds = Datastore.getInstance();
        Map<Integer, String> map = ds.getStatusesCache(email);
        if (map!=null && !map.isEmpty()) {
            String comment = args[1];
            if (Utils.isEmpty(comment)) {
                comment = "转";
            }
            int id = Utils.toInt(args[0]);
            if (id >= 0 && id < map.size()) {
                String s = map.get(id);
                String[] ss = Utils.process(s);
                String statusId = ss[0];
                String name = ss[1];
                String origText = ss[2];
                Member m = (Member) ds.getAndCacheMember(email);
                FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
				if (fanfou != null) {
					if (!fanfou.repost(comment + "->@" + name + " " + origText,
							statusId)) {
						XMPPUtils.sendMessage("回复失败", sender);
					}
				} else {
                    XMPPUtils.sendMessage("未绑定饭否帐号，回复失败", sender);
                }
            } else {
                XMPPUtils.sendMessage("无效ID，回复失败", sender);
            }
        } else {
            XMPPUtils.sendMessage("消息没有缓存，无法回复", sender);
        }
    }

    public String documentation() {
//         return "-retweet or -rt id content 转发某条消息，参数为消息ID(推送过来的消息后面带的那个ID)和评论内容";
        return null;
    }

}
