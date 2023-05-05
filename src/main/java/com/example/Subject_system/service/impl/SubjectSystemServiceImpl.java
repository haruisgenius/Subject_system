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
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
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
		if (subjectSystemRequest.getStartTime().getHour() < 8 || subjectSystemRequest.getStartTime().getHour() > 20
				|| subjectSystemRequest.getEndTime().getHour() < 8
				|| subjectSystemRequest.getEndTime().getHour() > 20) {
			return new SubjectSystemResponse("資料錯誤:上課時間錯誤");
		}
		if (subjectSystemRequest.getStartTime().getHour() > subjectSystemRequest.getEndTime().getHour()) {
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

//	新增學生
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

//	刪除課程
	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse("課程不存在");
		}
		// 檢查此課程修課人數是否>0
		if (op.get().getCourseStudent() > 0) {
			return new SubjectSystemResponse("課程無法刪除");
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse("刪除課程成功");
	}

//	查詢課程(課程代碼)
	@Override
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查課程是否存在
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse("課程不存在");
		}
		Course courseInfo = op.get();
		return new SubjectSystemResponse(courseInfo, "取得課程資料成功");
	}

//	查詢課程(課程名稱)
	@Override
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest) {
		// 檢查課程名稱
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse("資料錯誤:課程名稱為空");
		}
		List<Course> findCourseNameList = courseDao
				.findByCourseNameContainingIgnoreCase(subjectSystemRequest.getCourseName());
		// 檢查課程是否存在
		if (findCourseNameList.isEmpty()) {
			return new SubjectSystemResponse("資料錯誤:相關課程不存在");
		}
		return new SubjectSystemResponse(findCourseNameList, "取得課程資料成功");
	}

//	修改課程
	@Override
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest) {
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
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// 檢查星期
		List<String> week = Arrays.asList("一", "二", "三", "四", "五");
		if (subjectSystemRequest.getWeekDay().isEmpty()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (!week.contains(subjectSystemRequest.getWeekDay())) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// 檢查上課時間and下課時間
		if (subjectSystemRequest.getStartTime().getHour() < 8 || subjectSystemRequest.getStartTime().getHour() > 20
				|| subjectSystemRequest.getEndTime().getHour() < 8
				|| subjectSystemRequest.getEndTime().getHour() > 20) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (subjectSystemRequest.getStartTime().getHour() >= subjectSystemRequest.getEndTime().getHour()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// 檢查學分數
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		List<Student> allStudentInfoList = studentDao.findAll();
		//若資料庫無學生 > 直接修改課程資料
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

		for (Student studentInfo : allStudentInfoList) {
			//若學生無選修課程 > 跳過
			if (studentInfo.getTakeCourseNumber() == null) {
				continue;
			}else if (studentInfo.getTakeCourseNumber().contains(subjectSystemRequest.getCourseNumber())) {
				// 學生原總學分
				int studentCredit = studentInfo.getTotalCredits();
				// 學生新總學分
				int newStudentCredit = (studentCredit - orginalCredit) + newCredeit;
				studentInfo.setTotalCredits(newStudentCredit);
			}
		}
		
		//修改資料存進資料表
		Course updateCourse = op.get();
		updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
				subjectSystemRequest.getCredits());
		//修改資料表存進資料庫
		courseDao.save(updateCourse);

		//存學生修改學分後資料
		studentDao.saveAll(allStudentInfoList);

		return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	選課
	@Override
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest) {
		// 判斷學號輸入內容格式
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// 判斷在 學生DB中 是否已存在這個學生ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("資料錯誤:此學生不存在");
		}

		// 檢查輸入課程代碼格式
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//			課程不存在
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(takeCourseList)) {
			return new SubjectSystemResponse("課程不存在");
		} else if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("課程不存在");
		}

		// 檢查課程代碼是否重複
		for (int i = 0; i < takeCourseList.size(); i++) {
			for (int j = i + 1; j < takeCourseList.size(); j++) {
				if (takeCourseList.get(i).getCourseNumber().equals(takeCourseList.get(j).getCourseNumber())) {
					return new SubjectSystemResponse("無法選修相同課程");
				} else if (takeCourseList.get(i).getCourseName()
						.equalsIgnoreCase(takeCourseList.get(j).getCourseName())) {
					return new SubjectSystemResponse("無法選修相同課程");
				}
			}
		}

		// 檢查課程是否衝堂
		for (int i = 0; i < takeCourseList.size(); i++) {
			for (int j = i + 1; j < takeCourseList.size(); j++) {
				if (takeCourseList.get(i).getWeekDay().equals(takeCourseList.get(j).getWeekDay())) {
					if (!takeCourseList.get(j).getStartTime().isAfter(takeCourseList.get(i).getEndTime())
							&& !takeCourseList.get(j).getEndTime().isBefore(takeCourseList.get(i).getStartTime())) {
						return new SubjectSystemResponse("選修課程時間衝突");
					}
				}
			}
		}

		// 檢查課程選修人數是否達上限
		for (Course takeCourse : takeCourseList) {
			if (takeCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse("課程已達選修人數限制");
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
			return new SubjectSystemResponse("選修學分已達上限");
		}
		student.setTotalCredits(takeCourseTotalCredits);
		studentDao.save(student);

		// 選課課程代號(字串)加進字串陣列
		List<String> takeCourseNumList = new ArrayList<>();
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			takeCourseNumList.add(item);
		}

		List<Course> finallyTakeCouseList = courseDao.findAllByCourseNumberIn(takeCourseNumList);
		// 將陣列資料轉字串存進學生資訊
		String allCourseStr = takeCourseNumList.toString();
		// 選課字串陣列去前後中括號
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		studentDao.save(student);

//	 		(算選修人數)

		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int sltTotalStudent = 0;
		for (Course takeCourse : takeCourseList) {
			courseStd = takeCourse.getCourseStudent();
			// 原選修人數+1
			sltTotalStudent = courseStd + 1;
			// 將新選修總人數存進資料庫
			takeCourse.setCourseStudent(sltTotalStudent);
			courseDao.save(takeCourse);
		}
		return new SubjectSystemResponse(student, finallyTakeCouseList, "選修課程成功");
	}
	
//	加課

	// 選課
	@Override
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest) {

		// 判斷學號輸入內容格式
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// 判斷在 學生DB中 是否已存在這個學生ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("資料錯誤:此學生不存在");
		}

		// 檢查輸入課程代碼格式
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//		(檢查課程)
		if (studentOp.get().getTakeCourseNumber().isEmpty()) {
			return new SubjectSystemResponse("請先選課");
		}

//		檢查課程不存在
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(takeCourseList)) {
			return new SubjectSystemResponse("課程不存在");
		} else if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("課程不存在");
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
					return new SubjectSystemResponse("無法選修相同課程");
				} else if (stdCourse.getCourseName().equals(sltCourse.getCourseName())) {
					return new SubjectSystemResponse("無法選修相同課程");
				}
			}
		}
// 		(比較衝堂----檢查已選與欲選課程時間是否衝突)
		for (Course stdCourseTime : stdCourseList) { // (已選)
			LocalTime str1 = stdCourseTime.getStartTime();
			LocalTime end1 = stdCourseTime.getEndTime();
			for (Course sltCourseTime : sltCourseList) { // (加選)
				LocalTime str2 = sltCourseTime.getStartTime();
				LocalTime end2 = sltCourseTime.getEndTime();

				// 比對星期是否相同
				if (stdCourseTime.getWeekDay().equals(sltCourseTime.getWeekDay())) {
					// 比對課堂時間是否衝堂
					if (!str2.isAfter(end1) && !end2.isBefore(str1)) {
						return new SubjectSystemResponse("選修課程時間衝突");
					}
				}
			}
		}

//		(個別檢查選修人數是否達上限)
		for (Course sltCourse : sltCourseList) {
			if (sltCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse("課程已達選修人數限制");
			}
		}

// 		(檢查總學分)

		// 欲選總學分
		int sltTotalCredit = 0;
		// 學生總學分
//		int stdTotalCredit = studentDao.findById(subjectSystemRequest.getStudentNumber()).get().getTotalCredits();
		int stdTotalCredit = studentOp.get().getTotalCredits();

		// 欲選學分加總
		for (Course sltCourse : sltCourseList) {
			// 欲選課程學分撈出來加總
			sltTotalCredit += sltCourse.getCredits();

		}
		// 學生最終總學分
		int finalTotalCredit = sltTotalCredit + stdTotalCredit;

		// 檢查學生最終總學分是否已達上限
		if (finalTotalCredit >= 10) {
			return new SubjectSystemResponse("選修學分已達上限");
		}
		student.setTotalCredits(finalTotalCredit);
		studentDao.save(student);

//(存取已選+加選的課程)	

		List<String> allCourseList = new ArrayList<>();
		List<String> stdCList = new ArrayList<>();
		List<String> sltCList = new ArrayList<>();
		// 從Course撈課程代碼(字串)加進(已選)字串陣列
		for (Course item : stdCourseList) {
			stdCList.add(item.getCourseNumber());
		}
		// 從Course撈課程代碼(字串)加進(新選)字串陣列
		for (Course item : sltCourseList) {
			sltCList.add(item.getCourseNumber());
		}
		// 將已選課程and新選課程加進同一陣列
		allCourseList.addAll(stdCList);
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

		return new SubjectSystemResponse(student, finallyCouseList, "加選課程成功");
	}
	
//	退課

	// 退課
	@Override
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest) {
		// 檢查學生
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("此學生不存在");
		}

		// 檢查課程代碼
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}
//		檢查課程不存在
		List<Course> dropCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(dropCourseList)) {
			return new SubjectSystemResponse("課程不存在");
		} else if (dropCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("課程不存在");
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
				return new SubjectSystemResponse("未選修此課程");
			}
		}

		// 退課
		// 原選課程內 想退選課程以外的課程 >最終想選課程
		Set<String> dropCourseNumStrSet = new HashSet<>();
		for (String dropCourse : dropCourseNumStrList) {
			dropCourseNumStrSet.add(dropCourse);
		}
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

//		課程資料 扣減選修人數
		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int newCourseStd = 0;
		for (Course course : dropCourseList) {
			courseStd = course.getCourseStudent();
			newCourseStd = courseStd - 1;
			course.setCourseStudent(newCourseStd);
			courseDao.save(course);
		}

		return new SubjectSystemResponse(student, finallyTakeCouseList, "退選成功");
	}
	
//	刪除學生

	// 刪除學生
	@Override
	public SubjectSystemResponse deleteStudent(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("資料錯誤:此學生不存在");
		}

		// 若無選課 > 直接刪除學生資料
		if (studentOp.get().getTakeCourseNumber().isBlank()) {
			studentDao.deleteById(subjectSystemRequest.getStudentNumber());
		}

//		課程資料 扣減選修人數
		List<String> takeCourseStrList = new ArrayList<>();
		String[] takeCourseStringAry = studentOp.get().getTakeCourseNumber().split(",");
		for (String item : takeCourseStringAry) {
			takeCourseStrList.add(item.trim());
		}

		List<Course> takeCourseInfo = courseDao.findAllById(takeCourseStrList);
		// 原選修人數
		int courseStd = 0;
		// 新選修人數
		int newCourseStd = 0;
		for (Course course : takeCourseInfo) {
			courseStd = course.getCourseStudent();
			newCourseStd = courseStd - 1;
			course.setCourseStudent(newCourseStd);
			courseDao.save(course);
		}

		studentDao.deleteById(subjectSystemRequest.getStudentNumber());

		return new SubjectSystemResponse("刪除學生成功");
	}
	
//	-----------------------------抽取方法-----------------------------
	

//  -----------------------------抽取方法-------------------------------
	private SubjectSystemResponse checkStudentNumber(String subjectSystemRequest) {
		// 檢查學號
		if (subjectSystemRequest.isBlank()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.matches(pattern)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		return null;
	}

	private SubjectSystemResponse checkCourseNumber(String subjectSystemRequest) {
		// 檢查課程代碼格式
		if (subjectSystemRequest.isBlank()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.matches(pattern)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		return null;
	}

}
