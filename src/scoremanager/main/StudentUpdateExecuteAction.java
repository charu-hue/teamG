package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

/**
 * 学生情報の更新処理を実行するアクションクラス。
 * フォームから送信された情報をもとに、学生情報を更新する。
 */
public class StudentUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 学生情報DAOを準備
        StudentDao sDao = new StudentDao();

        // リクエストパラメータから学生情報を取得
        int entYear = Integer.parseInt(req.getParameter("f1"));       // 入学年度
        String no = req.getParameter("f2");                           // 学生番号
        String name = req.getParameter("f3");                         // 氏名
        String classNum = req.getParameter("f4");                     // クラス番号

        // 「在学中」チェックボックスがチェックされていれば true、されていなければ false
        boolean isAttend = req.getParameter("f5") != null;

        // 学校情報は教師から取得
        School school = teacher.getSchool();

        // 更新対象の学生オブジェクトを生成・設定
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(isAttend);
        student.setSchool(school);

        // 学生情報を保存（新規・更新どちらにも対応した save メソッドを使用）
        boolean result = sDao.save(student);

        // 更新処理の結果（成功／失敗）をリクエストに保存
        req.setAttribute("update_result", result);

        // 完了画面（student_update_done.jsp）へ遷移
        req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
    }
}
