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

//	�s�W�ҵ{
	@Override
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ҵ{�N�X�榡 > ���k
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// �ˬd�ҵ{�W��
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		// �ˬd�W�Үɶ�and�U�Үɶ� > ���k
		SubjectSystemResponse checkTimeResult = checkWeekdayAndStartOrEndTime(subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime());
		if (checkTimeResult != null) {
			return checkTimeResult;
		}

		// �ˬd�Ǥ��� > ���i�p��1�Τj��3
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		// �ˬd�N�X�O�_�w�s�b
		if (courseDao.existsById(subjectSystemRequest.getCourseNumber())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// �s��Ʈw
		Course saveCourse = new Course(subjectSystemRequest.getCourseNumber(), subjectSystemRequest.getCourseName(),
				subjectSystemRequest.getWeekDay(), subjectSystemRequest.getStartTime(),
				subjectSystemRequest.getEndTime(), subjectSystemRequest.getCredits());
		courseDao.save(saveCourse);
		return new SubjectSystemResponse(saveCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	�s�W�ǥ�
	@Override
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�Ǹ� > ���k
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ǥͩm�W�O�_����
		if (!StringUtils.hasText(subjectSystemRequest.getStudentName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// �Ǹ��O�_����
		if (studentDao.existsById(subjectSystemRequest.getStudentNumber())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// �s��Ʈw
		Student saveStudent = new Student(subjectSystemRequest.getStudentNumber(),
				subjectSystemRequest.getStudentName());
		studentDao.save(saveStudent);
		return new SubjectSystemResponse(saveStudent, RtnCode.SUCCESSFUL.getMessage());
	}

//	�R���ҵ{
	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd��J�ҵ{�N�X�榡 > ���k
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		// �ˬd���ҵ{�׽ҤH�ƬO�_>0
		if (op.get().getCourseStudent() > 0) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse(RtnCode.SUCCESSFUL.getMessage());
	}

//	�d�߽ҵ{(�ҵ{�N�X)
	@Override
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd��J�ҵ{�N�X�榡 > ���k
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Course courseInfo = op.get();
		return new SubjectSystemResponse(courseInfo, RtnCode.SUCCESSFUL.getMessage());
	}

//	�d�߽ҵ{(�ҵ{�W��)
	@Override
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ҵ{�W��
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		// �ҽk�j�M
		List<Course> findCourseNameList = courseDao
				.findByCourseNameContainingIgnoreCase(subjectSystemRequest.getCourseName());
		// �ˬd�ҵ{�O�_�s�b
		if (findCourseNameList.isEmpty()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		return new SubjectSystemResponse(findCourseNameList, RtnCode.SUCCESSFUL.getMessage());
	}

//	�ק�ҵ{
	@Override
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd��J�ҵ{�N�X�榡 > ���k
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		// �ˬd�ҵ{�W��
		if (!StringUtils.hasText(subjectSystemRequest.getCourseName())) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// �ˬd�W�Үɶ�and�U�Үɶ� > ���k
		SubjectSystemResponse checkTimeResult = checkWeekdayAndStartOrEndTime(subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime());
		if (checkTimeResult != null) {
			return checkTimeResult;
		}

		// �ˬd�Ǥ���
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// ���Ҧ��ǥ͸��
		List<Student> allStudentInfoList = studentDao.findAll();
		// �Y��Ӹ�Ʈw�L�ǥ�(�ǥͤH��=0) > �����ק�ҵ{���
		if (CollectionUtils.isEmpty(allStudentInfoList)) {
			Course updateCourse = op.get();
			updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
					subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
					subjectSystemRequest.getCredits());
			courseDao.save(updateCourse);
			return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
		}

		// �ק�e�Ǥ�
		int orginalCredit = op.get().getCredits();
		// �ק��Ǥ�
		int newCredeit = subjectSystemRequest.getCredits();
		// �M���ǥ͸��
		for (Student studentInfo : allStudentInfoList) {
			// �Y�ǥ͵L��׽ҵ{ > ���L
			if (studentInfo.getTakeCourseNumber() == null) {
				continue;
			} else if (studentInfo.getTakeCourseNumber().contains(subjectSystemRequest.getCourseNumber())) {
				// �ǥͭ��`�Ǥ�
				int studentCredit = studentInfo.getTotalCredits();
				// �ǥͷs�`�Ǥ� > ���`�Ǥ� - �ק�e�Ǥ� + �ק��Ǥ�
				int newStudentCredit = (studentCredit - orginalCredit) + newCredeit;
				// �s�i�ǥ͸��
				studentInfo.setTotalCredits(newStudentCredit);
			}
		}

		// �ק��Ʀs�i��ƪ�
		Course updateCourse = op.get();
		updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
				subjectSystemRequest.getCredits());
		// �ק��ƪ�s�i��Ʈw
		courseDao.save(updateCourse);

		// �s�ǥͭק�Ǥ����ƶi��Ʈw
		studentDao.saveAll(allStudentInfoList);

		return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	���
	@Override
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest) {
		// �P�_�Ǹ���J���e�榡 > ���k
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// �ˬd��J�ҵ{�N�X�榡 > ���k
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

		// �ˬd�ҵ{�O�_�s�b
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// �ˬd�ҵ{�N�X&�W�٬O�_����
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

		// �ˬd�ҵ{�O�_�İ� > ���k
		SubjectSystemResponse checkCrashCourseResult = crashCourse(takeCourseList, takeCourseList);
		if(checkCrashCourseResult != null) {
			return checkCrashCourseResult;
		}

		// �ˬd�ҵ{��פH�ƬO�_�F�W��
		for (Course takeCourse : takeCourseList) {
			if (takeCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
			}
		}

		// �[���`�Ǥ�
		int takeCourseTotalCredits = 0;
		// ����Ǥ��[�`
		for (Course takeCourse : takeCourseList) {
			// ����ҵ{�Ǥ����X�ӥ[�`
			takeCourseTotalCredits += takeCourse.getCredits();
		}
		// �ˬd�ǥͳ̲��`�Ǥ��O�_�w�F�W��
		if (takeCourseTotalCredits >= 10) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		student.setTotalCredits(takeCourseTotalCredits);

		// �n�^�Ǫ��ҵ{�Բ�
		List<Course> finallyTakeCouseList = courseDao
				.findAllByCourseNumberIn(subjectSystemRequest.getCourseNumberList());
		// �N�}�C�����r��s�i�ǥ͸�T
		String allCourseStr = subjectSystemRequest.getCourseNumberList().toString();
		// ��Ҧr��}�C�h�e�ᤤ�A��
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		studentDao.save(student);

//	 		(���פH��)
//TODO ��ƮwsaveAll
		// ���פH��
		int courseStd = 0;
		// �s��פH��
		int sltTotalStudent = 0;
		List<Course> allCourseList = courseDao.findAll();
		for (Course takeCourse : takeCourseList) {
			for (Course allCourse : allCourseList) {
				if (takeCourse.getCourseNumber().equals(allCourse.getCourseNumber())) {
					courseStd = allCourse.getCourseStudent();
					// ���פH��+1
					sltTotalStudent = courseStd + 1;
					// �N�s����`�H�Ʀs�i��Ʈw
					allCourse.setCourseStudent(sltTotalStudent);
				}
			}
		}
		courseDao.saveAll(allCourseList);
		return new SubjectSystemResponse(student, finallyTakeCouseList, RtnCode.SUCCESSFUL.getMessage());
	}

//	�[��
	@Override
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest) {

		// �P�_�Ǹ���J���e�榡
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// �ˬd��J�ҵ{�N�X�榡
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//		(�ˬd�O�_�w��׽ҵ{)
		if (studentOp.get().getTakeCourseNumber().isEmpty()) {
			return new SubjectSystemResponse(RtnCode.TAKE_COURSE_NOTYET.getMessage());
		}

//		�ˬd�ҵ{���s�b
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// ��ǥͤw��ҵ{�N���ӧO���X�s�i�}�C
		String[] stdCourseNumStr = studentOp.get().getTakeCourseNumber().split(",");
		List<String> stdCourseNumStrList = new ArrayList<>(); // �ǥͤw��ҵ{�N�X �ƭ�
		for (String item : stdCourseNumStr) {
			stdCourseNumStrList.add(item.trim());
		}

		// (�w��)�ǥͪ��ҵ{�N�Xand�ҵ{�W��
		List<Course> stdCourseList = courseDao.findAllByCourseNumberIn(stdCourseNumStrList);
		// (�[��)�q��Ʈw������ҵ{��T�s�icourseList
		List<Course> sltCourseList = courseDao.findAllByCourseNumberIn(subjectSystemRequest.getCourseNumberList());

		// ����ǥͬO�_���ƿ�� > �ǥͤw��P����ҵ{�N�X�O�_����or�W�٬O�_�ۦP
		for (Course stdCourse : stdCourseList) {
			for (Course sltCourse : sltCourseList) {
				if (stdCourse.getCourseNumber().equals(sltCourse.getCourseNumber())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				} else if (stdCourse.getCourseName().equals(sltCourse.getCourseName())) {
					return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
				}
			}
		}
		
//		����[��ҵ{�İ�
		SubjectSystemResponse checkSltCrashCourseResult = crashCourse(sltCourseList, sltCourseList);
		if(checkSltCrashCourseResult != null) {
			return checkSltCrashCourseResult;
		}

// 		(����İ�----�ˬd�w��P����ҵ{�ɶ��O�_�Ĭ�)
		SubjectSystemResponse checkCrashCourseResult = crashCourse(stdCourseList, sltCourseList);
		if(checkCrashCourseResult != null) {
			return checkCrashCourseResult;
		}

//		(�ӧO�ˬd��פH�ƬO�_�F�W��)
		for (Course sltCourse : sltCourseList) {
			if (sltCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
			}
		}

// 		(�ˬd�`�Ǥ�)

		// �����`�Ǥ�
		int sltTotalCredit = 0;
		// �ǥ��`�Ǥ�
		int stdTotalCredit = studentOp.get().getTotalCredits();

		// ����Ǥ��[�`
		for (Course sltCourse : sltCourseList) {
			// ����ҵ{�Ǥ����X�ӥ[�`
			sltTotalCredit += sltCourse.getCredits();

		}
		// �ǥͳ̲��`�Ǥ�
		int finalTotalCredit = sltTotalCredit + stdTotalCredit;

		// �ˬd�ǥͳ̲��`�Ǥ��O�_�w�F�W��
		if (finalTotalCredit > 10) {
			return new SubjectSystemResponse(RtnCode.UPDATE_NOT_ALLOW.getMessage());
		}
		student.setTotalCredits(finalTotalCredit);

//(�s���w��+�[�諸�ҵ{)	

		List<String> allCourseList = new ArrayList<>();
		List<String> sltCList = new ArrayList<>();

		// �qCourse���ҵ{�N�X(�r��)�[�i(�s��)�r��}�C
		for (Course item : sltCourseList) {
			sltCList.add(item.getCourseNumber());
		}
		// �N�w��ҵ{and�s��ҵ{�[�i�P�@�}�C
		allCourseList.addAll(stdCourseNumStrList);
		allCourseList.addAll(sltCList);
		List<Course> finallyCouseList = courseDao.findAllByCourseNumberIn(allCourseList);
		// �N�}�C�����r��s�i�ǥ͸�T
		String allCourseStr = allCourseList.toString();
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		// �N�ǥ͸�T�s�i��Ʈw
		studentDao.save(student);

// 		(���פH��)

		// ���פH��
		int courseStd = 0;
		// �s��פH��
		int sltTotalStudent = 0;
		for (Course sltCourse : sltCourseList) {
			courseStd = sltCourse.getCourseStudent();
			// ���פH��+1
			sltTotalStudent = courseStd + 1;
			// �N�s����`�H�Ʀs�i��Ʈw
			sltCourse.setCourseStudent(sltTotalStudent);
			courseDao.save(sltCourse);
		}

		return new SubjectSystemResponse(student, finallyCouseList, RtnCode.SUCCESSFUL.getMessage());
	}

	// �h��
	@Override
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ǥ�
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();

		// �ˬd�ҵ{�N�X
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}
//		�ˬd�ҵ{���s�b
		List<Course> dropCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (dropCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}

		// �ǥͤw��ҵ{�N�X(�r��)�s�i�r��}�C
		String[] takeCourseNumList = studentOp.get().getTakeCourseNumber().split(",");
		List<String> takeCourseNumStrList = new ArrayList<>();
		for (String item : takeCourseNumList) {
			takeCourseNumStrList.add(item.trim());
		}
		// �ǥͰh��ҵ{�N�X(�r��)�s�i�r��}�C
		List<String> dropCourseNumStrList = new ArrayList<>();
		for (String dropCourse : subjectSystemRequest.getCourseNumberList()) {
			dropCourseNumStrList.add(dropCourse);
		}

		// �ˬd�O�_����ױ��h�ҵ{
		for (String dropCourse : dropCourseNumStrList) {
			if (!takeCourseNumStrList.contains(dropCourse)) {
				return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
			}
		}

		// �h��
		// ���ҵ{�� �Q�h��ҵ{�H�~���ҵ{ >�̲׷Q��ҵ{
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
		// �n�^�Ǫ��̲׿�ҳ�
		List<Course> finallyTakeCouseList = courseDao.findAllById(finallyTakeCourseStrList);

		String finallyTakeCourseStr = finallyTakeCourseStrList.toString();
//		�ǥ͸�� ��s�̲׿��
		student.setTakeCourseNumber(finallyTakeCourseStr.substring(1, finallyTakeCourseStr.length() - 1));
//		studentDao.save(student);

		// �Ǥ�����
		int minusCredit = 0;
		int totalCredit = student.getTotalCredits();
		for (Course credit : dropCourseList) {
			minusCredit += credit.getCredits();
		}
		int finallyTotalCredit = totalCredit - minusCredit;
//		�ǥ͸�� ��s�̲��`�Ǥ�
		student.setTotalCredits(finallyTotalCredit);
//		�s�̲׾ǥ͸�� �i��Ʈw
		studentDao.save(student);

//TODO Dao�slist
//		�ҵ{��� �����פH��
		List<Course> allCourseInfoList = courseDao.findAll();
		// ���פH��
		int courseStd = 0;
		// �s��פH��
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


	// �R���ǥ�
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

		// �Y�L��� > �����R���ǥ͸��
		if (!StringUtils.hasText(studentOp.get().getTakeCourseNumber())) {
			studentDao.deleteById(subjectSystemRequest.getStudentNumber());
			return new SubjectSystemResponse(RtnCode.SUCCESSFUL.getMessage());
		}

//		�ҵ{��� �����פH��
		List<String> takeCourseStrList = new ArrayList<>();
		String[] takeCourseStringAry = studentOp.get().getTakeCourseNumber().split(",");
		for (String item : takeCourseStringAry) {
			takeCourseStrList.add(item.trim());
		}

//		TODO Dao�R��list
		List<Course> allCourseInfo = courseDao.findAll();
		// ���פH��
		int courseStd = 0;
		// �s��פH��
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


	// �d�߾ǥ�
	@Override
	public SubjectSystemResponse findStudent(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�Ǹ���J > ���k
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse(RtnCode.DATA_IS_EMPTY.getMessage());
		}
		Student student = studentOp.get();
		// �p�G�S���׽�
		if (!StringUtils.hasText(student.getTakeCourseNumber())) {
			return new SubjectSystemResponse(student, RtnCode.SUCCESSFUL.getMessage());
		}

		// �ǥͿ�׽ҵ{�[�Jlist
		String[] studentAllCourseStrAry = student.getTakeCourseNumber().split(",");
		List<String> studentAllCourseList = new ArrayList<>();
		for (String allCourseStr : studentAllCourseStrAry) {
			studentAllCourseList.add(allCourseStr.trim());
		}
		List<Course> StudentTakeCourse = courseDao.findAllById(studentAllCourseList);

		return new SubjectSystemResponse(student, StudentTakeCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	-----------------------------�����k-----------------------------

	private SubjectSystemResponse checkStudentNumber(String subjectSystemRequest) {
		// �ˬd�Ǹ�
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
		// �ˬd�ҵ{�N�X�榡
		if (!StringUtils.hasText(subjectSystemRequest)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.matches(pattern)) {
			return new SubjectSystemResponse(RtnCode.DATA_MISINPUT.getMessage());
		}

		return null;
	}

	// �ˬd�ҵ{�ɶ��榡
	private SubjectSystemResponse checkWeekdayAndStartOrEndTime(String subjectSystemRequestWeekday,
			LocalTime subjectSystemRequestStartTime, LocalTime subjectSystemRequestEndTime) {
		// �ˬd�P��
		List<String> week = Arrays.asList("�@", "�G", "�T", "�|", "��");
		if (!StringUtils.hasText(subjectSystemRequestWeekday)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (!week.contains(subjectSystemRequestWeekday)) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		// �ˬd�W�U�Үɶ�
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
