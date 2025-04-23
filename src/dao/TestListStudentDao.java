package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {
	private String baseSql =
		"SELECT s.name AS subject_name, t.subject_cd, t.no, t.point " +
		"FROM test AS t " +
		"JOIN subject AS s ON t.subject_cd = s.cd " +
		"WHERE t.student_no = ? " +
		"ORDER BY t.no ASC";

	private List<TestListStudent> postFilter(ResultSet rSet) throws Exception {
		List<TestListStudent> list = new ArrayList<>();
		try {
			while (rSet.next()) {
				TestListStudent tls = new TestListStudent();
				tls.setSubjectName(rSet.getString("subject_name"));
				tls.setSubjectCd(rSet.getString("subject_cd"));
				tls.setNum(rSet.getInt("no"));
				tls.setPoint(rSet.getInt("point"));
				list.add(tls);
			}
		} catch (SQLException e) {
			throw e;
		}
		return list;
	}

	public List<TestListStudent> filter(Student student) throws Exception {
		List<TestListStudent> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;

		try {
			statement = connection.prepareStatement(baseSql);
			statement.setString(1, student.getNo());
			rSet = statement.executeQuery();
			list = postFilter(rSet);
		} finally {
			if (rSet != null) rSet.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		}

		return list;
	}
}
