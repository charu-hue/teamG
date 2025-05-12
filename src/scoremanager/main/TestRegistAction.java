package scoremanager.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            // セッションからログイン中の教師を取得
            Teacher teacher = (Teacher) req.getSession().getAttribute("user");
            School school = teacher.getSchool();

            // 各種マスタデータを取得（入学年度、クラス、科目など）
            List<Integer> entYearList = new StudentDao().getEntYearList(school);
            List<String> classNumList = new StudentDao().getClassNumList(school);
            List<Subject> subjectList = new SubjectDao().getAll(school);

            // リクエストに設定（検索フォーム用）
            req.setAttribute("entYearList", entYearList);
            req.setAttribute("classNumList", classNumList);
            req.setAttribute("subjectList", subjectList);

            // 検索条件を受け取ったか確認（初回アクセスかどうか）
            String entYearStr = req.getParameter("entYear");
            String classNum = req.getParameter("classNum");
            String subjectCd = req.getParameter("subject");
            String noStr = req.getParameter("no");

            if (entYearStr != null && classNum != null && subjectCd != null && noStr != null) {
                // 検索条件がすべて揃っている場合 → 成績情報を取得して表示
                int entYear = Integer.parseInt(entYearStr);
                int no = Integer.parseInt(noStr);

                Subject subject = new Subject();
                subject.setCd(subjectCd);
                subject.setSchool(school);

                TestDao dao = new TestDao();
                List<Test> testList = dao.filter(entYear, classNum, subject, no, school);

                // 科目名表示用に取得
                String subjectName = "";
                for (Subject s : subjectList) {
                    if (s.getCd().equals(subjectCd)) {
                        subjectName = s.getName();
                        break;
                    }
                }

                req.setAttribute("testList", testList);
                req.setAttribute("subjectName", subjectName);
                req.setAttribute("entYear", entYear);
                req.setAttribute("classNum", classNum);
                req.setAttribute("subjectCd", subjectCd);
                req.setAttribute("no", no);
            }

            // 画面遷移
            req.getRequestDispatcher("test_regist.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "画面の表示に失敗しました。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }
}
