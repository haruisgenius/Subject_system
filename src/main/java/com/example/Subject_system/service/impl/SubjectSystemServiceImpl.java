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

//	�s�W�ҵ{
	@Override
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// �ˬd�ҵ{�W��
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�W�٬���");
		}

		// �ˬd�P��
		List<String> week = Arrays.asList("�@", "�G", "�T", "�|", "��");
		if (subjectSystemRequest.getWeekDay().isEmpty()) {
			return new SubjectSystemResponse("��ƿ��~:�W�Ҥ������");
		}
		if (!week.contains(subjectSystemRequest.getWeekDay())) {
			return new SubjectSystemResponse("��ƿ��~:�W�Ҥ�����~");
		}

		// �ˬd�W�Үɶ�and�U�Үɶ�
		if (subjectSystemRequest.getStartTime().getHour() < 8 || subjectSystemRequest.getStartTime().getHour() > 20
				|| subjectSystemRequest.getEndTime().getHour() < 8
				|| subjectSystemRequest.getEndTime().getHour() > 20) {
			return new SubjectSystemResponse("��ƿ��~:�W�Үɶ����~");
		}
		if (subjectSystemRequest.getStartTime().getHour() > subjectSystemRequest.getEndTime().getHour()) {
			return new SubjectSystemResponse("��ƿ��~:�W�Үɶ��榡���~");
		}

		// �ˬd�Ǥ���
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse("��ƿ��~:�Ǥ����~");
		}

		// �ˬd�N�X�O�_�w�s�b
		if (courseDao.existsById(subjectSystemRequest.getCourseNumber())) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�N�X����");
		}
		// �s��Ʈw
		Course saveCourse = new Course(subjectSystemRequest.getCourseNumber(), subjectSystemRequest.getCourseName(),
				subjectSystemRequest.getWeekDay(), subjectSystemRequest.getStartTime(),
				subjectSystemRequest.getEndTime(), subjectSystemRequest.getCredits());
		courseDao.save(saveCourse);
		return new SubjectSystemResponse(saveCourse, "�s�W�ҵ{���\");
	}

//	�s�W�ǥ�
	@Override
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�Ǹ�
		if (subjectSystemRequest.getStudentNumber().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�Ǹ�����");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getStudentNumber().matches(pattern)) {
			return new SubjectSystemResponse("��ƿ��~:�Ǹ��榡����");
		}
		// �ˬd�ǥͩm�W�O�_����
		if (subjectSystemRequest.getStudentName().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�m�W����");
		}
		// �Ǹ��O�_����
		if (studentDao.existsById(subjectSystemRequest.getStudentNumber())) {
			return new SubjectSystemResponse("��ƿ��~:�ǥ͸�Ƥw�s�b");
		}
		// �s��Ʈw
		Student saveStudent = new Student(subjectSystemRequest.getStudentNumber(),
				subjectSystemRequest.getStudentName());
		studentDao.save(saveStudent);
		return new SubjectSystemResponse(saveStudent, "�s�W�ǥͦ��\");
	}

//	�R���ҵ{
	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		}
		// �ˬd���ҵ{�׽ҤH�ƬO�_>0
		if (op.get().getCourseStudent() > 0) {
			return new SubjectSystemResponse("�ҵ{�L�k�R��");
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse("�R���ҵ{���\");
	}

//	�d�߽ҵ{(�ҵ{�N�X)
	@Override
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkCourseNumber(subjectSystemRequest.getCourseNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if (!op.isPresent()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		}
		Course courseInfo = op.get();
		return new SubjectSystemResponse(courseInfo, "���o�ҵ{��Ʀ��\");
	}

//	�d�߽ҵ{(�ҵ{�W��)
	@Override
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ҵ{�W��
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�W�٬���");
		}
		List<Course> findCourseNameList = courseDao
				.findByCourseNameContainingIgnoreCase(subjectSystemRequest.getCourseName());
		// �ˬd�ҵ{�O�_�s�b
		if (findCourseNameList.isEmpty()) {
			return new SubjectSystemResponse("��ƿ��~:�����ҵ{���s�b");
		}
		return new SubjectSystemResponse(findCourseNameList, "���o�ҵ{��Ʀ��\");
	}

//	�ק�ҵ{
	@Override
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest) {
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
		if (subjectSystemRequest.getCourseName().isBlank()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// �ˬd�P��
		List<String> week = Arrays.asList("�@", "�G", "�T", "�|", "��");
		if (subjectSystemRequest.getWeekDay().isEmpty()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (!week.contains(subjectSystemRequest.getWeekDay())) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// �ˬd�W�Үɶ�and�U�Үɶ�
		if (subjectSystemRequest.getStartTime().getHour() < 8 || subjectSystemRequest.getStartTime().getHour() > 20
				|| subjectSystemRequest.getEndTime().getHour() < 8
				|| subjectSystemRequest.getEndTime().getHour() > 20) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}
		if (subjectSystemRequest.getStartTime().getHour() >= subjectSystemRequest.getEndTime().getHour()) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		// �ˬd�Ǥ���
		if (subjectSystemRequest.getCredits() < 1 || subjectSystemRequest.getCredits() > 3) {
			return new SubjectSystemResponse(RtnCode.DATA_ERROR.getMessage());
		}

		List<Student> allStudentInfoList = studentDao.findAll();
		//�Y��Ʈw�L�ǥ� > �����ק�ҵ{���
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

		for (Student studentInfo : allStudentInfoList) {
			//�Y�ǥ͵L��׽ҵ{ > ���L
			if (studentInfo.getTakeCourseNumber() == null) {
				continue;
			}else if (studentInfo.getTakeCourseNumber().contains(subjectSystemRequest.getCourseNumber())) {
				// �ǥͭ��`�Ǥ�
				int studentCredit = studentInfo.getTotalCredits();
				// �ǥͷs�`�Ǥ�
				int newStudentCredit = (studentCredit - orginalCredit) + newCredeit;
				studentInfo.setTotalCredits(newStudentCredit);
			}
		}
		
		//�ק��Ʀs�i��ƪ�
		Course updateCourse = op.get();
		updateCourse.updateCourse(subjectSystemRequest.getCourseName(), subjectSystemRequest.getWeekDay(),
				subjectSystemRequest.getStartTime(), subjectSystemRequest.getEndTime(),
				subjectSystemRequest.getCredits());
		//�ק��ƪ�s�i��Ʈw
		courseDao.save(updateCourse);

		//�s�ǥͭק�Ǥ�����
		studentDao.saveAll(allStudentInfoList);

		return new SubjectSystemResponse(updateCourse, RtnCode.SUCCESSFUL.getMessage());
	}

//	���
	@Override
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest) {
		// �P�_�Ǹ���J���e�榡
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("��ƿ��~:���ǥͤ��s�b");
		}

		// �ˬd��J�ҵ{�N�X�榡
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//			�ҵ{���s�b
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(takeCourseList)) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		} else if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		}

		// �ˬd�ҵ{�N�X�O�_����
		for (int i = 0; i < takeCourseList.size(); i++) {
			for (int j = i + 1; j < takeCourseList.size(); j++) {
				if (takeCourseList.get(i).getCourseNumber().equals(takeCourseList.get(j).getCourseNumber())) {
					return new SubjectSystemResponse("�L�k��׬ۦP�ҵ{");
				} else if (takeCourseList.get(i).getCourseName()
						.equalsIgnoreCase(takeCourseList.get(j).getCourseName())) {
					return new SubjectSystemResponse("�L�k��׬ۦP�ҵ{");
				}
			}
		}

		// �ˬd�ҵ{�O�_�İ�
		for (int i = 0; i < takeCourseList.size(); i++) {
			for (int j = i + 1; j < takeCourseList.size(); j++) {
				if (takeCourseList.get(i).getWeekDay().equals(takeCourseList.get(j).getWeekDay())) {
					if (!takeCourseList.get(j).getStartTime().isAfter(takeCourseList.get(i).getEndTime())
							&& !takeCourseList.get(j).getEndTime().isBefore(takeCourseList.get(i).getStartTime())) {
						return new SubjectSystemResponse("��׽ҵ{�ɶ��Ĭ�");
					}
				}
			}
		}

		// �ˬd�ҵ{��פH�ƬO�_�F�W��
		for (Course takeCourse : takeCourseList) {
			if (takeCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse("�ҵ{�w�F��פH�ƭ���");
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
			return new SubjectSystemResponse("��׾Ǥ��w�F�W��");
		}
		student.setTotalCredits(takeCourseTotalCredits);
		studentDao.save(student);

		// ��ҽҵ{�N��(�r��)�[�i�r��}�C
		List<String> takeCourseNumList = new ArrayList<>();
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			takeCourseNumList.add(item);
		}

		List<Course> finallyTakeCouseList = courseDao.findAllByCourseNumberIn(takeCourseNumList);
		// �N�}�C�����r��s�i�ǥ͸�T
		String allCourseStr = takeCourseNumList.toString();
		// ��Ҧr��}�C�h�e�ᤤ�A��
		student.setTakeCourseNumber(allCourseStr.substring(1, allCourseStr.length() - 1));
		studentDao.save(student);

//	 		(���פH��)

		// ���פH��
		int courseStd = 0;
		// �s��פH��
		int sltTotalStudent = 0;
		for (Course takeCourse : takeCourseList) {
			courseStd = takeCourse.getCourseStudent();
			// ���פH��+1
			sltTotalStudent = courseStd + 1;
			// �N�s����`�H�Ʀs�i��Ʈw
			takeCourse.setCourseStudent(sltTotalStudent);
			courseDao.save(takeCourse);
		}
		return new SubjectSystemResponse(student, finallyTakeCouseList, "��׽ҵ{���\");
	}
	
//	�[��

	// ���
	@Override
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest) {

		// �P�_�Ǹ���J���e�榡
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}

		// �P�_�b �ǥ�DB�� �O�_�w�s�b�o�Ӿǥ�ID
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("��ƿ��~:���ǥͤ��s�b");
		}

		// �ˬd��J�ҵ{�N�X�榡
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}

//		(�ˬd�ҵ{)
		if (studentOp.get().getTakeCourseNumber().isEmpty()) {
			return new SubjectSystemResponse("�Х����");
		}

//		�ˬd�ҵ{���s�b
		List<Course> takeCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(takeCourseList)) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		} else if (takeCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
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
					return new SubjectSystemResponse("�L�k��׬ۦP�ҵ{");
				} else if (stdCourse.getCourseName().equals(sltCourse.getCourseName())) {
					return new SubjectSystemResponse("�L�k��׬ۦP�ҵ{");
				}
			}
		}
// 		(����İ�----�ˬd�w��P����ҵ{�ɶ��O�_�Ĭ�)
		for (Course stdCourseTime : stdCourseList) { // (�w��)
			LocalTime str1 = stdCourseTime.getStartTime();
			LocalTime end1 = stdCourseTime.getEndTime();
			for (Course sltCourseTime : sltCourseList) { // (�[��)
				LocalTime str2 = sltCourseTime.getStartTime();
				LocalTime end2 = sltCourseTime.getEndTime();

				// ���P���O�_�ۦP
				if (stdCourseTime.getWeekDay().equals(sltCourseTime.getWeekDay())) {
					// ���Ұ�ɶ��O�_�İ�
					if (!str2.isAfter(end1) && !end2.isBefore(str1)) {
						return new SubjectSystemResponse("��׽ҵ{�ɶ��Ĭ�");
					}
				}
			}
		}

//		(�ӧO�ˬd��פH�ƬO�_�F�W��)
		for (Course sltCourse : sltCourseList) {
			if (sltCourse.getCourseStudent() >= 3) {
				return new SubjectSystemResponse("�ҵ{�w�F��פH�ƭ���");
			}
		}

// 		(�ˬd�`�Ǥ�)

		// �����`�Ǥ�
		int sltTotalCredit = 0;
		// �ǥ��`�Ǥ�
//		int stdTotalCredit = studentDao.findById(subjectSystemRequest.getStudentNumber()).get().getTotalCredits();
		int stdTotalCredit = studentOp.get().getTotalCredits();

		// ����Ǥ��[�`
		for (Course sltCourse : sltCourseList) {
			// ����ҵ{�Ǥ����X�ӥ[�`
			sltTotalCredit += sltCourse.getCredits();

		}
		// �ǥͳ̲��`�Ǥ�
		int finalTotalCredit = sltTotalCredit + stdTotalCredit;

		// �ˬd�ǥͳ̲��`�Ǥ��O�_�w�F�W��
		if (finalTotalCredit >= 10) {
			return new SubjectSystemResponse("��׾Ǥ��w�F�W��");
		}
		student.setTotalCredits(finalTotalCredit);
		studentDao.save(student);

//(�s���w��+�[�諸�ҵ{)	

		List<String> allCourseList = new ArrayList<>();
		List<String> stdCList = new ArrayList<>();
		List<String> sltCList = new ArrayList<>();
		// �qCourse���ҵ{�N�X(�r��)�[�i(�w��)�r��}�C
		for (Course item : stdCourseList) {
			stdCList.add(item.getCourseNumber());
		}
		// �qCourse���ҵ{�N�X(�r��)�[�i(�s��)�r��}�C
		for (Course item : sltCourseList) {
			sltCList.add(item.getCourseNumber());
		}
		// �N�w��ҵ{and�s��ҵ{�[�i�P�@�}�C
		allCourseList.addAll(stdCList);
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

		return new SubjectSystemResponse(student, finallyCouseList, "�[��ҵ{���\");
	}
	
//	�h��

	// �h��
	@Override
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ǥ�
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		Student student = studentOp.get();

		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("���ǥͤ��s�b");
		}

		// �ˬd�ҵ{�N�X
		for (String item : subjectSystemRequest.getCourseNumberList()) {
			SubjectSystemResponse checkCResult = checkCourseNumber(item);
			if (checkCResult != null) {
				return checkCResult;
			}
		}
//		�ˬd�ҵ{���s�b
		List<Course> dropCourseList = courseDao.findAllById(subjectSystemRequest.getCourseNumberList());
		if (CollectionUtils.isEmpty(dropCourseList)) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		} else if (dropCourseList.size() != subjectSystemRequest.getCourseNumberList().size()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
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
				return new SubjectSystemResponse("����צ��ҵ{");
			}
		}

		// �h��
		// ���ҵ{�� �Q�h��ҵ{�H�~���ҵ{ >�̲׷Q��ҵ{
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

//		�ҵ{��� �����פH��
		// ���פH��
		int courseStd = 0;
		// �s��פH��
		int newCourseStd = 0;
		for (Course course : dropCourseList) {
			courseStd = course.getCourseStudent();
			newCourseStd = courseStd - 1;
			course.setCourseStudent(newCourseStd);
			courseDao.save(course);
		}

		return new SubjectSystemResponse(student, finallyTakeCouseList, "�h�令�\");
	}
	
//	�R���ǥ�

	// �R���ǥ�
	@Override
	public SubjectSystemResponse deleteStudent(SubjectSystemRequest subjectSystemRequest) {
		SubjectSystemResponse checkResult = checkStudentNumber(subjectSystemRequest.getStudentNumber());
		if (checkResult != null) {
			return checkResult;
		}
		Optional<Student> studentOp = studentDao.findById(subjectSystemRequest.getStudentNumber());
		if (!studentOp.isPresent()) {
			return new SubjectSystemResponse("��ƿ��~:���ǥͤ��s�b");
		}

		// �Y�L��� > �����R���ǥ͸��
		if (studentOp.get().getTakeCourseNumber().isBlank()) {
			studentDao.deleteById(subjectSystemRequest.getStudentNumber());
		}

//		�ҵ{��� �����פH��
		List<String> takeCourseStrList = new ArrayList<>();
		String[] takeCourseStringAry = studentOp.get().getTakeCourseNumber().split(",");
		for (String item : takeCourseStringAry) {
			takeCourseStrList.add(item.trim());
		}

		List<Course> takeCourseInfo = courseDao.findAllById(takeCourseStrList);
		// ���פH��
		int courseStd = 0;
		// �s��פH��
		int newCourseStd = 0;
		for (Course course : takeCourseInfo) {
			courseStd = course.getCourseStudent();
			newCourseStd = courseStd - 1;
			course.setCourseStudent(newCourseStd);
			courseDao.save(course);
		}

		studentDao.deleteById(subjectSystemRequest.getStudentNumber());

		return new SubjectSystemResponse("�R���ǥͦ��\");
	}
	
//	-----------------------------�����k-----------------------------
	

//  -----------------------------�����k-------------------------------
	private SubjectSystemResponse checkStudentNumber(String subjectSystemRequest) {
		// �ˬd�Ǹ�
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
		// �ˬd�ҵ{�N�X�榡
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
