package com.example.Subject_system.vo;

import java.util.List;

import com.example.Subject_system.entity.Course;
import com.example.Subject_system.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class SubjectSystemResponse {

	private Course course;
	
	private Student student;
	
	private String message;
	
	private List<Course> courseList;

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
	
	public SubjectSystemResponse(Student student,List<Course> courseList,  String message) {
		super();
		this.student = student;
		this.courseList = courseList;
		this.message = message;
	}

	public SubjectSystemResponse(List<Course> courseList, String message) {
		super();
		this.courseList = courseList;
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

	public List<Course> getcourseList() {
		return courseList;
	}

	public void setcourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	
	
	
}
