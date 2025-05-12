package scoremanager.main;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;

public class TestListAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            HttpSession session = req.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");
            School school = teacher.getSchool();

            // 入学年度リストの取得
            StudentDao studentDao = new StudentDao();
            List<Integer> entYearList = studentDao.getEntYearList(school);
            req.setAttribute("ent_year_set", entYearList);

            // クラス番号リストの取得
            ClassNumDao classNumDao = new ClassNumDao();
            List<String> classNumList = classNumDao.filter(school);
            req.setAttribute("class_num_set", classNumList);

            // 科目名リストの取得
            SubjectDao subjectDao = new SubjectDao();
            List<Subject> subjectList = subjectDao.getAll(school);
            Set<String> subjectNameSet = subjectList.stream()
                                                    .map(Subject::getName)
                                                    .collect(Collectors.toSet());
            req.setAttribute("subject_name_set", subjectNameSet);

            // JSPにフォワード
            req.getRequestDispatcher("test_list.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "データ取得時にエラーが発生しました。");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
