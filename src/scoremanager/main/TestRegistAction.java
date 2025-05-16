package scoremanager.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();
        TestDao testDao = new TestDao();

        List<Integer> entYearList = studentDao.getEntYearList(school);
        List<String> classNumList = studentDao.getClassNumList(school);
        List<Subject> subjectList = subjectDao.getAll(school);

        req.setAttribute("entYearList", entYearList);
        req.setAttribute("classNumList", classNumList);
        req.setAttribute("subjectList", subjectList);

        String entYearStr = req.getParameter("entYear");
        String classNum = req.getParameter("classNum");
        String subjectCd = req.getParameter("subject");
        String noStr = req.getParameter("no");

        if (entYearStr != null && classNum != null && subjectCd != null && noStr != null) {
            int entYear = Integer.parseInt(entYearStr);
            int no = Integer.parseInt(noStr);

            Subject subject = new Subject();
            subject.setCd(subjectCd);
            subject.setSchool(school);

            List<Student> studentList = studentDao.filter(school, entYear, classNum, true);
            List<Test> existingTests = testDao.filter(entYear, classNum, subject, no, school);

            Map<String, Test> testMap = new HashMap<>();
            for (Test t : existingTests) {
                testMap.put(t.getStudent().getNo(), t);
            }

            List<Test> testList = new ArrayList<>();
            for (Student student : studentList) {
                Test test = testMap.getOrDefault(student.getNo(), new Test());
                test.setStudent(student);
                test.setClassNum(student.getClassNum());
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);
                testList.add(test);
            }

            String subjectName = subjectList.stream()
                .filter(s -> s.getCd().equals(subjectCd))
                .map(Subject::getName)
                .findFirst()
                .orElse("");

            req.setAttribute("testList", testList);
            req.setAttribute("subjectName", subjectName);
            req.setAttribute("entYear", entYear);
            req.setAttribute("classNum", classNum);
            req.setAttribute("subjectCd", subjectCd);
            req.setAttribute("no", no);
        }


        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}
