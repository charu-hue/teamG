package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目情報の更新処理を実行するアクションクラス。
 * フォームから送信された情報をもとに、科目情報を更新する。
 */
public class SubjectUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // フォームから送信されたパラメータを取得
        String cd = req.getParameter("f1");      // 科目コード
        String name = req.getParameter("f2");    // 科目名

        // 科目オブジェクトを生成し、値を設定
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(teacher.getSchool());

        // DAOを使って更新処理を実行
        SubjectDao dao = new SubjectDao();
        boolean result = dao.update(subject);  // ← save → update に修正

        // 結果をリクエスト属性に設定
        req.setAttribute("update_result", result);

        // 完了画面へフォワード
        req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
    }
}
