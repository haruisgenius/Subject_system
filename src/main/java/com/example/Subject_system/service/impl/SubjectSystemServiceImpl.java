package com.example.Subject_system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Subject_system.entity.Course;
import com.example.Subject_system.entity.Student;
import com.example.Subject_system.respository.CourseDao;
import com.example.Subject_system.respository.StudentDao;
import com.example.Subject_system.service.ifs.SubjectSystemService;
import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

@Service
public class SubjectSystemServiceImpl implements SubjectSystemService {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private StudentDao studentDao;

	@Override
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查課程代碼格式
		if (subjectSystemRequest.getCourseNumber().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:課程代碼為空");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getCourseNumber().matches(pattern)) {
			return new SubjectSystemResponse("資料錯誤:課程代碼格式不符");
		}

		// 檢查課程名稱
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:課程名稱為空");
		}

		// 檢查星期
		List<String> week = Arrays.asList("一", "二", "三", "四", "五");
		if (subjectSystemRequest.getWeekDay().isEmpty()) {
			return new SubjectSystemResponse("資料錯誤:上課日期為空");
		}
		if (!week.contains(subjectSystemRequest.getWeekDay())) {
			return new SubjectSystemResponse("資料錯誤:上課日期錯誤");
		}

		// 檢查上課時間and下課時間
		if (subjectSystemRequest.getStartTime() <= 0 || subjectSystemRequest.getStartTime() > 24
				|| subjectSystemRequest.getEndTime() <= 0 || subjectSystemRequest.getEndTime() > 24) {
			return new SubjectSystemResponse("資料錯誤:上課時間錯誤");
		}
		if (subjectSystemRequest.getStartTime() > subjectSystemRequest.getEndTime()) {
			return new SubjectSystemResponse("資料錯誤:上課時間格式錯誤");
		}

		// 檢查學分數
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse("資料錯誤:學分錯誤");
		}

		// 檢查代碼是否已存在
		if (courseDao.existsById(subjectSystemRequest.getCourseNumber())) {
			return new SubjectSystemResponse("資料錯誤:課程代碼重複");
		}
		// 存資料庫
		Course saveCourse = new Course(subjectSystemRequest.getCourseNumber(), subjectSystemRequest.getCourseName(),
				subjectSystemRequest.getWeekDay(), subjectSystemRequest.getStartTime(),
				subjectSystemRequest.getEndTime(), subjectSystemRequest.getCredits());
		courseDao.save(saveCourse);
		return new SubjectSystemResponse(saveCourse, "新增課程成功");
	}

	@Override
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest) {
		// 檢查學號
		if (subjectSystemRequest.getStudentNumber().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:學號為空");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getStudentNumber().matches(pattern)) {
			return new SubjectSystemResponse("資料錯誤:學號格式不符");
		}
		// 檢查學生姓名是否為空
		if (subjectSystemRequest.getStudentName().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:姓名為空");
		}
		// 學號是否重複
		if (studentDao.existsById(subjectSystemRequest.getStudentNumber())) {
			return new SubjectSystemResponse("資料錯誤:學生資料已存在");
		}
		// 存資料庫
		Student saveStudent = new Student(subjectSystemRequest.getStudentNumber(),
				subjectSystemRequest.getStudentName());
		studentDao.save(saveStudent);
		return new SubjectSystemResponse(saveStudent, "新增學生成功");
	}

	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查課程代碼格式
		if (subjectSystemRequest.getCourseNumber().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:課程代碼為空");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getCourseNumber().matches(pattern)) {
			return new SubjectSystemResponse("資料錯誤:課程代碼格式不符");
		}
		//檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if(op.isEmpty()) {
			return new SubjectSystemResponse("課程不存在");
		}
		//檢查此課程修課人數是否>0
		Course courseInfo = op.get();
		if( courseInfo.getCourseStudent() > 0) {
			return new SubjectSystemResponse("課程無法刪除");
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse("刪除課程成功");
	}

}
