package project.fantalk.command;

import project.fantalk.api.Utils;
import project.fantalk.xmpp.Message;
import project.fantalk.xmpp.XMPPUtils;

public class HelpHandler extends BaseCommand {

    public HelpHandler() {
        super("help", "h");
    }

    @Override
    public void doCommand(Message message, String argument) {
        StringBuilder sb = new StringBuilder();
        sb.append("FanTalk命令以/或-开头，下面是目前支持的命令列表\n");
        sb.append("-------------------------------------------------\n");
        for (Command command : Command.values()) {
            String docs = command.commandHandler.documentation();
            if (docs != null) {
                sb.append("").append(command.commandHandler.documentation())
                        .append("\n\n");
            }
        }
        sb.append("支持页面：\n").append(
                "主页：http://"+Utils.getAppID()+".appspot.com/\n").append(
                "项目：http://code.google.com/p/fanfoutalk/\n").append(
                "饭否：http://fanfou.com/mcxiaoke\n");
        sb.append("-------------------------------------------------");
        XMPPUtils.sendMessage(sb.toString(), message.sender);
    }

    public String documentation() {
        return "-help\n显示命令列表和帮助信息";
    }

}
