package project.fantalk.command;

import project.fantalk.api.ReturnCode;
import project.fantalk.api.Utils;
import project.fantalk.api.twitter.TwitterService;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


public class TwitterHandler extends BaseCommand {

    public TwitterHandler() {
        super("twitter", "t");
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
        if (!Utils.isEmpty(m.getTwitterUsername())) {
            String tu = m.getTwitterUsername();
            String tp = m.getTwitterPassword();
            TwitterService ts = new TwitterService(tu, tp);
            ReturnCode rc = ts.update(argument);
            switch (rc) {
                case ERROR_REPETITION:
                    XMPPUtils.sendMessage("重复消息，发送失败", sender);
                    break;
                case ERROR_WORDS_LIMIT_ERROR:
                    XMPPUtils.sendMessage("超过140字限制，发送失败", sender);
                    break;
                case ERROR_SERVER_ERROR:
                    XMPPUtils.sendMessage("服务器故障，发送失败", sender);
                    break;
                case ERROR_FALSE:
                    XMPPUtils.sendMessage("未知错误，发送失败", sender);
                    break;
                case ERROR_OK:
                	XMPPUtils.sendMessage("成功发送到Twitter\n", sender);
                default:
                    break;
            }
        } else {
            XMPPUtils.sendMessage("未绑定Twitter帐号\n", sender);
        }
    }

    public String documentation() {
        return "-twitter content\n仅发送消息到Twitter(不发送到其它微博)，也可使用-twitter content, content表示你要发送的消息内容";
    }

}
