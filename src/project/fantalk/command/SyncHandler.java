/**
 * 
 */
package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

import com.google.appengine.api.xmpp.JID;


/**
 * 同步发送到其它微博的开启/关闭
 * @author mcxiaoke
 */
public class SyncHandler extends BaseCommand {

    public SyncHandler() {
        super("sync");
    }

    @Override
    public void doCommand(Message message, String argument) {
        JID sender = message.sender;
        String email = message.email;
        if (Utils.isEmpty(argument)) {
            XMPPUtils.sendMessage("未指定参数，要开启同步更新，使用 -sync on, 要关闭同步更新，使用 -sync off",
                    sender);
            return;
        }
        Member m = Datastore.getInstance().getAndCacheMember(email);
        if (argument.equalsIgnoreCase("on")) {
            m.setSync(true);
            m.put();
            XMPPUtils.sendMessage("同步更新已开启", sender);
        } else if (argument.equalsIgnoreCase("off")) {
            m.setSync(false);
            m.put();
            XMPPUtils.sendMessage("同步更新已关闭", sender);
        } else {
            XMPPUtils.sendMessage("参数错误，要开启同步更新，使用 -sync on, 要关闭同步发送，使用 -sync off",
                    sender);
        }
    }

    public String documentation() {
        return "-sync on/off\n开启或关闭同步更新其它微博，暂时只支持同步更新Twitter";
    }

}
