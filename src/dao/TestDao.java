package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {
	private String baseSql = "SELECT * FROM test WHERE school_cd=?";

	public Test get(Student student, Subject subject, School school, int no) throws Exception {
		Test test = null;
		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			String sql = "SELECT * FROM test WHERE student_no=? AND subject_cd=? AND school_cd=? AND no=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, student.getNo());
			statement.setString(2, subject.getCd());
			statement.setString(3, school.getCd());
			statement.setInt(4, no);

			ResultSet rSet = statement.executeQuery();
			if (rSet.next()) {
				test = new Test();
				test.setStudent(student);
				test.setSubject(subject);
				test.setSchool(school);
				test.setNo(rSet.getInt("no"));
				test.setPoint(rSet.getInt("point"));
			}
		} finally {
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		}

		return test;
	}

	private List<Test> postFilter(ResultSet rSet, School school) throws Exception {
		List<Test> list = new ArrayList<>();
		StudentDao studentDao = new StudentDao();
		SubjectDao subjectDao = new SubjectDao();

		while (rSet.next()) {
			Test test = new Test();
			test.setNo(rSet.getInt("no"));
			test.setPoint(rSet.getInt("point"));
			test.setStudent(studentDao.get(rSet.getString("student_no")));
			test.setSubject(subjectDao.get(rSet.getString("subject_cd"), school));
			test.setSchool(school);
			list.add(test);
		}

		return list;
	}

	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
		List<Test> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;

		String sql = baseSql + " AND ent_year=? AND class_num=? AND subject_cd=? AND no=? ORDER BY student_no ASC";

		try {
			statement = connection.prepareStatement(sql);
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			statement.setString(3, classNum);
			statement.setString(4, subject.getCd());
			statement.setInt(5, num);

			rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} finally {
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		}

		return list;
	}

	public boolean save(List<Test> list) throws Exception {
		boolean result = true;
		for (Test test : list) {
			if (!save(test, null)) {
				result = false;
			}
		}
		return result;
	}

	public boolean save(Test test, Connection externalConnection) throws Exception {
		boolean useExternal = (externalConnection != null);
		Connection connection = useExternal ? externalConnection : getConnection();
		PreparedStatement statement = null;
		int count = 0;

		try {
			statement = connection.prepareStatement(
				"REPLACE INTO test (student_no, subject_cd, school_cd, no, score) VALUES (?, ?, ?, ?, ?)"
			);
			statement.setString(1, test.getStudent().getNo());
			statement.setString(2, test.getSubject().getCd());
			statement.setString(3, test.getSchool().getCd());
			statement.setInt(4, test.getNo());
			statement.setInt(5, test.getPoint());
			count = statement.executeUpdate();
		} finally {
			if (statement != null) statement.close();
			if (!useExternal && connection != null) connection.close();
		}

		return count > 0;
	}
}
