package com.example.student.dao;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.student.dto.CourseRequestDto;
import com.example.student.dto.CourseResponseDto;



@Repository
public class CourseDao {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public int insertCourse(CourseRequestDto dto) {
		int result=0;
		String sql="insert into courses(id,name) values(?,?)";
		result= jdbcTemplate.update(sql,dto.getId(),dto.getName());
		return result;
	}
	
	public List<CourseResponseDto> selectAllCourse() {
		String sql = "select * from courses";
		return jdbcTemplate.query(sql, (rs,rowNum) -> new  CourseResponseDto(rs.getString("id"),rs.getString("name")));
	}
	
	public boolean checkName(String name) {
		String sql="select exists(select * from courses where name=?)";
		return jdbcTemplate.queryForObject(sql,Boolean.class,name);
	}
	
	public CourseResponseDto findName(String name) {
		String sql="select * from courses where name=?";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new CourseResponseDto(rs.getString("id"),rs.getString("name")),name);
	}
	
	public List<String> selectCoursesByStudentid(String student_id) {
		String sql = "select courses.name, courses.id from students_courses join courses on students_courses.course_id = courses.id where students_courses.student_id = ? ";
		return jdbcTemplate.query(sql, (rs,rowNum) -> 
				rs.getString("name"),
				student_id);
	}
	
	public List<CourseResponseDto> selectCoursesByStudentId(String student_id){
		String sql = "select courses.name, courses.id from students_courses join courses on students_courses.course_id = courses.id where students_courses.student_id = ? ";
		return jdbcTemplate.query(sql, (rs,rowNum) -> new CourseResponseDto(rs.getString("id"),rs.getString("name")),student_id);
	}
}
