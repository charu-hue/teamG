package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        StudentDao sDao = new StudentDao();

        int entYear = Integer.parseInt(req.getParameter("f1"));
        String no = req.getParameter("f2");
        String name = req.getParameter("f3");
        String classNum = req.getParameter("f4");
        boolean isAttend = req.getParameter("f5") != null;  // チェックボックス: nullで未選択
        School school = teacher.getSchool();

        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(isAttend);
        student.setSchool(school);

        boolean result = sDao.save(student);  // ← 更新でも新規でも save() が対応！

        req.setAttribute("update_result", result);

        req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
    }
}
