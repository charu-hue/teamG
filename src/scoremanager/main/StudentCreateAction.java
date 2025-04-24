// 学生登録画面を表示するためのアクションクラス

package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;

/**
 * 学生登録画面（student_create.jsp）を表示するためのアクション。
 * ログイン中の教員情報を元に、選択可能なクラスや入学年度のリストを作成し、画面に渡す。
 */
public class StudentCreateAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// セッションからログイン中の教師情報を取得
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		// 現在の年を取得（入学年度候補作成に使用）
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();

		// クラス番号を取得するDAOを初期化
		ClassNumDao cNumDao = new ClassNumDao();

		// 入学年度の候補リストを作成（現在の年を中心に前後10年）
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 10; i++) {
			entYearSet.add(i);
		}

		// 教員の所属学校に紐づくクラス番号一覧を取得
		List<String> list = cNumDao.filter(teacher.getSchool());

		// 入学年度とクラス番号の候補リストをリクエストに保存
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", list);

		// 学生登録画面へフォワード
		req.getRequestDispatcher("student_create.jsp").forward(req, res);
	}
}
