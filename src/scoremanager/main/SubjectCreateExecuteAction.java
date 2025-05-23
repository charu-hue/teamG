package scoremanager.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目登録処理を実行するアクションクラス。
 * 入力値のバリデーションと重複チェックを行い、登録処理またはエラー表示へ遷移。
 */
public class SubjectCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        String cd = req.getParameter("f1");   // 科目コード
        String name = req.getParameter("f2"); // 科目名

        // 入力値を保持
        req.setAttribute("cd", cd);
        req.setAttribute("name", name);

        Map<String, String> errors = new HashMap<>();

        // 入力チェック
        if (cd == null || cd.trim().isEmpty()) {
            errors.put("f1", "科目コードを入力してください");
        }
        if (name == null || name.trim().isEmpty()) {
            errors.put("f2", "科目名を入力してください");
        }

        SubjectDao sDao = new SubjectDao();

        // 重複チェック（同じ学校＋コード）
        if (cd != null && !cd.isEmpty() && sDao.get(cd, school) != null) {
            errors.put("f1", "この科目コードは既に存在しています");
        }

        // エラーがある場合、登録画面に戻す
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("subject_create.jsp").forward(req, res);
            return;
        }

        // 登録処理
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(school);

        sDao.insert(subject);

        // 完了後、一覧へリダイレクト
        res.sendRedirect("SubjectList.action");
    }
}
