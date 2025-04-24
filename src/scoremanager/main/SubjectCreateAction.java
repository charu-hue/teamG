// 科目登録画面を表示するためのアクションクラス

package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import tool.Action;

/**
 * 科目登録画面（subject_create.jsp）を表示するためのアクション。
 * ログイン中の教員情報を利用して、科目登録画面に遷移する。
 * 画面には動的なデータは渡さないが、将来的に学校情報などを渡す拡張も可能。
 */
public class SubjectCreateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 必要に応じて teacher 情報を画面に渡すこともできる（現状は未使用）
        // req.setAttribute("teacher", teacher);

        // 科目登録画面へフォワード
        req.getRequestDispatcher("subject_create.jsp").forward(req, res);
    }
}
