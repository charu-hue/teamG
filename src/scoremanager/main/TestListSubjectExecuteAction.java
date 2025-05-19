package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import tool.Action;

public class TestListSubjectExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            HttpSession session = req.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");
            School school = teacher.getSchool();

            String f1 = req.getParameter("f1"); // 入学年度
            String f2 = req.getParameter("f2"); // クラス
            String f3 = req.getParameter("f3"); // 科目名
            req.setAttribute("f1", f1);
            req.setAttribute("f2", f2);
            req.setAttribute("f3", f3);

            if (f1 == null || f2 == null || f3 == null || f1.equals("0") || f2.equals("0") || f3.equals("0")) {
                req.setAttribute("error", "入学年度とクラスと科目を選択してください。");
                req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
                return;
            }

            int entYear = Integer.parseInt(f1);
            String classNum = f2;
            String subjectName = f3;

            // 科目コード取得
            SubjectDao subjectDao = new SubjectDao();
            List<Subject> subjectList = subjectDao.getAll(school);
            Subject subject = subjectList.stream()
                    .filter(s -> s.getName().equals(subjectName))
                    .findFirst()
                    .orElse(null);

            if (subject == null) {
                req.setAttribute("message", "指定された科目が見つかりませんでした。");
                req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
                return;
            }

            // 成績情報の取得（noは条件に含まない）
            TestDao testDao = new TestDao();
            List<Test> allTests = testDao.filter(entYear, classNum, subject, school);

            if (allTests.isEmpty()) {
                req.setAttribute("message", "成績情報が存在しませんでした。");
                req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
                return;
            }

            // 学生ごとにまとめる
            Map<String, TestListSubject> map = new LinkedHashMap<>();
            for (Test test : allTests) {
                String studentNo = test.getStudent().getNo();

                TestListSubject tls = map.getOrDefault(studentNo, new TestListSubject());
                tls.setEntYear(entYear);
                tls.setStudentNo(studentNo);
                tls.setClassNum(classNum);
                tls.setStudentName(test.getStudent().getName());

                if (tls.getPoints() == null) {
                    tls.setPoints(new HashMap<>());
                }
                tls.getPoints().put(String.valueOf(test.getNo()), test.getPoint());


                map.put(studentNo, tls);
            }

            req.setAttribute("students", new ArrayList<>(map.values()));
            req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "成績情報取得時にエラーが発生しました。");
            try {
                req.getRequestDispatcher("error.jsp").forward(req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
