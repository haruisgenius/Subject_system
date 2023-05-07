package com.example.Subject_system.service.impl;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.Subject_system.contants.RtnCode;
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

//	新增課程
	@Override
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查課程代碼格式 > 抽方法
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// 檢查課程名稱
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		// 檢查上課時間and下課時間 > 抽方法
		SubjectSystemResponse checkTimeResult = checkWeekdayAndStartOrEndTime(subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime());
		if (checkTimeResult != null) {
			return checkTimeResult;
		}

		// 檢查學分數 > 不可小於1或大於3
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		// 檢查代碼是否已存在
		if (courseDao.existsById(subjectSystemRequest.getCourseNumber())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// 存資料庫
		Course saveCourse = new Course(subjectSystemRequest.getCourseNumber(), subjectSystemRequest.getCourseName(),
				subjectSystemRequest.getWeekDay(), subjectSystemRequest.getStartTime(),
				subjectSystemRequest.getEndTime(), subjectSystemRequest.getCredits());
		courseDao.save(saveCourse);
		return new SubjectSystemResponse(saveCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	新增學生
	@Override
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest) {
		// 檢查學號 > 抽方法
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查學生姓名是否為空
		if (!StringUtils.hasText(subjectSystemRequest.getStudentName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// 學號是否重複
		if (studentDao.existsById(subjectSystemRequest.getStudentNumber())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// 存資料庫
		Student saveStudent = new Student(subjectSystemRequest.getStudentNumber(),
				subjectSystemRequest.getStudentName());
		studentDao.save(saveStudent);
		return new SubjectSystemResponse(saveStudent, RtnCode.SUCCESSFUL.getMessage());
	}

//	刪除課程
	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查輸入課程代碼格式 > 抽方法
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		// 檢查此課程修課人數是否>0
		if (op.get().getCourseStudent() > 0) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse(RtnCode.SUCCESSFUL.getMessage());
	}

//	查詢課程(課程代碼)
	@Override
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest) {
		// 檢查輸入課程代碼格式 > 抽方法
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Course courseInfo = op.get();
		return new SubjectSystemResponse(courseInfo, RtnCode.SUCCESSFUL.getMessage());
	}

//	查詢課程(課程名稱)
	@Override
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest) {
		// 檢查課程名稱
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// 模糊搜尋
		List<Course> findCourseNameList = courseDao
				.findByCourseNameContainingIgnoreCase(subjectSystemRequest.getCourseName());
		// 檢查課程是否存在
		if (findCourseNameList.isEmpty()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		return new SubjectSystemResponse(findCourseNameList, RtnCode.SUCCESSFUL.getMessage());
	}

//	修改課程
	@Override
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查輸入課程代碼格式 > 抽方法
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		// 檢查課程名稱
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// 檢查上課時間and下課時間 > 抽方法
		SubjectSystemResponse checkTimeResult = checkWeekdayAndStartOrEndTime(subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime());
		if (checkTimeResult != null) {
			return checkTimeResult;
		}

		// 檢查學分數
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// 撈所有學生資料
		List<Student> allStudentInfoList = studentDao.findAll();
		// 若整個資料庫無學生(學生人數=0) > 直接修改課程資料
		if (CollectionUtils.isEmpty(allStudentInfoList)) {
			Course updateCourse = op.get();
			updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
					subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
					subjectSystemRequest.getCredits());
			courseDao.save(updateCourse);
			return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
		}

		// 修改前學分
		int orginalCredit = op.get().getCredits();
		// 修改後學分
		int newCredeit = subjectSystemRequest.getCredits();
		// 遍歷學生資料
		for (Student studentInfo : allStudentInfoList) {
			// 若學生無選修課程 > 跳過
			if (studentInfo.getTakeCourseNumber() == null) {
				continue;
			} else if (studentInfo.getTakeCourseNumber().contains(subjectSystemRequest.getCourseNumber())) {
				// 學生原總學分
				int studentCredit = studentInfo.getTotalCredits();
				// 學生新總學分 > 原總學分 - 修改前學分 + 修改後學分
				int newStudentCredit = (studentCredit - orginalCredit) + newCredeit;
				// 存進學生資料
				studentInfo.setTotalCredits(newStudentCredit);
			}
		}

		// 修改資料存進資料表
		Course updateCourse = op.get();
		updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
				subjectSystemRequest.getCredits());
		// 修改資料表存進資料庫
		courseDao.save(updateCourse);

		// 存學生修改學分後資料進資料庫
		studentDao.saveAll(allStudentInfoList);

		return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	選課
	@Override
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest) {
		// 判斷學號輸入內容格式 > 抽方法
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 判斷在 學生DB中 是否已存在這個學生ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// 檢查輸入課程代碼格式 > 抽方法
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

		// 檢查課程是否存在
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// 檢查課程代碼&名稱是否重複
		for (int i = 0; i < takeCourseList.size(); i++) {
			for (int j = i + 1; j < takeCourseList.size(); j++) {
				if (takeCourseList.get(i).getCourseNumber().equals(takeCourseList.get(j).getCourseNumber())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				} else if (takeCourseList.get(i).getCourseName()
						.equalsIgnoreCase(takeCourseList.get(j).getCourseName())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				}
			}
		}

		// 檢查課程是否衝堂 > 抽方法
		SubjectSystemResponse checkCrashCourseResult = crashCourse(takeCourseList, takeCourseList);
		if(checkCrashCourseResult != null) {
			return checkCrashCourseResult;
		}

		// 檢查課程選修人數是否達上限
		for (Course takeCourse : takeCourseList) {
			if (takeCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
			}
		}

		// 加選總學分
		int takeCourseTotalCredits = 0;
		// 欲選學分加總
		for (Course takeCourse : takeCourseList) {
			// 欲選課程學分撈出來加總
			takeCourseTotalCredits += takeCourse.getCredits();
		}
		// 檢查學生最終總學分是否已達上限
		if (takeCourseTotalCredits >= 10) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		student.setTotalCredits(takeCourseTotalCredits);

		// 要回傳的課程詳細
		List<Course> finallyTakeCouseList = courseDao
				.findAllByCourseNumberIn(subjectSystemRequest.getCourseNumberList());
		// 將陣列資料轉字串存進學生資訊
		String allCourseStr = subjectSystemRequest.getCourseNumberList().toString();
		// 選課字串陣列去前後中括號
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		studentDao.save(student);

//	 		(算選修人數)
//TODO 資料庫saveAll
		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int sltTotalStudent = 0;
		List<Course> allCourseList = courseDao.findAll();
		for (Course takeCourse : takeCourseList) {
			for (Course allCourse : allCourseList) {
				if (takeCourse.getCourseNumber().equals(allCourse.getCourseNumber())) {
					courseStd = allCourse.getCourseStudent();
					// 原選修人數+1
					sltTotalStudent = courseStd + 1;
					// 將新選修總人數存進資料庫
					allCourse.setCourseStudent(sltTotalStudent);
				}
			}
		}
		courseDao.saveAll(allCourseList);
		return new SubjectSystemResponse(student, finallyTakeCouseList, RtnCode.SUCCESSFUL.getMessage());
	}

//	加課
	@Override
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest) {

		// 判斷學號輸入內容格式
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// 判斷在 學生DB中 是否已存在這個學生ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// 檢查輸入課程代碼格式
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//		(檢查是否已選修課程)
		if (studentOp.get().getTakeCourseNumber().isEmpty()) {
			return new SubjectSystemResponse(RtnCode.TAKE_COURSE_NOTYET.getMessage());
		}

//		檢查課程不存在
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// 把學生已選課程代號個別取出存進陣列
		String[] stdCourseNumStr = studentOp.get().getTakeCourseNumber().split(",");
		List<String> stdCourseNumStrList = new ArrayList<>(); // 學生已選課程代碼 數個
		for (String item : stdCourseNumStr) {
			stdCourseNumStrList.add(item.trim());
		}

		// (已選)學生表內課程代碼and課程名稱
		List<Course> stdCourseList = courseDao.findAllByCourseNumberIn(stdCourseNumStrList);
		// (加選)從資料庫撈欲選課程資訊存進courseList
		List<Course> sltCourseList = courseDao.findAllByCourseNumberIn(subjectSystemRequest.getCourseNumberList());

		// 比較學生是否重複選修 > 學生已選與欲選課程代碼是否重複or名稱是否相同
		for (Course stdCourse : stdCourseList) {
			for (Course sltCourse : sltCourseList) {
				if (stdCourse.getCourseNumber().equals(sltCourse.getCourseNumber())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				} else if (stdCourse.getCourseName().equals(sltCourse.getCourseName())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				}
			}
		}
		
//		比較加選課程衝堂
		SubjectSystemResponse checkSltCrashCourseResult = crashCourse(sltCourseList, sltCourseList);
		if(checkSltCrashCourseResult != null) {
			return checkSltCrashCourseResult;
		}

// 		(比較衝堂----檢查已選與欲選課程時間是否衝突)
		SubjectSystemResponse checkCrashCourseResult = crashCourse(stdCourseList, sltCourseList);
		if(checkCrashCourseResult != null) {
			return checkCrashCourseResult;
		}

//		(個別檢查選修人數是否達上限)
		for (Course sltCourse : sltCourseList) {
			if (sltCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
			}
		}

// 		(檢查總學分)

		// 欲選總學分
		int sltTotalCredit = 0;
		// 學生總學分
		int stdTotalCredit = studentOp.get().getTotalCredits();

		// 欲選學分加總
		for (Course sltCourse : sltCourseList) {
			// 欲選課程學分撈出來加總
			sltTotalCredit += sltCourse.getCredits();

		}
		// 學生最終總學分
		int finalTotalCredit = sltTotalCredit + stdTotalCredit;

		// 檢查學生最終總學分是否已達上限
		if (finalTotalCredit > 10) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		student.setTotalCredits(finalTotalCredit);

//(存取已選+加選的課程)	

		List<String> allCourseList = new ArrayList<>();
		List<String> sltCList = new ArrayList<>();

		// 從Course撈課程代碼(字串)加進(新選)字串陣列
		for (Course item : sltCourseList) {
			sltCList.add(item.getCourseNumber());
		}
		// 將已選課程and新選課程加進同一陣列
		allCourseList.addAll(stdCourseNumStrList);
		allCourseList.addAll(sltCList);
		List<Course> finallyCouseList = courseDao.findAllByCourseNumberIn(allCourseList);
		// 將陣列資料轉字串存進學生資訊
		String allCourseStr = allCourseList.toString();
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		// 將學生資訊存進資料庫
		studentDao.save(student);

// 		(算選修人數)

		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int sltTotalStudent = 0;
		for (Course sltCourse : sltCourseList) {
			courseStd = sltCourse.getCourseStudent();
			// 原選修人數+1
			sltTotalStudent = courseStd + 1;
			// 將新選修總人數存進資料庫
			sltCourse.setCourseStudent(sltTotalStudent);
			courseDao.save(sltCourse);
		}

		return new SubjectSystemResponse(student, finallyCouseList, RtnCode.SUCCESSFUL.getMessage());
	}

	// 退課
	@Override
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查學生
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// 檢查課程代碼
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}
//		檢查課程不存在
		List<Course> dropCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (dropCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// 學生已選課程代碼(字串)存進字串陣列
		String[] takeCourseNumList = studentOp.get().getTakeCourseNumber().split(",");
		List<String> takeCourseNumStrList = new ArrayList<>();
		for (String item : takeCourseNumList) {
			takeCourseNumStrList.add(item.trim());
		}
		// 學生退選課程代碼(字串)存進字串陣列
		List<String> dropCourseNumStrList = new ArrayList<>();
		for (String dropCourse : subjectSystemRequest.getCourseNumberList()) {
			dropCourseNumStrList.add(dropCourse);
		}

		// 檢查是否有選修欲退課程
		for (String dropCourse : dropCourseNumStrList) {
			if (!takeCourseNumStrList.contains(dropCourse)) {
				return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
			}
		}

		// 退課
		// 原選課程內 想退選課程以外的課程 >最終想選課程
		Set<String> dropCourseNumStrSet = new HashSet<>(dropCourseNumStrList);
//		for (String dropCourse : dropCourseNumStrList) {
//			dropCourseNumStrSet.add(dropCourse);
//		}
		List<String> finallyTakeCourseStrList = new ArrayList<>();
		for (int i = 0; i < takeCourseNumStrList.size(); i++) {
			String takeCourse = takeCourseNumStrList.get(i);
			if (!dropCourseNumStrSet.contains(takeCourse)) {
				finallyTakeCourseStrList.add(takeCourse);
			}
		}
		// 要回傳的最終選課單
		List<Course> finallyTakeCouseList = courseDao.findAllById(finallyTakeCourseStrList);

		String finallyTakeCourseStr = finallyTakeCourseStrList.toString();
//		學生資料 更新最終選課
		student.setTakeCourseNumber(finallyTakeCourseStr.substring(1, finallyTakeCourseStr.length() - 1));
//		studentDao.save(student);

		// 學分扣減
		int minusCredit = 0;
		int totalCredit = student.getTotalCredits();
		for (Course credit : dropCourseList) {
			minusCredit += credit.getCredits();
		}
		int finallyTotalCredit = totalCredit - minusCredit;
//		學生資料 更新最終總學分
		student.setTotalCredits(finallyTotalCredit);
//		存最終學生資料 進資料庫
		studentDao.save(student);

//TODO Dao存list
//		課程資料 扣減選修人數
		List<Course> allCourseInfoList = courseDao.findAll();
		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int newCourseStd = 0;
		for (Course course : dropCourseList) {
			for (Course CourseInfo : allCourseInfoList) {
				if (CourseInfo.getCourseNumber().equals(course.getCourseNumber())) {
					courseStd = CourseInfo.getCourseStudent();
					newCourseStd = courseStd - 1;
					CourseInfo.setCourseStudent(newCourseStd);

				}
			}
		}
		courseDao.saveAll(allCourseInfoList);

		return new SubjectSystemResponse(student, finallyTakeCouseList, RtnCode.SUCCESSFUL.getMessage());
	}


	// 刪除學生
	@Override
	public SubjectSystemResponse deleteStudent(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// 若無選課 > 直接刪除學生資料
		if (!StringUtils.hasText(studentOp.get().getTakeCourseNumber())) {
			studentDao.deleteById(subjectSystemRequest.getStudentNumber());
			return new SubjectSystemResponse(RtnCode.SUCCESSFUL.getMessage());
		}

//		課程資料 扣減選修人數
		List<String> takeCourseStrList = new ArrayList<>();
		String[] takeCourseStringAry = studentOp.get().getTakeCourseNumber().split(",");
		for (String item : takeCourseStringAry) {
			takeCourseStrList.add(item.trim());
		}

//		TODO Dao刪除list
		List<Course> allCourseInfo = courseDao.findAll();
		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int newCourseStd = 0;
		for (String course : takeCourseStrList) {
			for (Course courseInfo : allCourseInfo) {
				if (courseInfo.getCourseNumber().equals(course)) {
					courseStd = courseInfo.getCourseStudent();
					newCourseStd = courseStd - 1;
					courseInfo.setCourseStudent(newCourseStd);
				}
			}
		}
		courseDao.saveAll(allCourseInfo);

		studentDao.deleteById(subjectSystemRequest.getStudentNumber());

		return new SubjectSystemResponse(RtnCode.SUCCESSFUL.getMessage());
	}


	// 查詢學生
	@Override
	public SubjectSystemResponse findStudent(SubjectSystemRequest subjectSystemRequest) {
		// 檢查學號輸入 > 抽方法
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 判斷在 學生DB中 是否已存在這個學生ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();
		// 如果沒有修課
		if (!StringUtils.hasText(student.getTakeCourseNumber())) {
			return new SubjectSystemResponse(student, RtnCode.SUCCESSFUL.getMessage());
		}

		// 學生選修課程加入list
		String[] studentAllCourseStrAry = student.getTakeCourseNumber().split(",");
		List<String> studentAllCourseList = new ArrayList<>();
		for (String allCourseStr : studentAllCourseStrAry) {
			studentAllCourseList.add(allCourseStr.trim());
		}
		List<Course> StudentTakeCourse = courseDao.findAllById(studentAllCourseList);

		return new SubjectSystemResponse(student, StudentTakeCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	-----------------------------抽取方法-----------------------------

	private SubjectSystemResponse checkStudentNumber(String subjectSystemRequest) {
		// 檢查學號
		if (!StringUtils.hasText(subjectSystemRequest)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.matches(pattern)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		return null;
	}

	private SubjectSystemResponse checkCourseNumber(String subjectSystemRequest) {
		// 檢查課程代碼格式
		if (!StringUtils.hasText(subjectSystemRequest)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.matches(pattern)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		return null;
	}

	// 檢查課程時間格式
	private SubjectSystemResponse checkWeekdayAndStartOrEndTime(String subjectSystemRequestWeekday,
			LocalTime subjectSystemRequestStartTime, LocalTime subjectSystemRequestEndTime) {
		// 檢查星期
		List<String> week = Arrays.asList("一", "二", "三", "四", "五");
		if (!StringUtils.hasText(subjectSystemRequestWeekday)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (!week.contains(subjectSystemRequestWeekday)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		// 檢查上下課時間
		if (subjectSystemRequestStartTime == null || subjectSystemRequestEndTime == null
				|| subjectSystemRequestStartTime.toString().isBlank()
				|| subjectSystemRequestEndTime.toString().isBlank()) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		if (subjectSystemRequestStartTime.getHour() < 8 || subjectSystemRequestStartTime.getHour() > 20
				|| subjectSystemRequestEndTime.getHour() < 8 || subjectSystemRequestEndTime.getHour() > 20) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (subjectSystemRequestStartTime.getHour() >= subjectSystemRequestEndTime.getHour()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		return null;
	}

	private SubjectSystemResponse crashCourse(List<Course> courseList1, List<Course> courseList2) {
		for (int i = 0; i < courseList1.size(); i++) {
			String weekDay1 = courseList1.get(i).getWeekDay();
			LocalTime str1 = courseList1.get(i).getStartTime();
			LocalTime end1 = courseList1.get(i).getEndTime();
			for (int j = i + 1; j < courseList2.size(); j++) {
				String weekDay2 = courseList2.get(j).getWeekDay();
				LocalTime str2 = courseList2.get(j).getStartTime();
				LocalTime end2 = courseList2.get(j).getEndTime();
				if (weekDay1.equals(weekDay2)) {
					if (!str2.isAfter(end1) && !end2.isBefore(str1)) {
						return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
					}
				}
			}
		}
		return null;
	}

}
