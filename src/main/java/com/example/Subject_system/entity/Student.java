package com.example.Subject_system.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student {

	//�Ǹ�
	@Id
	@Column(name = "student_number")
	private String studentNumber;

	//�ǥͩm�W
	@Column(name = "student_name")
	private String studentName;

	//�w�׽ҵ{�N��
	@Column(name = "take_course_number")
	private String takeCourseNumber;
	
	//�w�׽ҵ{�W��
	@Column(name = "take_course_name")
	private String takeCourseName;

	//�`�Ǥ�
	@Column(name = "total_credits")
	private int totalCredits;
	
	

	public Student() {
		super();
		
	}

	public Student(String studentNumber, String studentName) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
	}

	public Student(String studentNumber, String studentName, String takeCourseNumber, int totalCredits) {
		super();
		this.studentNumber = studentNumber;
		this.studentName = studentName;
		this.takeCourseNumber = takeCourseNumber;
		this.totalCredits = totalCredits;
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

	public String getTakeCourseNumber() {
		return takeCourseNumber;
	}

	public void setTakeCourseNumber(String takeCorseNumber) {
		this.takeCourseNumber = takeCorseNumber;
	}

	public String getTakeCourseName() {
		return takeCourseName;
	}

	public void setTakeCourseName(String takeCourseName) {
		this.takeCourseName = takeCourseName;
	}

	public int getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(int totalCredits) {
		this.totalCredits = totalCredits;
	}
}
