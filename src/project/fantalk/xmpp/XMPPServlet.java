package project.fantalk.xmpp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.fantalk.api.Utils;
import project.fantalk.command.Command;
import project.fantalk.model.Datastore;


import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.XMPPService;
import com.google.appengine.api.xmpp.XMPPServiceFactory;

public final class XMPPServlet extends HttpServlet {

	private static final long serialVersionUID = 4607743537109047772L;

	private static final Logger logger = Logger.getLogger(XMPPServlet.class
			.getName());

	private XMPPService xmpp;
	private Message xmppMessage;

	public XMPPServlet() {
		super();
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		xmpp = XMPPServiceFactory.getXMPPService();
		try {
			xmppMessage = xmpp.parseMessage(req);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage());
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		doXMPP(xmppMessage);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void doXMPP(Message xmppMessage) {
		/** 如果消息内容全为空白字符，直接返回，不处理 */
		String content = xmppMessage.getBody().trim();
		if (!Utils.isEmpty(content)) {
			Datastore datastore = Datastore.getInstance();
			JID sender = xmppMessage.getFromJid();
			String email = XMPPUtils.JIDToEmail(sender);
			datastore.getAndCacheMember(email);
			if (content.startsWith("-")) {
				content = content.substring(1);
			}
			project.fantalk.xmpp.Message message = new project.fantalk.xmpp.Message.Builder()
					.setContent(content).setSender(sender).setType(
							project.fantalk.xmpp.Message.Type.XMPP).build();
			Command.getCommandHandler(message).doCommand(message);
		}

	}

}
