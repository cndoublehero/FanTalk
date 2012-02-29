package project.fantalk.task;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.xmpp.JID;

import project.fantalk.api.Utils;
import project.fantalk.api.fanfou.FanfouService;
import project.fantalk.api.fanfou.FanfouServiceFactory;
import project.fantalk.command.HomeHandler;
import project.fantalk.command.MentionsHandler;
import project.fantalk.command.StatusesHandler;
import project.fantalk.model.Datastore;
import project.fantalk.model.Member;
import project.fantalk.xmpp.XMPPUtils;

/**
 * @author xu
 * @date 2011-3-17
 */
public class FanfouPushTask extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FanfouPushTask.class
            .getName());

    private static final long serialVersionUID = 6500531678929810778L;

    private static final int PageSize = 15;
    
    private static final StatusesHandler MentionsHandler = new MentionsHandler();
    private static final StatusesHandler HomeHandler = new HomeHandler();
    
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long time1 = System.currentTimeMillis();
		String queueId = request.getParameter("queueId");
		int pageNo = Utils.toInt(queueId);
		Datastore datastore = Datastore.getInstance();
		List<String> users = datastore.getEmailsByPage(pageNo, PageSize);

		for (String email : users) {
			processFanFouMessage(email);
		}
		logger.log(Level.INFO, "queueId" + pageNo + " user cost time : "
				+ (System.currentTimeMillis() - time1) / 1000);
	}

	private void processFanFouMessage(String email) {
		Datastore datastore = Datastore.getInstance();
        Member m = (Member) datastore.getAndCacheMember(email);
        String pushType = m.getPushType();
        if ("home".equalsIgnoreCase(pushType)
				|| "mentions".equalsIgnoreCase(pushType)) {
			FanfouService fanfou = FanfouServiceFactory.newFanFouService(m);
			if (fanfou != null) {
				JID sender = new JID(email);
				String messages = null;
				if ("home".equalsIgnoreCase(pushType)) {
					messages = HomeHandler.getAndProcessStatus(fanfou, 20, m);
				} else {
					messages = MentionsHandler.getAndProcessStatus(fanfou, 20,
							m);
				}
				if (!Utils.isEmpty(messages)) {
					XMPPUtils.sendMessage(messages, sender);
				}
				logger.info("task of "
						+ email
						+ " "
						+ pushType
						+ "-execute at "
						+ Calendar
								.getInstance(TimeZone.getTimeZone("GMT+08:00"))
								.getTime().toString());
			}
		}
	}
}
