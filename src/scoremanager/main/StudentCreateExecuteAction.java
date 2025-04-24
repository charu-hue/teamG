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
 * 学生登録処理を実行するアクションクラス。
 * フォームから送信された学生情報をデータベースに保存し、完了画面に遷移する。
 */
public class StudentCreateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションからログイン中の教師情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		// 学生データアクセス用DAOを初期化
		StudentDao sDao = new StudentDao();

		// リクエストパラメータ（フォーム入力値）から学生情報を取得
		int entYear = Integer.parseInt(req.getParameter("f1")); // 入学年度
		String no = req.getParameter("f2");                     // 学生番号
		String name = req.getParameter("f3");                   // 氏名
		String classNum = req.getParameter("f4");               // クラス番号
		boolean isAttend = true;                                // 登録時は在学中に固定
		School school = teacher.getSchool();                    // 教師の所属学校を取得

		// デバッグ用出力（後で削除してもOK）
		System.out.println(no + name + classNum);

		// 学生オブジェクトを生成し、各プロパティに値を設定
		Student stu = new Student();
		stu.setNo(no);
		stu.setName(name);
		stu.setEntYear(entYear);
		stu.setClassNum(classNum);
		stu.setAttend(isAttend);
		stu.setSchool(school);

		// 学生情報をデータベースに保存
		boolean save = sDao.save(stu);

		// 保存結果をリクエストスコープに格納（確認画面などで使用可能）
		req.setAttribute("class_num", save); // ★保存成功の可否を返す変数名としては若干わかりにくいかも？

		// 登録完了画面に遷移
		req.getRequestDispatcher("student_create_done.jsp").forward(req, res);
	}
}
