package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

/**
 * 学生情報の変更画面を表示するアクションクラス。
 * 学生番号をもとに該当する学生情報を取得し、変更フォームに表示する。
 */
public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン中の教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // リクエストから学生番号を取得
        String no = req.getParameter("f2");  // 学生番号はパラメータ名 f2 で渡される

        if (no != null && !no.isEmpty()) {
            // 学生情報取得用DAOを初期化
            StudentDao sDao = new StudentDao();

            // 学生番号から学生情報を取得
            Student student = sDao.get(no);

            // 学生情報が存在する場合、リクエスト属性に情報を設定
            if (student != null) {
                req.setAttribute("f1", student.getEntYear());     // 入学年度
                req.setAttribute("f2", student.getNo());          // 学生番号
                req.setAttribute("f3", student.getName());        // 氏名
                req.setAttribute("f4", student.getClassNum());    // クラス
                req.setAttribute("f5", student.isAttend());       // 在学中フラグ
            }
        }

        // 学校に対応するクラスリストを取得し、フォームのクラス選択肢に使用
        ClassNumDao cNumDao = new ClassNumDao();
        List<String> classList = cNumDao.filter(teacher.getSchool());

        // クラス選択肢をリクエストに設定
        req.setAttribute("class_num_set", classList);

        // 学生変更画面へフォワード（student_update.jsp）
        req.getRequestDispatcher("student_update.jsp").forward(req, res);
    }
}
