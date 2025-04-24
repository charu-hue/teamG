package scoremanager.main;

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
 * フォームから送信された科目情報をデータベースに保存し、完了画面に遷移する。
 */
public class SubjectCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 教師の所属学校を取得
        School school = teacher.getSchool();

        // フォームから送信されたデータを取得
        String cd = req.getParameter("f1");     // 科目コード
        String name = req.getParameter("f2");   // 科目名

        // 科目オブジェクトを生成し、値を設定
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(school);

        // 科目情報をデータベースに保存
        SubjectDao sDao = new SubjectDao();
        boolean save = sDao.save(subject);

        // 保存結果をリクエストに設定
        req.setAttribute("save_result", save);

        // 完了画面へフォワード
        req.getRequestDispatcher("/scoremanager/main/subject_create_done.jsp").forward(req, res);
    }
}
