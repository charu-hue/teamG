package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Subject;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {
	private String baseSql =
		"SELECT s.ent_year, s.class_num, s.no AS student_no, s.name AS student_name, " +
		"t.no AS test_no, r.point " +
		"FROM student s " +
		"JOIN result r ON s.no = r.student_no " +
		"JOIN test t ON r.test_no = t.no " +
		"WHERE s.school_cd = ? AND s.ent_year = ? AND s.class_num = ? AND t.subject_cd = ?";

	private List<TestListSubject> postFilter(ResultSet rSet) throws Exception {
		Map<String, TestListSubject> map = new LinkedHashMap<>();

		try {
			while (rSet.next()) {
				String studentNo = rSet.getString("student_no");
				TestListSubject testList = map.get(studentNo);

				if (testList == null) {
					testList = new TestListSubject();
					testList.setEntYear(rSet.getInt("ent_year"));
					testList.setClassNum(rSet.getString("class_num"));
					testList.setStudentNo(studentNo);
					testList.setStudentName(rSet.getString("student_name"));
					testList.setPoints(new HashMap<>());
					map.put(studentNo, testList);
				}

				int testNo = rSet.getInt("test_no");
				int point = rSet.getInt("point");
				testList.getPoints().put(testNo, point);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return new ArrayList<>(map.values());
	}

	public List<TestListSubject> filter(int entYear, String classNum, Subject subject, School school) throws Exception {
		List<TestListSubject> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;

		try {
			statement = connection.prepareStatement(baseSql + " ORDER BY s.no, t.no");
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			statement.setString(3, classNum);
			statement.setString(4, subject.getCd());
			rSet = statement.executeQuery();
			list = postFilter(rSet);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}

		return list;
	}
}
