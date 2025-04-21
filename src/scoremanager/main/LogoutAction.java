package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.Action;

public class LogoutAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {
		String url = "";
		HttpSession session=req.getSession();


		if (session.getAttribute("user") != null) {
			session.invalidate();
		}


		url = "logout.jsp";
		req.getRequestDispatcher(url).forward(req, res);
	}

}
