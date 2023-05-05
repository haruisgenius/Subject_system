package com.example.Subject_system.respository;



import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Subject_system.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, String> {
	
//	public List<Course> saveAllById(String courseNumber);
	
//	List<Student> findAllByTakeCourseNumberIn(List<String> stdCourseList);
	
}
