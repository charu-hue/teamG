package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 入力取得
        String f1 = req.getParameter("f1"); // 入学年度
        String no = req.getParameter("f2");
        String name = req.getParameter("f3");
        String classNum = req.getParameter("f4");

        Map<String, String> errors = new HashMap<>();

        // バリデーション
        if (f1 == null || f1.equals("0")) {
            errors.put("f1", "入学年度を選択してください");
        }
        if (no == null || no.trim().isEmpty()) {
            errors.put("f2", "学生番号を入力してください");
        }
        if (name == null || name.trim().isEmpty()) {
            errors.put("f3", "氏名を入力してください");
        }
        if (classNum == null || classNum.equals("0")) {
            errors.put("f4", "クラスを選択してください");
        }

        // エラーあり → 入力画面に戻す
        if (!errors.isEmpty()) {
            // ±10年の入学年度リストを作成
            List<Integer> entYearList = new ArrayList<>();
            int year = LocalDate.now().getYear();
            for (int i = year - 10; i <= year + 9; i++) {
                entYearList.add(i);
            }

            // クラス一覧を取得
            ClassNumDao cDao = new ClassNumDao();
            List<String> classList = cDao.filter(school);

            req.setAttribute("ent_year_set", entYearList);
            req.setAttribute("class_num_set", classList);
            req.setAttribute("errors", errors);
            req.setAttribute("no", no);
            req.setAttribute("name", name);

            req.getRequestDispatcher("student_create.jsp").forward(req, res);
            return;
        }

        // 登録処理
        int entYear = Integer.parseInt(f1);
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(entYear);
        student.setClassNum(classNum);
        student.setAttend(true); // 登録時は在学中
        student.setSchool(school);

        StudentDao sDao = new StudentDao();
        sDao.insertIfNotExists(student); // 重複チェック付きで登録

        res.sendRedirect("StudentList.action");
    }
}
