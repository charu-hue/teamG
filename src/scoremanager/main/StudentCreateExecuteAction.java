


package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception{
		HttpSession session = req.getSession();//セッション
		Teacher teacher = (Teacher)session.getAttribute("user");

		StudentDao sDao = new StudentDao();

		int entYear = Integer.parseInt(req.getParameter("f1"));
		String no = req.getParameter("f2");
		String name = req.getParameter("f3");
		String classNum = req.getParameter("f4");
		boolean isAttend = true;
		School school = teacher.getSchool();

		System.out.println(no+name+classNum);
		Student stu = new Student();
		stu.setNo(no);
		stu.setName(name);
		stu.setEntYear(entYear);
		stu.setClassNum(classNum);
		stu.setAttend(isAttend);
		stu.setSchool(school);

		boolean save = sDao.save(stu);

		req.setAttribute("class_num", save);

		req.getRequestDispatcher("student_create_done.jsp").forward(req, res);


	}
}