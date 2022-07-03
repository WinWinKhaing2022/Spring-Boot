package com.example.student.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class StudentBean {
	private String id;
	@NotEmpty
	private String name;
	@NotEmpty
	private String dob;
	@NotEmpty
	private String gender;
	@NotEmpty
	private String phone;
	@NotEmpty
	private String education;
	@NotEmpty
	private List<String>attendCourses;
	
	

		
			public StudentBean() {
				
			}

			public StudentBean( String name, String dob, String gender, String phone, String education) {
				super();
				this.name = name;
				this.dob = dob;
				this.gender = gender;
				this.phone = phone;
				this.education = education;
			
				
			}
			public StudentBean(String id, String name, String dob, String gender, String phone, String education) {
				super();
				this.id=id;
				this.name = name;
				this.dob = dob;
				this.gender = gender;
				this.phone = phone;
				this.education = education;
			
				
			}

			public StudentBean( String name, String dob, String gender, String phone, String education,List<String>attendCourses) {
				super();
				this.name = name;
				this.dob = dob;
				this.gender = gender;
				this.phone = phone;
				this.education = education;
				this.attendCourses=attendCourses;			
			}
			
			public StudentBean( String id,String name, String dob, String gender, String phone, String education,List<String>attendCourses) {
				super();
				this.id=id;
				this.name = name;
				this.dob = dob;
				this.gender = gender;
				this.phone = phone;
				this.education = education;
				this.attendCourses=attendCourses;			
			}
		
			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getDob() {
				return dob;
			}

			public void setDob(String dob) {
				this.dob = dob;
			}

			public String getGender() {
				return gender;
			}

			public void setGender(String gender) {
				this.gender = gender;
			}

			public String getPhone() {
				return phone;
			}

			public void setPhone(String phone) {
				this.phone = phone;
			}

			public String getEducation() {
				return education;
			}

			public void setEducation(String education) {
				this.education = education;
			}
			
			
			public List<String> getAttendCourses() {
				return attendCourses;
			}

			public void setAttendCourses(List<String> attendCourses) {
				this.attendCourses = attendCourses;
			}

			@Override
			public String toString() {
				return "StudentBean [id=" + id + ", name=" + name + ", dob=" + dob + ", gender=" + gender + ", phone="
						+ phone + ", education=" + education + ", attendCourses=" + "]";
			}
}
