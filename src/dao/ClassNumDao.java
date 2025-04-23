package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {
	public ClassNum get(String class_num, School school) throws Exception {
		ClassNum classNum = new ClassNum();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("SELECT * FROM class_num WHERE class_num = ? AND school_cd = ?");
			statement.setString(1, class_num);
			statement.setString(2, school.getCd());
			ResultSet rSet = statement.executeQuery();
			SchoolDao sDao = new SchoolDao();
			if (rSet.next()) {
				classNum.setClass_num(rSet.getString("class_num"));
				classNum.setSchool(sDao.get(rSet.getString("school_cd")));
			} else {
				classNum = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return classNum;
	}

	public List<String> filter(School school) throws Exception {
		List<String> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement("SELECT class_num FROM class_num WHERE school_cd = ? ORDER BY class_num");
			statement.setString(1, school.getCd());
			ResultSet rSet = statement.executeQuery();
			while (rSet.next()) {
				list.add(rSet.getString("class_num"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return list;
	}

	// 保存（存在しなければINSERT、あればUPDATE）
	public boolean save(ClassNum classNum) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int count = 0;
		try {
			ClassNum old = get(classNum.getClass_num(), classNum.getSchool());
			if (old == null) {
				statement = connection.prepareStatement("INSERT INTO class_num(class_num, school_cd) VALUES(?, ?)");
				statement.setString(1, classNum.getClass_num());
				statement.setString(2, classNum.getSchool().getCd());
			} else {
				// すでに存在する場合、更新内容がないのでスキップ（必要なら更新処理追加可）
				return false;
			}
			count = statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return count > 0;
	}

	// クラス名を変更（UPDATE処理）
	public boolean save(ClassNum classNum, String newClassNum) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int count = 0;
		try {
			statement = connection.prepareStatement("UPDATE class_num SET class_num = ? WHERE class_num = ? AND school_cd = ?");
			statement.setString(1, newClassNum);
			statement.setString(2, classNum.getClass_num());
			statement.setString(3, classNum.getSchool().getCd());
			count = statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return count > 0;
	}
}
