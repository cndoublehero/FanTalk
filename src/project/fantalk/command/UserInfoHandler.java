package project.fantalk.command;

import project.fantalk.xmpp.Message;

public class UserInfoHandler extends BaseCommand {

    public UserInfoHandler() {
        super("showuser","userinfo");
    }

    public void doCommand(Message message, String argument) {

    }

    public String documentation() {
        return null;
    }

}
