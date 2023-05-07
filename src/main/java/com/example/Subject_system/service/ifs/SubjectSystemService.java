package com.example.Subject_system.service.ifs;

import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

public interface SubjectSystemService {
	//創課程
	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest);
	
	//加學生資料
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest);
	
	//刪課程
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest);
	
	//用課程代碼找課程
	public SubjectSystemResponse findCourseByCourseNumber(SubjectSystemRequest subjectSystemRequest);
	
	//用課程名稱找課程
	public SubjectSystemResponse findCourseByCourseName(SubjectSystemRequest subjectSystemRequest);
	
	//修改課程
	public SubjectSystemResponse updateCourse(SubjectSystemRequest subjectSystemRequest);
	
	//選課
	public SubjectSystemResponse takeCourse(SubjectSystemRequest subjectSystemRequest);
	
	//加課
	public SubjectSystemResponse selectCourse(SubjectSystemRequest subjectSystemRequest);
	
	//退選
	public SubjectSystemResponse dropCourse(SubjectSystemRequest subjectSystemRequest);
	
	//刪除學生
	public SubjectSystemResponse deleteStudent(SubjectSystemRequest subjectSystemRequest);

	//查詢學生
	public SubjectSystemResponse findStudent(SubjectSystemRequest subjectSystemRequest);
}
