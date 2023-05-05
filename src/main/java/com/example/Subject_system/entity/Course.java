package com.example.Subject_system.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

	// 課程代號
	@Id
	@Column(name = "course_number")
	private String courseNumber;

	// 課程名稱
	@Column(name = "course_name")
	private String courseName;

	// 課程上課日
	@Column(name = "week_day")
	private String weekDay;

	// 開始上課時間
	@Column(name = "start_time")
	private LocalTime startTime;

	// 課程結束時間
	@Column(name = "end_time")
	private LocalTime endTime;

	// 課程學分
	@Column(name = "credits")
	private int credits;

	// 修課學生
	@Column(name = "course_student")
	private int courseStudent;

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Course(String courseNumber, String courseName, String weekDay, LocalTime startTime, LocalTime endTime,
			int credits) {
		super();
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.weekDay = weekDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.credits = credits;
	}

	public void updateCourse(String courseName, String weekDay, LocalTime startTime, LocalTime endTime, int credits) {
		this.courseName = courseName;
		this.weekDay = weekDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.credits = credits;
	}
	
	public void updateCourseStudent(int credits) {
		this.credits = credits;
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

}
