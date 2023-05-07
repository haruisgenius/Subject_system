package com.example.Subject_system.service.ifs;

import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

public interface SubjectSystemService {
	//�нҵ{
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest);
	
	//�[�ǥ͸��
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest);
	
	//�R�ҵ{
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest);
	
	//�νҵ{�N�X��ҵ{
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest);
	
	//�νҵ{�W�٧�ҵ{
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest);
	
	//�ק�ҵ{
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest);
	
	//���
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest);
	
	//�[��
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest);
	
	//�h��
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest);
	
	//�R���ǥ�
	public SubjectSystemResponse deleteStudent(SubjectSystemRequest subjectSystemRequest);

	//�d�߾ǥ�
	public SubjectSystemResponse findStudent(SubjectSystemRequest subjectSystemRequest);
}
