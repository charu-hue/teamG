package scoremanager.main;

// 必要なクラスのインポート
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tool.Action;

/**
 * メニュー画面への遷移を担当するアクションクラス。
 * このクラスは、リクエストを受け取ったら menu.jsp に処理を転送します。
 */
public class MenuAction extends Action {

	/**
	 * execute メソッドは、リクエストを処理し、menu.jsp にフォワードする。
	 *
	 * @param req  クライアントから送信された HTTP リクエスト情報
	 * @param res  サーバーからクライアントへの HTTP レスポンス情報
	 * @throws Exception  例外が発生した場合にスローされる
	 */
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// "menu.jsp" へ画面遷移（フォワード）を行う。
		// このメソッドが呼び出されると、ユーザーはメニュー画面に遷移する。
		req.getRequestDispatcher("menu.jsp").forward(req, res);
	}
}
