package com.example.student.dao;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.student.dto.StudentRequestDto;
import com.example.student.dto.StudentResponseDto;




@Repository
public class StudentDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<StudentResponseDto> selectAllStudents(){
		String sql="select * from students";
		return jdbcTemplate.query(sql, (rs,rowNum) -> new  StudentResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("dob"),rs.getString("gender"),rs.getString("phone"),rs.getString("education")));
		
	}
	
	public int insertStudentData(StudentRequestDto dto) {
		int result=0;
		String sql="insert into students(id,name,dob,gender,phone,education) values(?,?,?,?,?,?)";
		result= jdbcTemplate.update(sql,dto.getId(),dto.getName(),dto.getDob(),dto.getGender(),dto.getPhone(),dto.getEducation());
		return result;
		
	}
	
	public int addCourseForStrudent(String student_id, String course_id) {
		int result=0;
		String sql = "insert into students_courses (student_id, course_id) values(?, ?)";
		result=jdbcTemplate.update(sql,student_id,course_id);
		return result;
	}
	
	public List<StudentResponseDto> selectStudentListByIdOrNameOrCourse(String id, String name, String course) {
		String sql = "select distinct students.id, students.name from students_courses join students on students_courses.student_id = students.id join courses on students_courses.course_id = courses.id where students.id like ? or students.name like ? or courses.name like ?";
		 return jdbcTemplate.query(sql, (rs, rowNum) -> new StudentResponseDto(
	                rs.getString("id"),
	                rs.getString("name")),
	                "%" + id + "%",
	                "%" + name + "%",
	                "%" + course + "%");
	}
	
	public StudentResponseDto selectStudentById(String student_id) {
		String sql = "select * from students where id=?";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new StudentResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("dob"),rs.getString("gender"),rs.getString("phone"),rs.getString("education")),student_id);
	}
	
	public int updateStudent(StudentRequestDto dto) {
		int result=0;
		String sql = "update students set name=?, dob=?,  gender=?, phone=?, education=? where id=?";
		result= jdbcTemplate.update(sql,dto.getName(),dto.getDob(),dto.getGender(),dto.getPhone(),dto.getEducation(),dto.getId());
		return result;
	}
	public int deleteStudentById(String student_id) {
		int result=0;
		String sql = "delete from students where id=?";
		result=jdbcTemplate.update(sql,student_id);
		return result;
		
	}
	
	public int deleteAttendCoursesByStudentId(String student_id) {
		int result=0;
		String sql = "delete from students_courses where student_id=?";
		result=jdbcTemplate.update(sql,student_id);
		return result;
	}
	
	
}
