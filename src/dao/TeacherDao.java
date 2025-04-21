package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Teacher;

public class TeacherDao extends Dao{
	public Teacher get(String id) throws Exception{
		Teacher teacher=new Teacher();
		Connection connection = getConnection();
		PreparedStatement statement=null;
		try {
			statement=connection.prepareStatement("select * from teacher where id =?");
			statement.setString(1, id);
			ResultSet rSet = statement.executeQuery();
			SchoolDao schoolDao=new SchoolDao();
			if (rSet.next()){
				teacher.setId(rSet.getString("id"));
				teacher.setPassword(rSet.getString("password"));
				teacher.setName(rSet.getString("name"));
				teacher.setSchool(schoolDao.get(rSet.getString("school_cd")));
			}else{
				teacher=null;
			}
		}catch (Exception e){
			throw e;
		}finally {
			if (statement !=null){
				try{
					statement.close();
				}catch (SQLException sqle){
					throw sqle;
				}
			}
			if (connection !=null){
				try{
					connection.close();
				}catch (SQLException sqle){
					throw sqle;
				}
			}
		}
		return teacher;
	}
	public Teacher login(String id, String password)throws Exception{
			// 教員クラスのインスタンスを取得
			Teacher teacher = get(id);
			// 教員がnullまたはパスワードが一致しない場合
			if (teacher == null || !teacher.getPassword().equals(password)) {
				return null;
			}
			return teacher;
		}
}


