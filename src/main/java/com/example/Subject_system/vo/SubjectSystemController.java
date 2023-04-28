package com.example.Subject_system.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.example.Subject_system.service.ifs.SubjectSystemService;

@RestController
public class SubjectSystemController {

	@Autowired
	private SubjectSystemService subjectSystemService;
	
	
	
}
