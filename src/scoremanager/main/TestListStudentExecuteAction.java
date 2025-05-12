package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.Test;
import bean.TestListStudent;
import dao.StudentDao;
import dao.TestDao;

public class TestListStudentExecuteAction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String studentNo = req.getParameter("f4"); // 学生番号
            req.setAttribute("no", studentNo); // 入力保持用

            if (studentNo == null || studentNo.trim().isEmpty()) {
                req.setAttribute("error", "学生番号を入力してください。");
                forward(req, res);
                return;
            }

            // 学生情報取得
            StudentDao studentDao = new StudentDao();
            Student student = studentDao.get(studentNo);

            if (student == null) {
                req.setAttribute("error", "該当する学生が見つかりません。");
                forward(req, res);
                return;
            }

            // 成績情報の取得
            TestDao testDao = new TestDao();
            List<Test> testList = testDao.getByStudent(student);

            // 表示用に変換
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
                req.setAttribute("list", list);
            }

            forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("学生別成績検索中にエラーが発生しました。", e);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("test_list_student.jsp");
        dispatcher.forward(req, res);
    }
}
