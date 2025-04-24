package scoremanager.main;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.Action;

// このクラスはログアウト処理を担当するアクションです。
// ユーザーのセッションを破棄（ログアウト）し、ログアウト完了ページへ遷移します。
public class LogoutAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
			throws Exception {

		// 遷移先ページのURLを格納する変数
		String url = "";

		// 現在のセッションを取得
		// ここではログイン状態かどうかを判定し、ログアウト処理を行うために使用
		HttpSession session = req.getSession();

		// セッションに"user"という名前の属性（ログイン中のユーザー情報）が存在するか確認
		if (session.getAttribute("user") != null) {

			// 存在する場合（＝ログイン中）、セッションを破棄してログアウト処理を実行
			// session.invalidate() は、セッションに保存されたすべてのデータを削除します
			session.invalidate();
		}

		// ログアウト完了ページ（logout.jsp）に遷移するようにURLを設定
		url = "logout.jsp";

		// 設定されたURLに画面遷移（フォワード）を実行
		// フォワードとは、サーバー内部で別のページに処理を渡す仕組みです（リダイレクトではない）
		req.getRequestDispatcher(url).forward(req, res);
	}
}
