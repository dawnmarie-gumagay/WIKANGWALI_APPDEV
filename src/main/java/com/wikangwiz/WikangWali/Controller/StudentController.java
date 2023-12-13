package com.wikangwiz.WikangWali.Controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

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
import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.ForgotPasswordRequest;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Methods.ResetCodeResponse;
import com.wikangwiz.WikangWali.Methods.ResetPasswordRequest;
import com.wikangwiz.WikangWali.Methods.UpdatePasswordRequest;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;
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
	////////////////////
    
	 @GetMapping("/getStudentResponseById/{student_id}")
	 public ResponseEntity<StudentEntity> getStudentResponseById(@PathVariable int student_id){
		 return sserv.getStudentResponseById(student_id);
	 }
	 
	 @GetMapping("/getStudentByUsername/{username}")
	 public ResponseEntity<StudentEntity> getStudentByUsername(@PathVariable String username){
		 return sserv.getStudentByUsername(username);
	 }
	 
	//U - Update a Student PROFILE (USED IN REACT)
	@PutMapping("/updateStudentProfile")
	public StudentEntity updateStudentProfile(@RequestParam String username,@RequestBody StudentEntity newStudentDetails){
		return sserv.updateStudentProfile(username, newStudentDetails);
	}
	
	//U - Update a Student PASSWORD (USED IN REACT)
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
	
	///////////FORGET PASSWORD////////////
	//GENERATE CODE
	@PostMapping("/generateResetCode")
    public ResponseEntity<ResetCodeResponse> generateResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            return sserv.generateResetCode(forgotPasswordRequest.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResetCodeResponse(e.getMessage()));
        }
    }
	
	//RESET PASS UPDATE
	@PostMapping("/resetPassword")
    public ResponseEntity<ResetCodeResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            return sserv.resetPassword(
            		resetPasswordRequest.getUsername(),
                    resetPasswordRequest.getResetCode(),
                    resetPasswordRequest.getNewPassword(),
                    resetPasswordRequest.getConfirmPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResetCodeResponse(e.getMessage()));
        }
    }
	
	////////////ACHIEVEMENTS//////////
	//ADD EXISTING ACHIEVEMENTS TO USER
	@PutMapping("/{username}/achievements/{achievementId}")
    public ResponseEntity<?> addAchievementToStudent(
            @PathVariable String username,
            @PathVariable int achievementId) {
        try {
            StudentEntity updatedStudent = sserv.addAchievementToStudent(username, achievementId);
            return ResponseEntity.ok(updatedStudent);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
	
	//ADD PROGRESS PERCENT
	// Increment the progress of a specific progress tracker for a student
	@PutMapping("/{username}/progressTrackers/{trackerId}/increment/{incrementAmount}")
	public ResponseEntity<?> incrementProgressTracker(
	        @PathVariable String username,
	        @PathVariable int trackerId,
	        @PathVariable int incrementAmount) {
	    try {
	        StudentEntity updatedStudent = sserv.incrementProgressTracker(username, trackerId, incrementAmount);
	        return ResponseEntity.ok(updatedStudent);
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.notFound().build();
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}
	
	
	////////////PROGRESS TRACKERS//////////
	//ADD EXISTING TRACKERS TO USER
	@PutMapping("/{username}/progTrackers/{trackerId}")
    public ResponseEntity<?> addProgressTrackerToStudent(
            @PathVariable String username,
            @PathVariable int trackerId) {
        try {
            StudentEntity updatedStudent = sserv.addProgressTrackerToStudent(username, trackerId);
            return ResponseEntity.ok(updatedStudent);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
	
	
	/////////////////////
	
	//STAR PTS
	@PutMapping("/{username}/addPtStar/{numberOfStars}")
    public ResponseEntity<String> addPtStarToStudent(
            @PathVariable String username,
            @PathVariable int numberOfStars) {
        try {
            sserv.addPtStarToStudent(username, numberOfStars);
            return ResponseEntity.ok("Successfully added "+ numberOfStars + " Star/s to Student " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add "+ numberOfStars + " Star/s to Student " + username);
        }
    }
	
	//DIAS PTS
		@PutMapping("/{username}/addPtDia/{numberOfDias}")
	    public ResponseEntity<String> addPtDiasToStudent(
	            @PathVariable String username,
	            @PathVariable int numberOfDias) {
	        try {
	            sserv.addPtDiasToStudent(username, numberOfDias);
	            return ResponseEntity.ok("Successfully added "+ numberOfDias + " Diamond/s to Student " + username);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("Failed to add "+ numberOfDias + " Diamond/s to Student " + username);
	        }
	    }

}

