package com.example.Subject_system.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Subject_system.entity.Course;
@Repository
public interface CourseDao extends JpaRepository<Course, String>{
	
	public List<Course> findByCourseNameContainingIgnoreCase(String courseName);
	
	public List<Course> findAllByCourseNumberIn(List<String> courseNumberList);
	
//	public List<Course> findAllBystudentCourseNumberIn(List<String> studentCourseNumberList);
}
