package project.fantalk.task;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * @author xu
 * @date 2011-3-17
 */
public class FanFouPushJob extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FanFouPushJob.class
            .getName());

    private static final long serialVersionUID = 6500531678929810778L;

    private static final int TaskQueueNumber = 10;//Queue列表数
    
    private static final int TaskNumber = 6;//一个Queue中可以放置的任务数
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long time1 = System.currentTimeMillis();
		try {
			for (int i = 0; i < TaskQueueNumber * TaskNumber; ++i) {
				queueArrays[i % TaskQueueNumber].add(TaskOptions.Builder
						.withUrl("/tasks/fanfoupush").method(Method.GET).param(
								"queueId", String.valueOf(i)));
			}
		} catch (Exception e) {
			logger.warning("Error in Class " + e.getClass().getSimpleName()
					+ ": " + e.getMessage());
		}
		logger.log(Level.INFO, " pushJob cost time : "
				+ (System.currentTimeMillis() - time1) / 1000);
	}

	private final static Queue[] queueArrays = new Queue[] {
			QueueFactory.getQueue("fetch0"), QueueFactory.getQueue("fetch1"),
			QueueFactory.getQueue("fetch2"), QueueFactory.getQueue("fetch3"),
			QueueFactory.getQueue("fetch4"), QueueFactory.getQueue("fetch5"),
			QueueFactory.getQueue("fetch6"), QueueFactory.getQueue("fetch7"),
			QueueFactory.getQueue("fetch8"), QueueFactory.getQueue("fetch9") };
}