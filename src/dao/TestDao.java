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
     * 成績情報の取得（studentテーブルとJOINし、ent_yearで絞り込み）
     */
    public List<Test> filter(int entYear, String classNum, Subject subject, int no, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            String sql = "SELECT t.*, s.ent_year, s.name AS student_name " +
                         "FROM test t " +
                         "JOIN student s ON t.student_no = s.no AND t.school_cd = s.school_cd " +
                         "WHERE s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND t.no = ? AND t.school_cd = ? " +
                         "ORDER BY s.no";

            ps = connection.prepareStatement(sql);
            ps.setInt(1, entYear);
            ps.setString(2, classNum);
            ps.setString(3, subject.getCd());
            ps.setInt(4, no);
            ps.setString(5, school.getCd());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Test test = new Test();

                Student student = new Student();
                student.setNo(rs.getString("student_no"));
                student.setEntYear(rs.getInt("ent_year"));
                student.setName(rs.getString("student_name"));
                student.setClassNum(rs.getString("class_num"));
                student.setSchool(school);

                test.setStudent(student);
                test.setClassNum(rs.getString("class_num"));
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);
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
     * 成績の保存（H2対応：DELETE→INSERT）
     */
    public boolean save(List<Test> list) throws Exception {
        Connection connection = getConnection();
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;

        try {
            connection.setAutoCommit(false);

            String deleteSql = "DELETE FROM test WHERE student_no = ? AND subject_cd = ? AND school_cd = ? AND no = ?";
            deleteStmt = connection.prepareStatement(deleteSql);

            for (Test t : list) {
                deleteStmt.setString(1, t.getStudent().getNo());
                deleteStmt.setString(2, t.getSubject().getCd());
                deleteStmt.setString(3, t.getSchool().getCd());
                deleteStmt.setInt(4, t.getNo());
                deleteStmt.addBatch();
            }
            deleteStmt.executeBatch();

            String insertSql = "INSERT INTO test (student_no, class_num, subject_cd, no, point, school_cd) " +
                               "VALUES (?, ?, ?, ?, ?, ?)";
            insertStmt = connection.prepareStatement(insertSql);

            for (Test t : list) {
                insertStmt.setString(1, t.getStudent().getNo());
                insertStmt.setString(2, t.getClassNum());
                insertStmt.setString(3, t.getSubject().getCd());
                insertStmt.setInt(4, t.getNo());
                insertStmt.setInt(5, t.getPoint());
                insertStmt.setString(6, t.getSchool().getCd());
                insertStmt.addBatch();
            }

            insertStmt.executeBatch();
            connection.commit();
            return true;

        } catch (Exception e) {
            if (connection != null) connection.rollback();
            throw e;
        } finally {
            if (deleteStmt != null) try { deleteStmt.close(); } catch (SQLException e) { throw e; }
            if (insertStmt != null) try { insertStmt.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) { throw e; }
        }
    }

    /**
     * 学生別の成績情報を取得
     */
    public List<Test> getByStudent(Student student) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement ps = null;

        try {
            String sql = "SELECT t.*, sub.cd AS subject_cd, sub.name AS subject_name " +
                         "FROM test t " +
                         "JOIN subject sub ON t.subject_cd = sub.cd AND t.school_cd = sub.school_cd " +
                         "WHERE t.student_no = ? AND t.school_cd = ? " +
                         "ORDER BY t.subject_cd, t.no";

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
