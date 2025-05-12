package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import bean.TestListSubject;
import dao.SubjectDao;
import dao.TestDao;

public class TestListSubjectExecuteAction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");
            School school = teacher.getSchool();

            // パラメータ取得とバリデーション
            String f1 = req.getParameter("f1"); // 年度
            String f2 = req.getParameter("f2"); // クラス
            String f3 = req.getParameter("f3"); // 科目名
            req.setAttribute("f1", f1);
            req.setAttribute("f2", f2);
            req.setAttribute("f3", f3);

            if (f1 == null || f2 == null || f3 == null || f1.equals("0") || f2.equals("0") || f3.equals("0")) {
                req.setAttribute("error", "入学年度とクラスと科目を選択してください。");
                forward(req, res);
                return;
            }

            int entYear = Integer.parseInt(f1);
            String classNum = f2;
            String subjectName = f3;

            SubjectDao subjectDao = new SubjectDao();
            List<Subject> subjectList = subjectDao.getAll(school);
            Subject subject = subjectList.stream()
                .filter(s -> s.getName().equals(subjectName))
                .findFirst()
                .orElse(null);

            if (subject == null) {
                req.setAttribute("message", "指定された科目が見つかりませんでした。");
                forward(req, res);
                return;
            }

            // 成績取得（複数回のテストを想定）
            TestDao testDao = new TestDao();
            List<Test> allTests = new ArrayList<>();
            int[] testNums = {1, 2, 3}; // 想定されるテスト回数（必要に応じて調整）

            for (int num : testNums) {
                List<Test> partialTests = testDao.filter(entYear, classNum, subject, num, school);
                allTests.addAll(partialTests);
            }

            if (allTests.isEmpty()) {
                req.setAttribute("message", "学生情報が存在しませんでした");
                forward(req, res);
                return;
            }

            // 学生ごとにTestListSubjectへ変換
            Map<String, TestListSubject> map = new LinkedHashMap<>();

            for (Test test : allTests) {
                String studentNo = test.getStudent().getNo();
                TestListSubject tls = map.getOrDefault(studentNo, new TestListSubject());

                tls.setEntYear(entYear);
                tls.setStudentNo(studentNo);
                tls.setClassNum(classNum);

                if (tls.getPoints() == null) {
                    tls.setPoints(new HashMap<>());
                }
                tls.getPoints().put(test.getNo(), test.getPoint());

                map.put(studentNo, tls);
            }

            req.setAttribute("list", new ArrayList<>(map.values()));
            forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("成績情報取得時にエラーが発生しました", e);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("test_list_subject.jsp");
        dispatcher.forward(req, res);
    }
}
