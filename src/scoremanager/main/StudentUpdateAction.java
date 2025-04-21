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
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String no = req.getParameter("f2");  // 学生番号取得
        if (no != null && !no.isEmpty()) {
            StudentDao sDao = new StudentDao();
            Student student = sDao.get(no);  // 学生情報取得

            if (student != null) {
                req.setAttribute("f1", student.getEntYear());  // 入学年度
                req.setAttribute("f2", student.getNo());       // 学生番号
                req.setAttribute("f3", student.getName());     // 氏名
                req.setAttribute("f4", student.getClassNum()); // クラス番号
                req.setAttribute("f5", student.isAttend());    // 在学フラグ
            }
        }

        ClassNumDao cNumDao = new ClassNumDao();
        List<String> classList = cNumDao.filter(teacher.getSchool());

        req.setAttribute("class_num_set", classList);

        req.getRequestDispatcher("student_update.jsp").forward(req, res);
    }
}
