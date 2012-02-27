package project.fantalk.command;

import project.fantalk.xmpp.Message;

public class UpgradeToolHandler extends BaseCommand {

    public UpgradeToolHandler() {
        super("upgrade");
    }

    public void doCommand(Message message, String argument) {

    }

    public String documentation() {
        return null;
    }

}
