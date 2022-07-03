package com.example.student.dto;

public class UserResponseDto {
	private String id;
	private String name;
	private String email;
	private String password;
	private String role;
	public UserResponseDto(String id,String name,String email,String password,String role) {
		this.id=id;
		this.name=name;
		this.email=email;
		this.password=password;
		
		this.role=role;
	}
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserResponseDto [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", role=" + role + "]";
	}
	
}
