package com.example.Subject_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Subject_system.service.ifs.SubjectSystemService;
import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

@RestController
public class SubjecSystemController {

	@Autowired
	private SubjectSystemService subjectSystemService;
	
	@PostMapping(value = "creat_course")
	public SubjectSystemResponse createCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.createCourse(subjectSystemRequest);
	}
	
	@PostMapping(value = "add_student")
	public SubjectSystemResponse addStudent(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.addStudent(subjectSystemRequest);
	}
	
	@PostMapping(value = "delete_course")
	public SubjectSystemResponse deleteCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.deleteCourse(subjectSystemRequest);
	}
	
	@PostMapping(value = "find_course_by_course_number")
	public SubjectSystemResponse findCourseByCourseNumber(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.findCourseByCourseNumber(subjectSystemRequest);
	}
	
	@PostMapping(value = "find_course_by_course_name")
	public SubjectSystemResponse findCourseByCourseName(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.findCourseByCourseName(subjectSystemRequest);
	}
	
	@PostMapping(value = "update_course")
	public SubjectSystemResponse updateCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.updateCourse(subjectSystemRequest);
	}

	//¿ï½Ò
	@PostMapping(value = "take_course")
	public SubjectSystemResponse takeCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.takeCourse(subjectSystemRequest);
	}
	
	//¥[¿ï½Ò
	@PostMapping(value = "select_course")
	public SubjectSystemResponse selectCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.selectCourse(subjectSystemRequest);
	}
	
	@PostMapping(value = "drop_course")
	public SubjectSystemResponse dropCourse(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.dropCourse(subjectSystemRequest);
	}
	
	@PostMapping(value = "delete_student")
	public SubjectSystemResponse deleteStudent(@RequestBody SubjectSystemRequest subjectSystemRequest) {
		return subjectSystemService.deleteStudent(subjectSystemRequest);
	}
	
}
