package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String no = req.getParameter("f2");

        if (no != null && !no.isEmpty()) {
            StudentDao sDao = new StudentDao();
            Student student = sDao.get(no, teacher.getSchool());

            if (student != null) {
                req.setAttribute("f1", student.getEntYear());
                req.setAttribute("f2", student.getNo());
                req.setAttribute("f3", student.getName());
                req.setAttribute("f4", student.getClassNum());
                req.setAttribute("f5", student.isAttend());
            }
        }

        ClassNumDao cNumDao = new ClassNumDao();
        List<String> classList = cNumDao.filter(teacher.getSchool());
        req.setAttribute("class_num_set", classList);

        req.getRequestDispatcher("student_update.jsp").forward(req, res);
    }
}
