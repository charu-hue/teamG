package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 現在のセッションを取得。セッションとは、ユーザーごとの一時的な保存領域のこと。
		HttpSession session = req.getSession();

		// セッションからログイン中の教師の情報を取得。
		// ログイン処理で"ユーザー"として保存されていたTeacher型のオブジェクト。
		Teacher teacher = (Teacher) session.getAttribute("user");

		// フォームから送信される入力項目の初期化
		String entYearStr = ""; // 入学年度（文字列で受け取り、あとでintに変換）
		String classNum = "";   // クラス番号（例：A組、B組など）
		String isAttendStr = ""; // 在学中かどうかの値（チェックボックス）

		// 入学年度（数値型）と在学フラグ（boolean型）の初期化
		int entYear = 0;
		boolean isAttend = false;

		// 結果として画面に表示する学生リスト
		List<Student> students = null;

		// 現在の年月日から「今年の年」を取得
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();

		// DAO（データアクセスオブジェクト）を使ってデータベースとやり取りする準備
		StudentDao sDao = new StudentDao();
		ClassNumDao cNumDao = new ClassNumDao();

		// 入力チェックやバリデーションエラーのメッセージを入れるマップ
		Map<String, String> errors = new HashMap<>();

		// リクエスト（フォーム）から送信されたパラメータを取得
		entYearStr = req.getParameter("f1"); // 入学年度
		classNum = req.getParameter("f2");   // クラス
		isAttendStr = req.getParameter("f3"); // 在学チェック（on もしくは null）

		// 入学年度が入力されていた場合、数値に変換する
		if (entYearStr != null) {
			entYear = Integer.parseInt(entYearStr);
		}

		// 入学年度のセレクトボックス用データを作成（例：2025〜2015年）
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		// 現在の教師が所属する学校のクラス一覧を取得
		// クラス番号の選択肢を作るために必要（例：A, B, Cクラスなど）
		List<String> list = cNumDao.filter(teacher.getSchool());

		// 条件によって学生の検索方法を切り替える
		if (entYear != 0 && !classNum.equals("0")) {
			// 入学年度とクラス両方が指定されている → もっとも絞り込まれた検索
			students = sDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
		} else if (entYear != 0 && classNum.equals("0")) {
			// 入学年度のみ指定され、クラスは「指定なし」 → 年度単位の検索
			students = sDao.filter(teacher.getSchool(), entYear, isAttend);
		} else if ((entYear == 0 && classNum == null) || (entYear == 0 && classNum.equals("0"))) {
			// 両方未指定 → 学校内すべての学生を対象に検索
			students = sDao.filter(teacher.getSchool(), isAttend);
		} else {
			// クラスが指定されているのに入学年度が未指定 → エラー扱い
			errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
			req.setAttribute("errors", errors);

			// エラーがあっても最低限の学生リストは表示する
			students = sDao.filter(teacher.getSchool(), isAttend);
		}

		// 検索条件を画面に保持するため、リクエストスコープに再設定
		req.setAttribute("f1", entYear);
		req.setAttribute("f2", classNum);

		// 「在学中」にチェックが入っていた場合はフラグをtrueにして画面に反映
		if (isAttendStr != null) {
			isAttend = true;
			req.setAttribute("f3", isAttendStr);
		}

		// 取得した学生リストを画面表示用にリクエストスコープへ格納
		req.setAttribute("students", students);

		// クラス一覧と入学年度一覧も画面で表示するためにリクエストへ格納
		req.setAttribute("class_num_set", list);
		req.setAttribute("ent_year_set", entYearSet);

		// 最終的にstudent_list.jsp（学生一覧ページ）へ画面遷移
		req.getRequestDispatcher("student_list.jsp").forward(req, res);
	}
}
