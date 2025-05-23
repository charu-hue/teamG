package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目一覧を取得して表示するアクション。
 * ログイン中の教師が所属する学校に登録されている科目情報を取得し、
 * JSP に渡して一覧表示を行う。
 */
public class SubjectListAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        SubjectDao sDao = new SubjectDao();
        List<Subject> subjects = sDao.getAll(teacher.getSchool());

        req.setAttribute("subjects", subjects);
        req.getRequestDispatcher("subject_list.jsp").forward(req, res);
    }
}
