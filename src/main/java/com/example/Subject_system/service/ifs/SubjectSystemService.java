package com.example.Subject_system.service.ifs;

import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

public interface SubjectSystemService {

	public SubjectSystemResponse createCourse(SubjectSystemRequest subjectSystemRequest);
	
	public SubjectSystemResponse addStudent(SubjectSystemRequest subjectSystemRequest);
	
	public SubjectSystemResponse deleteCourse(SubjectSystemRequest subjectSystemRequest);
	
//	public SubjectSystemResponse
	
}
