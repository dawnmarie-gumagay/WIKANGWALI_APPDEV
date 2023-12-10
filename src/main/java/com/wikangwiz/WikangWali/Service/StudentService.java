package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
	
	//D - DELETE student (PERMANENTLY)
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
	
	////D - DELETE student by UPDATING isDeleted
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
	@SuppressWarnings("finally")
	public StudentEntity updateStudentProfile(String username, StudentEntity newStudentDetails) {
	    StudentEntity student = new StudentEntity();
	    try {
	        //1.) search the id number of Student that will be updated
	        List<StudentEntity> students = srepo.findByUsernameAndIsDeleted(username, false);

	        //2.) handle the logic to choose the appropriate student
	        student = findStudentToReturn(students);

	        if (student != null) {
	            //3.) update the record
	            student.setFname(newStudentDetails.getFname());
	            student.setLname(newStudentDetails.getLname());
	            student.setEmail(newStudentDetails.getEmail());
	        } else {
	            throw new NoSuchElementException("Student " + username + " does not exist!");
	        }
	    } catch (Exception ex) {
	        // Handle other exceptions if needed
	        ex.printStackTrace();
	    } finally {
	        return srepo.save(student);
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

	
	//////END OF MODIFIED CRUD//////
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
	 
	 public StudentEntity findStudentByUsername(String username) {
	       return srepo.findByUsername(username);
	 }
	//////////////////////////////////////////////////
	 
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