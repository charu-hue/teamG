package scoremanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.TestDao;

public class TestRegistExecuteAction extends HttpServlet {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");

            // ① 入力値取得
            int entYear = Integer.parseInt(req.getParameter("entYear"));
            String classNum = req.getParameter("classNum");
            String subjectCd = req.getParameter("subjectCd");
            int no = Integer.parseInt(req.getParameter("no"));

            String[] studentNos = req.getParameterValues("studentNo");
            String[] points = req.getParameterValues("point");

            // ② セッションから教師の学校情報を取得
            Teacher teacher = (Teacher) req.getSession().getAttribute("user");
            School school = teacher.getSchool();

            // ③ 科目インスタンス作成
            Subject subject = new Subject();
            subject.setCd(subjectCd);
            subject.setSchool(school);

            // ④ 入力バリデーションとオブジェクト生成
            List<Test> testList = new ArrayList<>();
            Map<String, String> errorMessages = new HashMap<>();

            for (int i = 0; i < studentNos.length; i++) {
                String studentNo = studentNos[i];
                String pointStr = points[i];
                Test test = new Test();

                // 学生・基本情報設定
                Student student = new Student();
                student.setNo(studentNo);
                student.setEntYear(entYear);
                student.setClassNum(classNum);
                student.setSchool(school);

                test.setStudent(student);
                test.setClassNum(classNum);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);

                try {
                    int point = Integer.parseInt(pointStr);
                    if (point < 0 || point > 100) {
                        errorMessages.put(studentNo, "0～100の範囲で入力してください");
                    } else {
                        test.setPoint(point);
                    }
                } catch (NumberFormatException e) {
                    errorMessages.put(studentNo, "数値を入力してください");
                }

                testList.add(test);
            }

            // ⑤ エラーがあれば元の画面に戻る
            if (!errorMessages.isEmpty()) {
                req.setAttribute("testList", testList);
                req.setAttribute("errorMessages", errorMessages);
                req.setAttribute("entYear", entYear);
                req.setAttribute("classNum", classNum);
                req.setAttribute("subjectCd", subjectCd);
                req.setAttribute("no", no);
                req.getRequestDispatcher("/view/testRegist.jsp").forward(req, res);
                return;
            }

            // ⑥ 正常処理 → 登録実行
            TestDao dao = new TestDao();
            dao.save(testList);

            // 完了メッセージ
            req.setAttribute("message", "登録が完了しました");
            req.setAttribute("entYear", entYear);
            req.setAttribute("classNum", classNum);
            req.setAttribute("subjectCd", subjectCd);
            req.setAttribute("no", no);
            req.getRequestDispatcher("/TestRegistAction").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "登録処理に失敗しました。");
            req.getRequestDispatcher("/view/error.jsp").forward(req, res);
        }
    }
}
