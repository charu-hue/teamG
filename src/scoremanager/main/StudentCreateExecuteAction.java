package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 入力パラメータ取得
        String f1 = req.getParameter("f1"); // 入学年度
        String f2 = req.getParameter("f2"); // 学生番号
        String f3 = req.getParameter("f3"); // 氏名
        String f4 = req.getParameter("f4"); // クラス

        // 入力値を保持（JSP再表示用）
        req.setAttribute("no", f2);
        req.setAttribute("name", f3);
        req.setAttribute("entYearList", new StudentDao().getEntYearList(school));
        req.setAttribute("class_num_set", new StudentDao().getClassNumList(school));

        Map<String, String> errors = new HashMap<>();

        // バリデーション
        if (f1 == null || f1.equals("0")) {
            errors.put("f1", "入学年度を選択してください");
        }
        if (f4 == null || f4.equals("0")) {
            errors.put("f4", "クラスを選択してください");
        }
        if (f2 == null || f2.isEmpty()) {
            errors.put("f2", "学生番号を入力してください");
        }
        if (f3 == null || f3.isEmpty()) {
            errors.put("f3", "氏名を入力してください");
        }

        // エラーがあればフォームに戻る
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

        // 学生オブジェクト生成
        Student student = new Student();
        student.setNo(f2);
        student.setName(f3);
        student.setEntYear(Integer.parseInt(f1));
        student.setClassNum(f4);
        student.setAttend(true);
        student.setSchool(school);

        // 登録実行（重複チェック付き）
        StudentDao sDao = new StudentDao();
        boolean success = sDao.insertIfNotExists(student);

        if (!success) {
            errors.put("f2", "この学生番号は既に存在しています");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

        // 正常に登録できたら一覧にリダイレクト
        res.sendRedirect("StudentList.action");
    }
}
