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
		return subjectSystemService.createCourse(subjectSystemRequest);
	}
	
	
	
}
