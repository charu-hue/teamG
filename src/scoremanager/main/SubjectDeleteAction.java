//package scoremanager.main;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import bean.School;
//import bean.Subject;
//import dao.SubjectDao;
//import tool.Action;
//
//public class SubjectDeleteAction extends Action {
//
//    @Override
//    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//        // ログイン中の学校情報
//        School school = (School) req.getSession().getAttribute("school");
//
//        // リクエストパラメータから科目コードを取得
//        String cd = req.getParameter("cd");
//
//        String page = "subject_list.jsp";  // デフォルト：失敗時は一覧へ
//
//        if (cd != null && !cd.isEmpty() && school != null) {
//            SubjectDao dao = new SubjectDao();
//            Subject subject = dao.get(cd, school);
//
//            if (subject != null) {
//                req.setAttribute("subject", subject);
//                page = "subject_delete.jsp";  // 成功時は確認画面へ
//            }
//        }
//
//        // 画面遷移
//        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
//        dispatcher.forward(req, res);
//    }
//}

package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import tool.Action;

/**
 * 科目情報の削除確認画面を表示するアクションクラス。
 * 科目コードをもとに該当する科目情報を取得し、削除確認画面に表示する。
 */
public class SubjectDeleteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // リクエストパラメータから科目コードを取得
        String cd = req.getParameter("cd");

        // デフォルト遷移先
        String page = "subject_list.jsp";

        if (cd != null && !cd.isEmpty() && teacher != null) {
            SubjectDao dao = new SubjectDao();
            Subject subject = dao.get(cd, teacher.getSchool());

            if (subject != null) {
                req.setAttribute("subject", subject);
                page = "subject_delete.jsp";
            }
        }

        // 画面遷移
        req.getRequestDispatcher(page).forward(req, res);
    }
}
