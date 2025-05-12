package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    /**
     * 得点の検索処理
     */
    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            String sql = "SELECT * FROM test WHERE ent_year=? AND class_num=? AND subject_id=? AND no=? AND school_id=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, entYear);
            ps.setString(2, classNum);
            ps.setString(3, subject.getCd());     // ← Subject.cd
            ps.setInt(4, num);
            ps.setString(5, school.getCd());      // ← School.cd

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Test test = new Test();

                // Studentを生成し、基本情報をセット
                Student student = new Student();
                student.setNo(rs.getString("student_id")); // フィールド名は student_id を仮定
                // 必要であれば student.setName(...) や他項目も追加

                // テスト情報を構成
                test.setStudent(student);
                test.setClassNum(classNum);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(num);
                test.setPoint(rs.getInt("point"));

                list.add(test);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return list;
    }

    /**
     * 得点の保存処理（INSERT or UPDATE）
     */
    public boolean save(List<Test> list) throws Exception {
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO test (student_id, class_num, subject_id, no, point, ent_year, school_id) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                       + "ON DUPLICATE KEY UPDATE point = VALUES(point)";
            ps = connection.prepareStatement(sql);

            for (Test t : list) {
                ps.setString(1, t.getStudent().getNo());           // ← Student.no
                ps.setString(2, t.getClassNum());                  // ← String
                ps.setString(3, t.getSubject().getCd());           // ← Subject.cd
                ps.setInt(4, t.getNo());                           // ← 回数
                ps.setInt(5, t.getPoint());                        // ← 点数
                ps.setInt(6, t.getStudent().getEntYear());         // ← Student.entYear
                ps.setString(7, t.getSchool().getCd());            // ← School.cd

                ps.addBatch();
            }

            ps.executeBatch();
            return true;

        } catch (Exception e) {
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }
    }
    public List<Test> getByStudent(Student student) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            String sql = "SELECT t.*, sub.cd AS subject_cd, sub.name AS subject_name  FROM test t JOIN subject sub ON t.subject_id = sub.cd AND t.school_id = sub.school_cd WHERE t.student_id = ? AND t.school_id = ? ORDER BY t.subject_id, t.no";


            ps = connection.prepareStatement(sql);
            ps.setString(1, student.getNo());
            ps.setString(2, student.getSchool().getCd());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Test test = new Test();
                test.setStudent(student);
                test.setClassNum(rs.getString("class_num"));
                test.setSchool(student.getSchool());
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));

                Subject subject = new Subject();
                subject.setCd(rs.getString("subject_cd"));
                subject.setName(rs.getString("subject_name"));
                subject.setSchool(student.getSchool());
                test.setSubject(subject);

                list.add(test);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return list;
    }

}
