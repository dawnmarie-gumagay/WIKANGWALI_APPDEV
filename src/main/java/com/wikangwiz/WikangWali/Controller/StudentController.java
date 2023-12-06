package com.wikangwiz.WikangWali.Controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.wikangwiz.WikangWali.Entity.PointEntity;
import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Methods.UpdatePasswordRequest;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;
import com.wikangwiz.WikangWali.Repository.PointRepository;
import com.wikangwiz.WikangWali.Repository.ProgressTrackerRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;
import com.wikangwiz.WikangWali.Service.StudentService;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {
	
	@Autowired
	StudentService sserv;
	
	@Autowired
	StudentRepository srepo;
	
	@Autowired
	AchievementRepository achieveRepo;
	
	@Autowired
	ProgressTrackerRepository progtRepo;
	
	@Autowired
	PointRepository pRepo;
	
	//////FOR CHECKING//////
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - Student";
	}
	
	//////CRUD//////
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
	
	//R - READ all non-deleted students
	@GetMapping("/getAllNonDeletedStudents")
	public List<StudentEntity>getAllNonDeletedStudents(){
		return sserv.getAllNonDeletedStudents();
	}
	
	// R - READ all deleted students
	@GetMapping("/getAllDeletedStudents")
	public List<StudentEntity>getAllDeletedStudents(){
		return sserv.getAllDeletedStudents();
	}
	
	//U - Update a Student record
	@PutMapping("/updateStudent")
	public StudentEntity updateStudent(@RequestParam int student_id,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudent(student_id, newStudentDetails);
	}
	
	//D - Delete a Student record
	@DeleteMapping("/deletePStudent/{student_id}")
	public String deletePStudent(@PathVariable int student_id) {
		return sserv.deletePStudent(student_id);
	}
	
	// Endpoint to set isDeleted to true for a student by student_id
    @PostMapping("/delete/{student_id}")
    public ResponseEntity<StudentEntity> deleteStudent(@PathVariable int student_id) {
        try {
            StudentEntity deletedStudent = sserv.deleteStudent(student_id);
            return ResponseEntity.ok(deletedStudent);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            // Handle other exceptions if needed
            return ResponseEntity.status(500).body(null);
        }
    }
	
	/////////////
	@PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        LoginResponse response = sserv.login(authRequest);
        return ResponseEntity.ok(response);
    }
	
	 @GetMapping("/getStudentResponseById/{student_id}")
	 public ResponseEntity<StudentEntity> getStudentResponseById(@PathVariable int student_id){
		 return sserv.getStudentResponseById(student_id);
	 }
	 
	 @GetMapping("/getStudentByUsername/{username}")
	 public ResponseEntity<StudentEntity> getStudentByUsername(@PathVariable String username){
		 return sserv.getStudentByUsername(username);
	 }
	 
	//U - Update a Student NAME
	@PutMapping("/updateStudentProfile")
	public StudentEntity updateStudentName(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudentName(username, newStudentDetails);
	}
	
	//U - Update a Student NAME
	@PutMapping("/updateStudentPassword")
	public ResponseEntity<Object> updateStudentPassword(
	    @RequestParam String username,
	    @RequestBody UpdatePasswordRequest request
	) {
	    try {
	        StudentEntity updatedStudent = sserv.updateStudentPassword(username, request);
	        return ResponseEntity.ok(updatedStudent);
	    } catch (NoSuchElementException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}

	////////////ACHIEVEMENTS//////////
	//ADD EXISTING ACHIEVEMENTS TO USER
	@PutMapping("/{username}/achievements/{achievement_id}")
	StudentEntity addAchievementToStudent(
		@PathVariable String username,
		@PathVariable int achievement_id){
			StudentEntity student = srepo.findByUsername(username);
			AchievementEntity achievement = achieveRepo.findById(achievement_id);
			
			student.addAchievement(achievement);
			return srepo.save(student);
	}
	
	//VIEW ACHIEVEMENTS OF USER
	@GetMapping("/{username}/ViewStudentAchievements")
    public ResponseEntity<List<AchievementEntity>> getStudentAchievements(@PathVariable String username) {
        try {
            List<AchievementEntity> achievements = sserv.getStudentAchievements(username);
            return ResponseEntity.ok(achievements);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
	
	
	////////////PROGRESS TRACKERS//////////
	//ADD EXISTING TRACKERS TO USER
	@PutMapping("/{username}/progTrackers/{tracker_id}")
	StudentEntity addProgressTrackerToStudent(
		@PathVariable String username,
		@PathVariable int tracker_id){
			StudentEntity student = srepo.findByUsername(username);
			ProgressTrackerEntity progTrack = progtRepo.findById(tracker_id);
			
			student.addProgressTracker(progTrack);
			return srepo.save(student);
	}
	
	//VIEW PROGRESS TRACKER OF USER
	@GetMapping("/{username}/ViewStudentProgressT")
    public ResponseEntity<List<ProgressTrackerEntity>> getStudentProgressT(@PathVariable String username) {
		try {
			List<ProgressTrackerEntity> progTrackers = sserv.getStudentProgressT(username);
			return ResponseEntity.ok(progTrackers);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}
	
	////////////POINTS//////////
	//ADD EXISTING POINTS TO USER
	@PutMapping("/{username}/points/{point_id}")
	StudentEntity addPointsToStudent(
	@PathVariable String username,
	@PathVariable int point_id){
		StudentEntity student = srepo.findByUsername(username);
		PointEntity point = pRepo.findById(point_id);
		
		student.addPoint(point);
		return srepo.save(student);
	}
	
	//VIEW POINTS OF USER
	@GetMapping("/{username}/ViewStudentPoints")
	public ResponseEntity<List<PointEntity>> getStudentPoints(@PathVariable String username) {
	try {
		List<PointEntity> points = sserv.getStudentPoints(username);
		return ResponseEntity.ok(points);
	} catch (NoSuchElementException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	} catch (Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	}
	}
	
}

