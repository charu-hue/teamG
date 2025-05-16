package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Test;
import bean.TestListStudent;
import dao.StudentDao;
import dao.TestDao;
import tool.Action;

public class TestListStudentExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            String studentNo = req.getParameter("f4");
            req.setAttribute("no", studentNo);

            if (studentNo == null || studentNo.trim().isEmpty()) {
                req.setAttribute("error", "学生番号を入力してください。");
                req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
                return;
            }

            StudentDao studentDao = new StudentDao();
            Student student = studentDao.get(studentNo);

            if (student == null) {
                req.setAttribute("error", "該当する学生が見つかりません。");
                req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
                return;
            }

            TestDao testDao = new TestDao();
            List<Test> testList = testDao.getByStudent(student);

            List<TestListStudent> list = new ArrayList<>();
            for (Test test : testList) {
                TestListStudent tls = new TestListStudent();
                tls.setSubjectCd(test.getSubject().getCd());
                tls.setSubjectName(test.getSubject().getName());
                tls.setNum(test.getNo());
                tls.setPoint(test.getPoint());
                list.add(tls);
            }

            if (list.isEmpty()) {
                req.setAttribute("message", "成績情報が存在しませんでした");
            } else {
                req.setAttribute("students", list);
            }

            req.getRequestDispatcher("test_list_student.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "学生別成績検索中にエラーが発生しました。");
            try {
                req.getRequestDispatcher("error.jsp").forward(req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
