package com.example.Subject_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {

	// �ҵ{�N��
	@Id
	@Column(name = "course_number")
	private String courseNumber;

	// �ҵ{�W��
	@Column(name = "course_name")
	private String courseName;

	// �ҵ{�W�Ҥ�
	@Column(name = "week_day")
	private String weekDay;

	// �}�l�W�Үɶ�
	@Column(name = "start_time")
	private int startTime;

	// �ҵ{�����ɶ�
	@Column(name = "end_time")
	private int endTime;

	// �ҵ{�Ǥ�
	@Column(name = "credits")
	private int credits;

	// �׽Ҿǥ�
	@Column(name = "course_student")
	private int courseStudent;

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Course(String courseNumber, String courseName, String weekDay, int startTime, int endTime, int credits) {
		super();
		this.courseNumber = courseNumber;
		this.courseName = courseName;
		this.weekDay = weekDay;
		this.startTime = startTime;
		this.endTime = endTime;
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

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
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
