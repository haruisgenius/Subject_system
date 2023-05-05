package com.example.Subject_system;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.Subject_system.entity.Course;
import com.example.Subject_system.entity.Student;
import com.example.Subject_system.respository.CourseDao;
import com.example.Subject_system.respository.StudentDao;
import com.example.Subject_system.service.ifs.SubjectSystemService;
import com.example.Subject_system.vo.SubjectSystemRequest;
import com.example.Subject_system.vo.SubjectSystemResponse;

@SpringBootTest(classes = SubjectSystemApplication.class)
public class SubjectSystemTest {
	
	@Autowired
	private SubjectSystemService subjectSystemService;
	
	
	//新增課程test
	@Test
	public void createCourseCheckCourseNumberTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("123", "java", "三", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res1 = subjectSystemService.createCourse(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("", "java", "三", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res2 = subjectSystemService.createCourse(req2);
		System.out.println("res2:" + res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("00001", "java", "三", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res3 = subjectSystemService.createCourse(req3);
		System.out.println("res3:" + res3.getMessage());
		SubjectSystemRequest req4 = new SubjectSystemRequest("00001", "java", "三", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res4 = subjectSystemService.createCourse(req4);
		System.out.println("res4:" + res4.getMessage());
	}
	
	@Test
	public void createCourseCheckCourseNameTest() {
		SubjectSystemRequest req = new SubjectSystemRequest("00002", " ", "三", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res = subjectSystemService.createCourse(req);
		System.out.println(res.getMessage());
	}
	
	@Test
	public void createCourseCheckWeekDayTest() {
		SubjectSystemRequest req = new SubjectSystemRequest("00003", "java", "七", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res = subjectSystemService.createCourse(req);
		System.out.println("res:" + res.getMessage());
		SubjectSystemRequest req1 = new SubjectSystemRequest("00004", "java", "二", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res1 = subjectSystemService.createCourse(req1);
		System.out.println("res1:" + res1.getMessage());
	}
	
	@Test
	public void createCourseCheckTimeTest() {
		SubjectSystemRequest req = new SubjectSystemRequest("00005", "java", "五", LocalTime.of(8, 0), LocalTime.of(25, 0), 2);
		SubjectSystemResponse res = subjectSystemService.createCourse(req);
		System.out.println("res:" + res.getMessage());
		SubjectSystemRequest req1 = new SubjectSystemRequest("00006", "java", "五", LocalTime.of(10, 0), LocalTime.of(16, 0), 2);
		SubjectSystemResponse res1 = subjectSystemService.createCourse(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("00006", "java", "五", LocalTime.of(10, 0), LocalTime.of(8, 0), 2);
		SubjectSystemResponse res2 = subjectSystemService.createCourse(req2);
		System.out.println("res2:" + res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("00007", "java", "五", LocalTime.of(8, 0), LocalTime.of(10, 0), 2);
		SubjectSystemResponse res3 = subjectSystemService.createCourse(req3);
		System.out.println("res3:" + res3.getMessage());
	}
	
	@Test
	public void createCourseCheckCreditsTest() {
		SubjectSystemRequest req = new SubjectSystemRequest("00008", "java", "五", LocalTime.of(8, 0), LocalTime.of(10, 0), 0);
		SubjectSystemResponse res = subjectSystemService.createCourse(req);
		System.out.println("res:" + res.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("0009", "java", "五", LocalTime.of(8, 0), LocalTime.of(10, 0), 4);
		SubjectSystemResponse res2 = subjectSystemService.createCourse(req2);
		System.out.println("res2:" + res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("0008", "java", "五", LocalTime.of(8, 0), LocalTime.of(10, 0), 3);
		SubjectSystemResponse res3 = subjectSystemService.createCourse(req3);
		System.out.println("res3:" + res3.getMessage());
	}
	//新增學生test
	@Test
	public void addStudentCheckStudentNumberTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("s01", "shuanshuan");
		SubjectSystemResponse res1 = subjectSystemService.addStudent(req1);
		System.out.println("res1:"+res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest(" ", "shuanshuan");
		SubjectSystemResponse res2 = subjectSystemService.addStudent(req2);
		System.out.println("res2:"+res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("s00001", "shuanshuan");
		SubjectSystemResponse res3 = subjectSystemService.addStudent(req3);
		System.out.println("res3:"+res3.getMessage());
		SubjectSystemRequest req4 = new SubjectSystemRequest("s00001", "shuanshuan");
		SubjectSystemResponse res4 = subjectSystemService.addStudent(req4);
		System.out.println("res4:"+res4.getMessage());
	}
	
	@Test
	public void addStudentCheckStudentNameTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("s00001", "");
		SubjectSystemResponse res1 = subjectSystemService.addStudent(req1);
		System.out.println("res1:"+res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("s00002", "arisa");
		SubjectSystemResponse res2 = subjectSystemService.addStudent(req2);
		System.out.println("res2:"+res2.getMessage());
	}
	//刪除課程test
	@Test
	public void deleteCourseTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("00001");
		SubjectSystemResponse res1 = subjectSystemService.deleteCourse(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("00002");
		SubjectSystemResponse res2 = subjectSystemService.deleteCourse(req2);
		System.out.println("res2:" + res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("00001");
		SubjectSystemResponse res3 = subjectSystemService.deleteCourse(req3);
		System.out.println("res3:" + res3.getMessage());
	}
	
	@Test
	public void findCourseByCourseNumber() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("003");
		SubjectSystemResponse res1 = subjectSystemService.findCourseByCourseNumber(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("00001");
		SubjectSystemResponse res2 = subjectSystemService.findCourseByCourseNumber(req2);
		System.out.println("res2:" + res2.getMessage());
		SubjectSystemRequest req3 = new SubjectSystemRequest("00003");
		SubjectSystemResponse res3 = subjectSystemService.findCourseByCourseNumber(req3);
		System.out.println("res3:" + res3.getMessage());
	}
	
	@Test
	public void findCourseByCourseNameTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest();
		req1.setCourseName("Japanese");
		SubjectSystemResponse res1 = subjectSystemService.findCourseByCourseName(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest();
		req2.setCourseName("Java");
		SubjectSystemResponse res2 = subjectSystemService.findCourseByCourseName(req2);
		System.out.println("res2:" + res2.getMessage());
	}
	
	@Test
	public void updateCourseTest() {
		SubjectSystemRequest req1 = new SubjectSystemRequest("00001", "aaa", "三", LocalTime.of(7, 0), LocalTime.of(8, 0), 1);
		SubjectSystemResponse res1 = subjectSystemService.updateCourse(req1);
		System.out.println("res1:" + res1.getMessage());
		SubjectSystemRequest req2 = new SubjectSystemRequest("00002", "aaa", "三", LocalTime.of(7, 0), LocalTime.of(8, 0), 1);
		SubjectSystemResponse res2 = subjectSystemService.updateCourse(req2);
		System.out.println("res2:" + res2.getMessage());
	}
	
	
}
