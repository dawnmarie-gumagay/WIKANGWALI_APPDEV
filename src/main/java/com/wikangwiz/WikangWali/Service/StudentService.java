package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Entity.UserEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Methods.LoginResponse;
import com.wikangwiz.WikangWali.Methods.UpdatePasswordRequest;
import com.wikangwiz.WikangWali.Repository.StudentRepository;
import com.wikangwiz.WikangWali.Repository.UserRepository;

@Service
public class StudentService {

	@Autowired
	StudentRepository srepo;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	UserService userv;
	
	//C - Create new record for tblstudent
	public StudentEntity insertStudent(StudentEntity student) {
	    // Check if the username already exists in tbluser
		UserEntity existingAccount = urepo.findByUsername(student.getUsername());
	    if (existingAccount != null) {
	        //throw new MyCustomException("Username already exists");
	    }
	    return srepo.save(student);
	}
	
	//R - Read all records inside tblstudent
	public List<StudentEntity> getAllStudents(){
		return srepo.findAll();
	}
	
	//U - Update a student record in tblstudent
	@SuppressWarnings("finally")
	public StudentEntity updateStudent(String username, StudentEntity newStudentDetails) {
		StudentEntity student = new StudentEntity();
		try {
			//1.)search the id number of Student that will be updated
			student = srepo.findByUsername(username);
			
		    //2.) update the record
			student.setFname(newStudentDetails.getFname());
			student.setLname(newStudentDetails.getLname());
			student.setPassword(newStudentDetails.getPassword());
			student.setEmail(newStudentDetails.getEmail());
			student.setAchievements(newStudentDetails.getAchievements());

		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("Student "+ username + " does not exist!");
		}finally {
			return srepo.save(student);
		}
	}
	
	//D - delete a Student
	public String deletePStudent(int student_id) {
	    String msg = "";

	    if (srepo.findById(student_id).isPresent()) {
	        // Get Student by id
	        StudentEntity student = srepo.findById(student_id).get();
	        
	        // Delete Student's account
	        userv.deleteUser(student.getUsername());
	        
	        // Delete Student
	        srepo.deleteById(student_id);
	        msg = "Student " + student_id + " is successfully deleted!";
	    } else {
	        msg = "Student " + student_id + " does not exist";
	    }
	    return msg;
	}
	
	//D - delete a Student by IsDeleted
	@SuppressWarnings("finally")
	public StudentEntity deleteStudent(String username, StudentEntity newStudentDetails) {
		StudentEntity student = new StudentEntity();
		try {
			//1.)search the id number of Student that will be updated
			student = srepo.findByUsername(username);
			
		    //2.) update the record
			student.setIsDeleted(newStudentDetails.getIsDeleted());

		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("Student "+ username + " does not exist!");
		}finally {
			return srepo.save(student);
		}
	}

	///////////
	public LoginResponse login(AuthRequest authRequest) {
	    String username = authRequest.getUsername();
	    String password = authRequest.getPassword();

	    StudentEntity user = srepo.findByUsername(username);

	    if (user != null && user.getPassword().equals(password)) {
	        return new LoginResponse(true, "Login Successful");
	    } else {
	        return new LoginResponse(false, "Login Failed");
	    }
	}
	 
	 public ResponseEntity<StudentEntity> getStudentById(int student_id) {
		 StudentEntity student = srepo.findById(student_id).orElse(null);
	        if (student != null) {
	            return ResponseEntity.ok(student);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	 }
	 
	 public ResponseEntity<StudentEntity> getStudentByUsername(String username) {
		 StudentEntity student = srepo.findByUsername(username);
	        if (student != null) {
	            return ResponseEntity.ok(student);
	        } else {
	            return ResponseEntity.notFound().build();
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
	
	//
	// U - Update a STUDENT PASSWORD
	 @SuppressWarnings("finally")
	 public StudentEntity updateStudentPassword(String username, UpdatePasswordRequest request) {
	     StudentEntity student = srepo.findByUsername(username);

	     try {
	         if (!student.getPassword().equals(request.getOldPassword())) {
	             throw new RuntimeException("Old password does not match.");
	         }

	         if (!request.getNewPassword().equals(request.getConfirmPassword())) {
	             throw new RuntimeException("New password and confirm password do not match.");
	         }

	         student.setPassword(request.getNewPassword());

	     } catch(NoSuchElementException ex) {
			 throw new NoSuchElementException("Student "+ username + " does not exist!");
		 }finally {
			 return srepo.save(student);
		 }
	}


	
	 public StudentEntity findStudentByUsername(String username) {
	        return srepo.findByUsername(username);
	 }
	
}