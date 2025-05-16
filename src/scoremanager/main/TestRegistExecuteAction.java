package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.TestDao;
import tool.Action;

public class TestRegistExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");

            // ① 入力値取得
            int entYear = Integer.parseInt(req.getParameter("entYear"));
            String classNum = req.getParameter("classNum");
            String subjectCd = req.getParameter("subjectCd");
            int no = Integer.parseInt(req.getParameter("no"));

            String[] studentNos = req.getParameterValues("studentNoList");
            String[] points = req.getParameterValues("pointList");

            // ② 教師・学校情報取得
            Teacher teacher = (Teacher) req.getSession().getAttribute("user");
            School school = teacher.getSchool();

            // ③ 科目設定
            Subject subject = new Subject();
            subject.setCd(subjectCd);
            subject.setSchool(school);

            List<Test> testList = new ArrayList<>();
            Map<String, String> errorMessages = new HashMap<>();

            for (int i = 0; i < studentNos.length; i++) {
                String studentNo = studentNos[i];
                String pointStr = points[i];

                Student student = new Student();
                student.setNo(studentNo);
                student.setEntYear(entYear);
                student.setClassNum(classNum);
                student.setSchool(school);

                Test test = new Test();
                test.setStudent(student);
                test.setClassNum(classNum);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);

                try {
                    if (pointStr != null && !pointStr.trim().isEmpty()) {
                        int point = Integer.parseInt(pointStr);
                        if (point < 0 || point > 100) {
                            errorMessages.put(studentNo, "0～100の範囲で入力してください");
                        } else {
                            test.setPoint(point);
                        }
                    } else {
                        errorMessages.put(studentNo, "点数を入力してください");
                    }
                } catch (NumberFormatException e) {
                    errorMessages.put(studentNo, "数値を入力してください");
                }

                testList.add(test);
            }

            // ④ エラーがある場合：元の画面へ戻す
            if (!errorMessages.isEmpty()) {
                req.setAttribute("testList", testList);
                req.setAttribute("errorMessages", errorMessages);
                req.setAttribute("entYear", entYear);
                req.setAttribute("classNum", classNum);
                req.setAttribute("subjectCd", subjectCd);
                req.setAttribute("no", no);
                req.getRequestDispatcher("test_regist.jsp").forward(req, res);
                return;
            }

            // ⑤ 成績の保存
            new TestDao().save(testList);

            // ⑥ 完了ページへ
            req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "登録処理に失敗しました。");
            req.getRequestDispatcher("error.jsp").forward(req, res);
        }
    }
}
