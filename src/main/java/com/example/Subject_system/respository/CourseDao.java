package com.example.Subject_system.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Subject_system.entity.Course;
@Repository
public interface CourseDao extends JpaRepository<Course, String>{

}
