package project.fantalk.command;

import project.fantalk.xmpp.Message;

public interface CommandHandler {

    public void doCommand(Message message);

    public boolean matches(Message message);

    public String documentation();
}
