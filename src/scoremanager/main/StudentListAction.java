package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 入力取得
        String entYearStr = req.getParameter("f1");
        String classNum = req.getParameter("f2");
        String isAttendStr = req.getParameter("f3");

        // フォーム保持用に再セット
        req.setAttribute("f1", entYearStr);
        req.setAttribute("f2", classNum);
        req.setAttribute("f3", isAttendStr);

        // 値の整形
        int entYear = 0;
        boolean isAttend = false;

        if (entYearStr != null && !entYearStr.equals("0")) {
            entYear = Integer.parseInt(entYearStr);
        }
        if (isAttendStr != null && !isAttendStr.isEmpty()) {
            isAttend = true;
        }

        StudentDao sDao = new StudentDao();
        List<Integer> entYearList = sDao.getEntYearList(school);
        List<String> classList = sDao.getClassNumList(school);
        List<Student> students = null;

        // 絞り込み条件に応じて検索
        if (entYear != 0 && classNum != null && !classNum.equals("0")) {
            students = sDao.filter(school, entYear, classNum, isAttend);
        } else if (entYear != 0) {
            students = sDao.filter(school, entYear, isAttend);
        } else {
            students = sDao.filter(school, isAttend);
        }

        req.setAttribute("ent_year_set", entYearList);
        req.setAttribute("class_num_set", classList);
        req.setAttribute("students", students);

        req.getRequestDispatcher("student_list.jsp").forward(req, res);
    }
}
