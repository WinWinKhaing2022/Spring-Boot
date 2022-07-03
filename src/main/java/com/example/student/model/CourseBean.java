package com.example.student.model;

import javax.validation.constraints.NotEmpty;

public class CourseBean {
	
	private String id;
	@NotEmpty
	private String name;
	
	public CourseBean() {
		
	}
	public CourseBean(String id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	@Override
	public String toString() {
		return "CourseBean [id=" + id + ", name=" + name + "]";
	}
	
	
}
