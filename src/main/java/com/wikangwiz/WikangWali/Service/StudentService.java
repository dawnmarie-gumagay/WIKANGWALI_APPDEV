package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Entity.PointEntity;
import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.EmailAlreadyExistsException;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Methods.UpdatePasswordRequest;
import com.wikangwiz.WikangWali.Repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository srepo;
	
	//////CRUD//////
	//C - CREATE student
	/*public StudentEntity insertStudent(StudentEntity student) {
	    // Check if the username already exists in tbluser
		StudentEntity existingAccount = srepo.findByUsername(student.getUsername());
	    if (existingAccount != null) {
	        //throw new MyCustomException("Username already exists");
	    }
	    return srepo.save(student);
	}*/
	
	//NEED CHECKING
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



	/////////////////////////
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
    
	////////////////////////
	//U - UPDATE student
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
	
	//////END OF CRUD//////
	///////////////////////////////////////////////////////////////////////////////////////////////
	//////MODIFIED CRUD//////
	
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

	
	//U - Update a STUDENT NAME & EMAIL
	@SuppressWarnings("finally")
	public StudentEntity updateStudentName(String username, StudentEntity newStudentDetails) {
		StudentEntity student = new StudentEntity();
		try {
			//1.)search the id number of Student that will be updated
			student = srepo.findByUsername(username);
					
			//2.) update the record
			student.setFname(newStudentDetails.getFname());
			student.setLname(newStudentDetails.getLname());
			student.setEmail(newStudentDetails.getEmail());

		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("Student "+ username + " does not exist!");
		}finally {
			return srepo.save(student);
		}
	}
		
	// U - Update a STUDENT PASSWORD
	public StudentEntity updateStudentPassword(String username, UpdatePasswordRequest request) {
	    try {
	        // 1. Retrieve the student by username
	        StudentEntity student = srepo.findByUsername(username);

	        // 2. Check if the student exists
	        if (student == null) {
	            throw new NoSuchElementException("Student " + username + " does not exist!");
	        }

	        // 3. Check if the old password matches the current password
	        if (!student.getPassword().equals(request.getOldPassword())) {
	            throw new RuntimeException("Old password does not match.");
	        }

	        // 4. Check if the new password is different from the old password
	        if (request.getOldPassword().equals(request.getNewPassword())) {
	            throw new RuntimeException("New password must be different from the old password.");
	        }

	        // 5. Check if the new password matches the confirm password
	        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	            throw new RuntimeException("New password and confirm password do not match.");
	        }

	        // 6. Update the password
	        student.setPassword(request.getNewPassword());

	        // 7. Save the updated student
	        return srepo.save(student);
	    } catch (Exception ex) {
	        // Handle specific exceptions if needed
	        throw new RuntimeException("Error updating password: " + ex.getMessage(), ex);
	    }
	}

	
	//////END OF MODIFIED CRUD//////
	///////////////////////////////////////////////////////////////////////////////////////////////
	//////METHODS//////
	//LOG IN
	public LoginResponse login(AuthRequest authRequest) {
	    String username = authRequest.getUsername();
	    String password = authRequest.getPassword();

	    StudentEntity student = srepo.findByUsername(username);

	    if (student.getIsDeleted()!=true && student != null && student.getPassword().equals(password)) {
	        return new LoginResponse(true, "Login Successful");
	    } else {
	        return new LoginResponse(false, "Login Failed");
	    }
	}
	 
	// GET STUDENT via ID
    public ResponseEntity<StudentEntity> getStudentResponseById(int student_id) {
        StudentEntity student = srepo.findById(student_id);
        if (student != null) {
            return ResponseEntity.ok(student);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public StudentEntity getStudentById(int studentId) {
        StudentEntity student = srepo.findById(studentId);

        if (student != null) {
            return student;
        } else {
            throw new NoSuchElementException("Student with ID " + studentId + " not found");
        }
    }

	 
	// GET STUDENT via USERNAME
	 public ResponseEntity<StudentEntity> getStudentByUsername(String username) {
		 StudentEntity student = srepo.findByUsername(username);
	        if (student != null) {
	            return ResponseEntity.ok(student);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
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
	            student.setAchievements(newStudentDetails.getAchievements());

	            // 4. Save the updated student
	            return srepo.save(student);
	        } catch (Exception ex) {
	            // Handle specific exceptions if needed
	            throw new RuntimeException("Error updating student: " + ex.getMessage(), ex);
	        }
	    }
	 
	////////////////////////////////////ACHIEVEMNTS
	//VIEW ACHIEVEMENTS
	public List<AchievementEntity> getStudentAchievements(String username) {
	StudentEntity student = srepo.findByUsername(username);
	
		if (student == null) {
	        throw new NoSuchElementException("Student " + username + " not found");
		}
		return student.getAchievements();
	}
	
	
	////////////////////////////////////PROGRESS TRACKER
	//PROGRESS TRACKERSS
	public List<ProgressTrackerEntity> getStudentProgressT(String username) {
	StudentEntity student = srepo.findByUsername(username);
	
		if (student == null) {
	        throw new NoSuchElementException("Student " + username + " not found");
		}
		return student.getProgTrackers();
	}
	
	////////////////////////////////////POINTS
	//POINTSS
	public List<PointEntity> getStudentPoints(String username) {
		StudentEntity student = srepo.findByUsername(username);
		
		if (student == null) {
			throw new NoSuchElementException("Student " + username + " not found");
		}
		return student.getPoints();
	}
	
}