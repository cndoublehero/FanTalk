package project.fantalk.task;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import project.fantalk.model.Datastore;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;


public final class TaskAddJobServlet extends HttpServlet {
	private static final long serialVersionUID = 189175067040480516L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Datastore ds = Datastore.getInstance();
	}

	private void addTask(String email) {
		Queue queue = QueueFactory.getQueue("fetch");
		TaskHandle th = queue.add(TaskOptions.Builder.withUrl(
				"/tasks/fanfoupush").method(Method.GET).param("email", email));
		if (th != null) {
//			logger.warning("add a task: " + th.getName());
		}
	}

}
