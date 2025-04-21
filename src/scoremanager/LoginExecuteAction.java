package scoremanager;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;


public class LoginExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String url = "";
		String id = "";
		String password = "";
		TeacherDao teacherDao = new TeacherDao();
		Teacher teacher = null;

		id = req.getParameter("id");
		password = req.getParameter("password");

		teacher = teacherDao.login(id, password);


		if (teacher != null) {

			HttpSession session = req.getSession(true);
			teacher.setAuthenticated(true);
			session.setAttribute("user", teacher);

			url = "main/Menu.action";
			res.sendRedirect(url);
		} else {
			List<String> errors = new ArrayList<>();
			errors.add("IDまたはパスワードが確認できませんでした");
			req.setAttribute("errors", errors);
			req.setAttribute("id", id);

			url = "login.jsp";
			req.getRequestDispatcher(url).forward(req, res);
		}

	}

}
