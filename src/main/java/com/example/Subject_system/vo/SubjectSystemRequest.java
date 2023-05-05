package com.example.Subject_system.vo;

import java.time.LocalTime;
import java.util.List;

import com.example.Subject_system.entity.Course;

public class SubjectSystemRequest {

	// course list
	private String courseNumber;

	private String courseName;

	private String weekDay;

	private LocalTime startTime;

	private LocalTime endTime;

	private int credits;

	private int courseStudent;

	// student list

	private String studentNumber;

	private String studentName;

	private List<String> courseNumberList;
	
	public SubjectSystemRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubjectSystemRequest(String courseNumber, String courseName, String weekDay, LocalTime startTime,
			LocalTime endTime, int credits) {
		super();
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.weekDay = weekDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.credits = credits;
	}

	public SubjectSystemRequest(String studentNumber, List<String> courseNumberList) {
		super();
		this.studentNumber = studentNumber;
		this.courseNumberList = courseNumberList;
	}

	public SubjectSystemRequest(String courseNumber) {
		super();
		this.courseNumber = courseNumber;
	}

	public SubjectSystemRequest(String studentNumber, String studentName) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getCourseStudent() {
		return courseStudent;
	}

	public void setCourseStudent(int courseStudent) {
		this.courseStudent = courseStudent;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public List<String> getCourseNumberList() {
		return courseNumberList;
	}

	public void setCourseNumberList(List<String> courseNumberList) {
		this.courseNumberList = courseNumberList;
	}

}
