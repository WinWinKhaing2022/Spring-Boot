package com.example.student.dao;




import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.student.dto.UserRequestDto;
import com.example.student.dto.UserResponseDto;



@Repository
public class UserDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	
	public boolean checkLogin(String email, String password) {
        String sql = "select count(*) from user where binary email=? && binary password=?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, password);
        return count != null && count > 0;
    }

	
	public UserResponseDto selectLoginOneEmail(String email){
		
		String sql="select * from user where email=?";
		return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")),email);
		
		}

	public List<UserResponseDto> selectAllUsers(){
		
		String sql="select * from user";
		return jdbcTemplate.query(sql, (rs,rowNum) -> new  UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")));
		
	}
	
	public boolean checkEmailExist(String email) {
		
		String sql="select exists(select * from user where email=?)";
		return jdbcTemplate.queryForObject(sql,Boolean.class,email);
	}
	
	public int insertUserData(UserRequestDto dto) {
		int result=0;
		String sql="insert into user(id,name,email,password,role) values(?,?,?,?,?)";
		result= jdbcTemplate.update(sql,dto.getId(),dto.getName(),dto.getEmail(),dto.getPassword(),dto.getRole());
		return result;
	}
	
	public List<UserResponseDto> selectNameSearch(String name){
		String sql="select * from user where name=?";
		return jdbcTemplate.query(sql, (rs,rowNum) -> new  UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")),name);
	}
	
	public List<UserResponseDto> selectIdSearch(String id){
		String sql="select * from user where id=?";
		return jdbcTemplate.query(sql, (rs,rowNum) ->  new UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")),id);
	}
	
	public List<UserResponseDto> selectNameAndIdSearch(String id,String name){
		String sql="select * from user where id like ? || name like ?";
		return jdbcTemplate.query(sql, (rs,rowNum) ->  new UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")),id,name);
	}
	
	public UserResponseDto selectOneId(String id) {
		String sql="select * from user where id=? ";
		return jdbcTemplate.queryForObject( sql, (rs,rowNum) -> new UserResponseDto(rs.getString("id"),rs.getString("name"),rs.getString("email"),rs.getString("password"),rs.getString("role")),id);		
	}
	
	public int UpdateUserData(UserRequestDto dto) {
		System.out.println("Update dao "+dto);
		int result=0;
		String sql="update user set name=?,email=?,password=?,role=? where id=?";
		result= jdbcTemplate.update(sql,dto.getName(),dto.getEmail(),dto.getPassword(),dto.getRole(),dto.getId());
		return result;
	}
	
	public int deleteUser(UserRequestDto dto) {
		int result=0;
		String sql="delete from user where id=?";
		result=jdbcTemplate.update(sql, dto.getId());
		return result;
	}
}

