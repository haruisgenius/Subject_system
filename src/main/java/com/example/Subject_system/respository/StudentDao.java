package com.example.Subject_system.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Subject_system.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, String> {

}
