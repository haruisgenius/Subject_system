package com.example.Subject_system.vo;

import java.util.List;

import com.example.Subject_system.entity.Course;
import com.example.Subject_system.entity.Student;

public class SubjectSystemResponse {

	private Course course;
	
	private Student student;
	
	private String message;

	public SubjectSystemResponse() {
		super();
		
	}

	public SubjectSystemResponse(String message) {
		super();
		this.message = message;
	}

	public SubjectSystemResponse(Course course, String message) {
		super();
		this.course = course;
		this.message = message;
	}

	public SubjectSystemResponse(Student student, String message) {
		super();
		this.student = student;
		this.message = message;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
