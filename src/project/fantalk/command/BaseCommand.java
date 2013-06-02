package project.fantalk.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.fantalk.xmpp.Message;


abstract class BaseCommand implements CommandHandler {

    private final Pattern pattern;

    BaseCommand(String name, String... otherNames) {
        StringBuilder sb = new StringBuilder("(?:");// ("^[/-\@](?:")
        sb.append(name);
        for (String otherName : otherNames) {
            sb.append("|").append(otherName);
        }
        sb.append(")(\\s.*)?$");

        this.pattern = Pattern.compile(sb.toString());
    }

    public abstract void doCommand(Message message, String argument);

    public void doCommand(Message message, String commandName, String content) {
    	doCommand(message, content);
    }
    
	public void doCommand(Message message) {
		Matcher matcher = getMatcher(message);
		String commandName = matcher.group(0);
		String argument = matcher.group(1);
		if (argument != null) {
			argument = argument.trim();
		}
		doCommand(message, commandName, argument);
	}

    public boolean matches(Message msg) {
        return getMatcher(msg) != null;
    }

    private Matcher getMatcher(Message msg) {
        Matcher m = pattern.matcher(msg.content.trim());
        return m.matches() ? m : null;
    }
}
