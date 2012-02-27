package project.fantalk.command;

import project.fantalk.xmpp.Message;

public class FanTalkHandler extends BaseCommand {

    public FanTalkHandler() {
        super("fantalk");
    }

    @Override
    public void doCommand(Message message, String argument) {

    }

    public String documentation() {
        return null;
    }

}
