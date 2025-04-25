package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目情報の変更画面を表示するアクションクラス。
 * 科目コードをもとに該当する科目情報を取得し、変更フォームに表示する。
 */
public class SubjectUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // リクエストから科目コードを取得
        String cd = req.getParameter("f1");  // f1 = 科目コード

        if (cd != null && !cd.isEmpty()) {
            SubjectDao sDao = new SubjectDao();

            // 教師の所属学校とコードから科目情報を取得
            Subject subject = sDao.get(cd, teacher.getSchool());

            if (subject != null) {
                // 科目情報をリクエスト属性に設定
                req.setAttribute("f1", subject.getCd());
                req.setAttribute("f2", subject.getName());
            }
        }

        // 変更フォームを表示するJSPへフォワード（相対パスでOK）
        req.getRequestDispatcher("subject_update.jsp").forward(req, res);
    }
}
