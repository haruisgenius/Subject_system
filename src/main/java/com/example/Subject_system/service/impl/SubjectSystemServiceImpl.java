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
		// �ˬd�ҵ{�N�X�榡
		if (subjectSystemRequest.getCourseNumber().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�N�X����");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getCourseNumber().matches(pattern)) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�N�X�榡����");
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
		if (subjectSystemRequest.getStartTime() <= 0 || subjectSystemRequest.getStartTime() > 24
				|| subjectSystemRequest.getEndTime() <= 0 || subjectSystemRequest.getEndTime() > 24) {
			return new SubjectSystemResponse("��ƿ��~:�W�Үɶ����~");
		}
		if (subjectSystemRequest.getStartTime() > subjectSystemRequest.getEndTime()) {
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

	@Override
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest) {
		// �ˬd�ҵ{�N�X�榡
		if (subjectSystemRequest.getCourseNumber().isBlank()) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�N�X����");
		}
		String pattern = "[\\w^_]{5,8}";
		if (!subjectSystemRequest.getCourseNumber().matches(pattern)) {
			return new SubjectSystemResponse("��ƿ��~:�ҵ{�N�X�榡����");
		}
		//�ˬd�ҵ{�O�_�s�b
		Optional<Course> op = courseDao.findById(subjectSystemRequest.getCourseNumber());
		if(op.isEmpty()) {
			return new SubjectSystemResponse("�ҵ{���s�b");
		}
		//�ˬd���ҵ{�׽ҤH�ƬO�_>0
		Course courseInfo = op.get();
		if( courseInfo.getCourseStudent() > 0) {
			return new SubjectSystemResponse("�ҵ{�L�k�R��");
		}
		courseDao.deleteById(subjectSystemRequest.getCourseNumber());
		return new SubjectSystemResponse("�R���ҵ{���\");
	}

}
