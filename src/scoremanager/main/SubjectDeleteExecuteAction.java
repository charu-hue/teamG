package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目情報の削除処理を実行するアクションクラス。
 * フォームから送信された科目コードをもとに、該当する科目を削除する。
 */
public class SubjectDeleteExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // フォームから渡された科目コードを取得
        String cd = req.getParameter("cd");

        if (cd != null && !cd.isEmpty() && teacher != null) {
            // 削除対象の科目オブジェクトを生成
            Subject subject = new Subject();
            subject.setCd(cd);
            subject.setSchool(teacher.getSchool());

            // DAOを使用して削除処理を実行
            SubjectDao dao = new SubjectDao();
            dao.delete(subject);
        }

        // 完了画面へフォワード
        req.getRequestDispatcher("subject_delete_done.jsp").forward(req, res);
    }
}
