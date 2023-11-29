package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Service.AchievementService;
import com.wikangwiz.WikangWali.Service.StudentService;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	
	@Autowired
	StudentService sserv;
	
	@Autowired
	AchievementService achieveServ;

	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - Student";
	}
	
	//C - Create a Student record
	@PostMapping("/insertStudent")
	public StudentEntity insertStudent(@RequestBody StudentEntity student) {
		return sserv.insertStudent(student);
	}
	
	//R - Read all Student records in tblStudent
	@GetMapping("/getAllStudents")
	public List<StudentEntity>getAllStudents(){
		return sserv.getAllStudents();
	}
	
	//U - Update a Student record
	@PutMapping("/updateStudent")
	public StudentEntity updateStudent(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudent(username, newStudentDetails);
	}
	
	//D - Delete a Student record
	@DeleteMapping("/deletePStudent/{student_id}")
	public String deletePStudent(@PathVariable int student_id) {
		return sserv.deletePStudent(student_id);
	}
	
	//D - Delete a Student record
	@PutMapping("/deleteStudent")
	public StudentEntity deleteStudent(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.deleteStudent(username, newStudentDetails);
	}
	
	/////////////
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        LoginResponse response = sserv.login(authRequest);
        return ResponseEntity.ok(response);
    }
	
	 @GetMapping("/getStudentById/{student_id}")
	 public ResponseEntity<StudentEntity> getStudentById(@PathVariable int student_id){
		 return sserv.getStudentById(student_id);
	 }
	 
	 @GetMapping("/getStudentByUsername/{username}")
	 public ResponseEntity<StudentEntity> getStudentByUsername(@PathVariable String username){
		 return sserv.getStudentByUsername(username);
	 }
	 
	//U - Update a Student NAME
	@PutMapping("/updateStudentName")
	public StudentEntity updateStudentName(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudentName(username, newStudentDetails);
	}
	
	//U - Update a Student PASSWORD
	@PutMapping("/updateStudentPassword")
	public StudentEntity updateStudentPassword(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudentPassword(username, newStudentDetails);
	}
	
	////////////ACHIEVEMENTS
	//ADD ACHIVEMENTS
	@PostMapping("/{username}/addAchievement/{achievement_id}")
	public ResponseEntity<String> addAchievementToStudent(
		@PathVariable String username,
		@PathVariable int achievement_id) {

		// Find the student by username
		StudentEntity student = sserv.findStudentByUsername(username);
		if (student == null) {
				return ResponseEntity.notFound().build();
		}

		// Find the achievement by ID
		AchievementEntity achievement = achieveServ.findAchievementById(achievement_id);
		if (achievement == null) {
			return ResponseEntity.notFound().build();
		}

		// Add the achievement to the student's list
		student.getAchievements().add(achievement);
		sserv.updateStudent(username, student); // Assuming you have a method to save/update a student
		return ResponseEntity.ok("Achievement added to the student successfully.");
	}
	
	//VIEW ACHIEVEMENTS
	// Endpoint to view achievements for a specific student
    @GetMapping("/{username}/achievements")
    public ResponseEntity<List<AchievementEntity>> viewAchievements(@PathVariable String username) {
        // Find the student by username
        StudentEntity student = sserv.findStudentByUsername(username);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        // Get the achievements for the student
        List<AchievementEntity> achievements = student.getAchievements();
        return ResponseEntity.ok(achievements);
    }
}

