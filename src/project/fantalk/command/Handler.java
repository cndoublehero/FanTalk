package project.fantalk.command;

import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

/**
 * 吃掉所有以 - / 开头的但不是已实现的命令的消息
 * @author mcxiaoke
 */
public class Handler implements CommandHandler {

    public Handler() {}

    public void doCommand(Message message) {
        XMPPUtils.sendMessage("未知命令: " + message.content, message.sender);
    }

    public String documentation() {
        return null;
    }

    /**
     * 匹配所有以 - / 开头的消息
     */
    public boolean matches(Message message) {
        String content = message.content;
        if (content.startsWith("-") || content.startsWith("/")) {
            return true;
        }
        return false;
    }

}
