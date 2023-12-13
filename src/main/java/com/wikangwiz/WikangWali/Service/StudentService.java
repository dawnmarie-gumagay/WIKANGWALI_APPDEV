package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.EmailAlreadyExistsException;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Methods.ResetCodeResponse;
import com.wikangwiz.WikangWali.Methods.UpdatePasswordRequest;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;
import com.wikangwiz.WikangWali.Repository.ProgressTrackerRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository srepo;
	
	@Autowired
    private ProgressTrackerRepository progtRepo;
	
	@Autowired
    private AchievementRepository achieveRepo;
	
	
	//USED TO REGISTER AND ADD USER (USED IN REACT) (isDeleted is considered)
	public StudentEntity insertStudent(StudentEntity student) {
	    // Check if the username already exists in tbluser
	    StudentEntity existingUsername = srepo.findByUsername(student.getUsername());

	    // Check if the existing student with the same username is deleted or not present
	    if (existingUsername != null) {
	        if (existingUsername.getIsDeleted()) {
	            // If the existing student is deleted, you can proceed with creating the new student
	            return srepo.save(student);
	        } else {
	            // If the existing student is not deleted, throw an exception
	            throw new IllegalArgumentException("Username already exists and is not deleted");
	            // Replace "IllegalArgumentException" with an appropriate exception class.
	        }
	    }

	    // Check if the email already exists in tbluser
	    StudentEntity existingEmail = srepo.findByEmail(student.getEmail());
	    if (existingEmail != null) {
	        throw new EmailAlreadyExistsException("Email already exists");
	        // Replace "EmailAlreadyExistsException" with an appropriate exception class.
	    }

	    // If the username does not exist, create the new student
	    return srepo.save(student);
	}

	//R - READ all students
	public List<StudentEntity> getAllStudents(){
		return srepo.findAll();
	}
	
	// R - READ all non-deleted students
    public List<StudentEntity> getAllNonDeletedStudents() {
        return srepo.findByIsDeletedFalse();
    }
	
	// R - READ all deleted students
    public List<StudentEntity> getAllDeletedStudents() {
        return srepo.findByIsDeletedTrue();
    }
    
	//U - UPDATE student (NOT USED IN REACT)
	@SuppressWarnings("finally")
	public StudentEntity updateStudent(int student_id, StudentEntity newStudentDetails) {
		StudentEntity student = new StudentEntity();
		try {
			//1.)search the id number of Student that will be updated
			student = srepo.findById(student_id);
			
		    //2.) update the record
			student.setFname(newStudentDetails.getFname());
			student.setLname(newStudentDetails.getLname());
			student.setPassword(newStudentDetails.getPassword());
			student.setEmail(newStudentDetails.getEmail());
			student.setAchievements(newStudentDetails.getAchievements());
			student.setProgTrackers(newStudentDetails.getProgTrackers());

		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("Student "+ student_id + " does not exist!");
		}finally {
			return srepo.save(student);
		}
	}
	
	//D - DELETE student (PERMANENTLY) (NOT USED IN REACT)
	public String deletePStudent(int student_id){
		String msg = "";
			
		//If student_id is not equal to null, and is found, it can be deleted.
		if(srepo.findById(student_id) != null) {
			srepo.deleteById(student_id);
			msg = "Student " + student_id + " is successfully deleted!";
		}else{
			msg = "Student " + student_id + " does not exist.";
		}
		return msg;
	}
	
	////D - DELETE student by UPDATING isDeleted (TO BE USED)
	public StudentEntity deleteStudent(int student_id) {
	    try {
	        // 1. Search the student by student_id
	        StudentEntity student = srepo.findById(student_id);

	        // 2. Check if the student exists
	        if (student == null) {
	            throw new NoSuchElementException("Student with ID " + student_id + " does not exist!");
	        }

	        // 3. Update the isDeleted property
	        student.setIsDeleted(true); // Set isDeleted to true

	        // 4. Save the updated student
	        return srepo.save(student);
	    } catch (Exception ex) {
	        // Handle specific exceptions if needed
	        throw new RuntimeException("Error updating student: " + ex.getMessage(), ex);
	    }
	}

	//U - Update a STUDENT NAME & EMAIL (isDeleted is considered)
	public StudentEntity updateStudentProfile(String username, StudentEntity newStudentDetails) {
	    try {
	        // 1. Retrieve the list of students by username and isDeleted status
	        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	        // 2. Handle the logic to choose the appropriate student
	        StudentEntity student = findStudentToReturn(students);

	        if (student != null) {
	            // 3. Check if the new email is already taken by another student
	            if (!newStudentDetails.getEmail().equals(student.getEmail()) &&
	                    srepo.existsByEmailAndIsDeleted(newStudentDetails.getEmail(), false)) {
	                throw new RuntimeException("Email is already taken by another student.");
	            }

	            // 4. Update the record
	            student.setFname(newStudentDetails.getFname());
	            student.setLname(newStudentDetails.getLname());
	            student.setEmail(newStudentDetails.getEmail());
	        } else {
	            throw new NoSuchElementException("Student " + username + " does not exist!");
	        }

	        // 5. Save the updated student
	        return srepo.save(student);
	    } catch (Exception ex) {
	        // Handle other exceptions if needed
	        ex.printStackTrace();
	        throw new RuntimeException("Error updating student profile: " + ex.getMessage());
	    }
	}
		
	// U - Update a STUDENT PASSWORD (isDeleted is considered)
	public StudentEntity updateStudentPassword(String username, UpdatePasswordRequest request) {
	    try {
	        // 1. Retrieve the students by username and isDeleted condition
	        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	        // 2. Handle the logic to choose the appropriate student
	        StudentEntity student = findStudentToUpdatePassword(students);

	        // 3. Check if the student exists
	        if (student == null) {
	            throw new NoSuchElementException("Student " + username + " does not exist!");
	        }

	        // 4. Check if the old password matches the current password
	        if (!student.getPassword().equals(request.getOldPassword())) {
	            throw new RuntimeException("Old password does not match.");
	        }

	        // 5. Check if the new password is different from the old password
	        if (request.getOldPassword().equals(request.getNewPassword())) {
	            throw new RuntimeException("New password must be different from the old password.");
	        }

	        // 6. Check if the new password matches the confirm password
	        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	            throw new RuntimeException("New password and confirm password do not match.");
	        }

	        // 7. Update the password
	        student.setPassword(request.getNewPassword());

	        // 8. Save the updated student
	        return srepo.save(student);
	    } catch (Exception ex) {
	        // Handle specific exceptions if needed
	        throw new RuntimeException("Error updating password: " + ex.getMessage(), ex);
	    }
	}

	private StudentEntity findStudentToUpdatePassword(List<StudentEntity> students) {
	    // Implement your logic here to choose the appropriate student
	    // In this example, we'll return the first non-deleted student if available
	    for (StudentEntity s : students) {
	        if (!s.getIsDeleted()) {
	            return s;
	        }
	    }
	    return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	//////METHODS//////
	//LOG IN (isDeleted is considered)
	public LoginResponse login(AuthRequest authRequest) {
	    String username = authRequest.getUsername();
	    String password = authRequest.getPassword();

	    List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	    if (!students.isEmpty()) {
	        // Assuming there is only one active student with the given username
	        StudentEntity student = students.get(0);

	        if (student.getPassword().equals(password)) {
	            return new LoginResponse(true, "Login Successful");
	        } else {
	            return new LoginResponse(false, "Login Failed");
	        }
	    } else {
	        // No active student found with the given username
	        return new LoginResponse(false, "Login Failed");
	    }
	}

	// RESPONSE STUDENT ID
    public ResponseEntity<StudentEntity> getStudentResponseById(int student_id) {
        StudentEntity student = srepo.findById(student_id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //STUDENT ID
    public StudentEntity getStudentById(int studentId) {
        StudentEntity student = srepo.findById(studentId);

        if (student != null) {
            return student;
        } else {
            throw new NoSuchElementException("Student with ID " + studentId + " not found");
        }
    }

	 
    // GET STUDENT via USERNAME and is not deleted (FOR FETCHING DATA) (isDeleted is considered)
    public ResponseEntity<StudentEntity> getStudentByUsername(String username) {
        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

        // Handle the logic to choose the appropriate student
        StudentEntity student = findStudentToReturn(students);

        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    //FIND STUDENT (IF SAME MULTIPLE USERNAME)
    private StudentEntity findStudentToReturn(List<StudentEntity> students) {
        // Implement your logic here to choose the appropriate student
        // In this example, we'll return the first non-deleted student if available
        for (StudentEntity s : students) {
            if (!s.getIsDeleted()) {
                return s;
            }
        }
        return null;
    }
	 
    //MIGHT BE USEFUL
	 public StudentEntity findStudentByUsername(String username) {
	       return srepo.findByUsername(username);
	 }
	//////////////////////////////////////////////////
	 
	 //OUTDATED
	 public StudentEntity updateStudentObject(StudentEntity newStudentDetails) {
		 try {
			 // 1. Search the student by username
			 StudentEntity student = srepo.findByUsername(newStudentDetails.getUsername());

			 // 2. Check if the student exists
			 if (student == null) {
				 throw new NoSuchElementException("Student with username " + newStudentDetails.getUsername() + " does not exist!");
			 }

			 // 3. Update the student details
			 student.setFname(newStudentDetails.getFname());
			 student.setLname(newStudentDetails.getLname());
			 student.setPassword(newStudentDetails.getPassword());
			 student.setEmail(newStudentDetails.getEmail());

			 // 4. Save the updated student
			 return srepo.save(student);
		 } catch (Exception ex) {
			 // Handle specific exceptions if needed
			 throw new RuntimeException("Error updating student: " + ex.getMessage(), ex);
		 }
	}
	 
	 /////////////FORGET PASSWORD////////////
	 //GENERATE CODE 2
	 public ResponseEntity<ResetCodeResponse> generateResetCode(String username) {
	        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	        if (students.isEmpty()) {
	            throw new IllegalArgumentException("No active student found with username: " + username);
	        }

	        StudentEntity student = students.get(0);

	        try {
	            // Generate a random 4-digit code
	            int resetCode = new Random().nextInt(9000) + 1000;

	            // TODO: Send the reset code to the user (you can use email, SMS, etc.)
	            System.out.print("The reset code is " + resetCode);

	            // Store the reset code in the database
	            student.setResetCode(resetCode);
	            srepo.save(student);
	            System.out.print("The reset code for " + username + " is " + resetCode);

	            return ResponseEntity.ok(new ResetCodeResponse("Reset code generated successfully. Check database for the code."));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(new ResetCodeResponse(e.getMessage()));
	        }
	    }
	 
	//RESET CODE UPDATE 2
	 public ResponseEntity<ResetCodeResponse> resetPassword(String username, int resetCode, String newPassword, String confirmPassword) {
	        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	        if (students.isEmpty()) {
	            throw new IllegalArgumentException("No active student found with username: " + username);
	        }

	        StudentEntity student = students.get(0);

	        try {
	            if (student.getResetCode() != resetCode) {
	                throw new IllegalArgumentException("Invalid reset code. Please check your code again.");
	            }

	            // Check if newPassword is different from the old password
	            if (newPassword.equals(student.getPassword())) {
	                throw new IllegalArgumentException("New password must be different from the old password.");
	            }

	            // Check if newPassword matches confirmPassword
	            if (!newPassword.equals(confirmPassword)) {
	                throw new IllegalArgumentException("New password and confirm password do not match.");
	            }

	            // Update the password and reset code
	            student.setPassword(newPassword);
	            student.setResetCode(0); // Clear the reset code after successful reset

	            srepo.save(student);

	            return ResponseEntity.ok(new ResetCodeResponse("Password reset successfully!"));
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body(new ResetCodeResponse(e.getMessage()));
	        }
	    }
	 
	 
	////////////////////////////////////ACHIEVEMNTS
	 
	//ADD ACHIEVEMENTS TRACKER TO STUDENT(isDeleted is considered)
	public StudentEntity addAchievementToStudent(String username, int achievementId) {
		List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

		if (students.isEmpty()) {
			throw new EntityNotFoundException("No active student found with username: " + username);
		}

		StudentEntity student = students.get(0);

		AchievementEntity achievement = achieveRepo.findById(achievementId);

		if (achievement == null) {
			throw new EntityNotFoundException("Achievement not found with id: " + achievementId);
		}

		student.addAchievement(achievement);

		return srepo.save(student);
	}
	 
	//VIEW ACHIEVEMENTS (isDeleted is considered)
	 public List<AchievementEntity> getStudentAchievements(String username) {
		 // 1. Retrieve the students by username and isDeleted condition
		 List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

		 // 2. Handle the logic to choose the appropriate student
		 StudentEntity student = findStudentForAchievements(students);

		 // 3. Check if the student exists
		 if (student == null) {
			 throw new NoSuchElementException("Student " + username + " not found");
		 }

		 // 4. Return the achievements of the chosen student
		 return student.getAchievements();
	}
	 
	private StudentEntity findStudentForAchievements(List<StudentEntity> students) {
		// Implement your logic here to choose the appropriate student
		// In this example, we'll return the first non-deleted student if available
		for (StudentEntity s : students) {
			if (!s.getIsDeleted()) {
				return s;
			}
		}
		return null;
	}
	
	////////////////////////////////////PROGRESS TRACKER
	//PROGRESS TRACKERSS
	//ADD PROGRESS TRACKER TO STUDENT(isDeleted is considered)
	public StudentEntity addProgressTrackerToStudent(String username, int trackerId) {
		// Retrieve the list of students by username and isDeleted status
		List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

		// Check if the list is empty
		if (students.isEmpty()) {
			throw new EntityNotFoundException("No active student found with username: " + username);
		}

		// Assuming you want to add the progress tracker to the first student in the list
		StudentEntity student = students.get(0);

		// Retrieve the progress tracker by ID
		ProgressTrackerEntity progTrack = progtRepo.findById(trackerId);

	    //Check if the progress tracker exists
	    if (progTrack == null) {
	        throw new EntityNotFoundException("Progress Tracker not found with id: " + trackerId);
	    }

	    //6. Add progress tracker to the chosen student
	    student.addProgressTracker(progTrack);

	    // 7. Save the updated student
	    return srepo.save(student);
	}
		
	//VIEW PROGRESS TRACKER (isDeleted is considered)
	public List<ProgressTrackerEntity> getStudentProgressT(String username) {
	    // 1. Retrieve the students by username and isDeleted condition
		List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

		// 2. Handle the logic to choose the appropriate student
		StudentEntity student = findStudentForProgressTracker(students);

		// 3. Check if the student exists
		if (student == null) {
			throw new NoSuchElementException("Student " + username + " not found");
		}

		// 4. Return the progress trackers of the chosen student
		return student.getProgTrackers();
		}

	private StudentEntity findStudentForProgressTracker(List<StudentEntity> students) {
		// Implement your logic here to choose the appropriate student
		// In this example, we'll return the first non-deleted student if available
		for (StudentEntity s : students) {
			if (!s.getIsDeleted()) {
				return s;
			}
		}
		return null;
		}
	
	// Increment the progress of a specific progress tracker for a student
	public StudentEntity incrementProgressTracker(String username, int trackerId, int incrementAmount) {
	    // Retrieve the list of students by username and isDeleted status
	    List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	    // Check if the list is empty
	    if (students.isEmpty()) {
	        throw new EntityNotFoundException("No active student found with username: " + username);
	    }

	    // Assuming you want to increment the progress of the first student in the list
	    StudentEntity student = students.get(0);

	    // Retrieve the progress tracker by ID
	    ProgressTrackerEntity progTrack = progtRepo.findById(trackerId);

	    // Check if the progress tracker exists
	    if (progTrack == null) {
	        throw new EntityNotFoundException("Progress Tracker not found with id: " + trackerId);
	    }

	    // Check if the progress tracker is already completed
	    if (progTrack.isCompleted()) {
	        throw new RuntimeException("Progress Tracker is already completed and cannot be incremented further.");
	    }

	    // Increment the progress of the chosen progress tracker
	    progTrack.setProgPerc(progTrack.getProgPerc() + incrementAmount);

	    // Check if the progress reached 100 and mark it as completed
	    if (progTrack.getProgPerc() >= 100) {
	        progTrack.setCompleted(true);
	    }

	    // Save the updated progress tracker
	    ProgressTrackerEntity updatedProgTrack = progtRepo.save(progTrack);

	    // Remove the progress tracker from its current position in the list
	    student.removeProgressTracker(progTrack);

	    // Add the updated progress tracker to the end of the list
	    student.addProgressTracker(updatedProgTrack);

	    // Save the updated student
	    return srepo.save(student);
	}
	
	/////////////////////// POINTS
	//STAR POINTS (isDeleted is considered)
	public void addPtStarToStudent(String username, int numberOfStars) {
	    // 1. Retrieve the students by username and isDeleted condition
	    List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	    // 2. Handle the logic to choose the appropriate student
	    StudentEntity student = findStudentForPtStar(students);

	    // 3. Check if the student exists
	    if (student == null) {
	        throw new EntityNotFoundException("Student not found with id: " + username);
	    }

	    // 4. Add stars to the chosen student
	    student.setPtStar(student.getPtStar() + numberOfStars);
	    srepo.save(student);
	}

	private StudentEntity findStudentForPtStar(List<StudentEntity> students) {
	    // Implement your logic here to choose the appropriate student
	    // In this example, we'll return the first non-deleted student if available
	    for (StudentEntity s : students) {
	        if (!s.getIsDeleted()) {
	            return s;
	        }
	    }
	    return null;
	}
	
	//DIAMOND POINTS (isDeleted is considered)
	public void addPtDiasToStudent(String username, int numberOfDias) {
	    // 1. Retrieve the students by username and isDeleted condition
	    List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	    // 2. Handle the logic to choose the appropriate student
	    StudentEntity student = findStudentForPtDias(students);

	    // 3. Check if the student exists
	    if (student == null) {
	        throw new EntityNotFoundException("Student not found with id: " + username);
	    }

	    // 4. Add dias to the chosen student
	    student.setPtDia(student.getPtDia() + numberOfDias);
	    srepo.save(student);
	}

	private StudentEntity findStudentForPtDias(List<StudentEntity> students) {
	    // Implement your logic here to choose the appropriate student
	    // In this example, we'll return the first non-deleted student if available
	    for (StudentEntity s : students) {
	        if (!s.getIsDeleted()) {
	            return s;
	        }
	    }
	    return null;
	}
}