package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.UserEntity;
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository urepo;
	
	//C - Create new record for tbluser
	public UserEntity insertUser(UserEntity user) {
		return urepo.save(user);
	}
	
	//R - Read all records inside tbluser
	public List<UserEntity> getAllUsers(){
		return urepo.findAll();
	}
	
	//U - Update a student record in tbluser
	@SuppressWarnings("finally")
	public UserEntity updateUser(String username, UserEntity newUserDetails) {
		UserEntity user = new UserEntity();
		try {
			//1.)search the id number of account that will be updated
			user = urepo.findByUsername(username);
			
		    //2.) update the record
			user.setPassword(newUserDetails.getPassword());
			user.setEmail(newUserDetails.getEmail());
			
		}catch(NoSuchElementException ex) {
			throw new NoSuchElementException("User "+ username + " does not exist!");
		}finally {
			return urepo.save(user);
		}
	}
	
	//D - delete a user record.
	public String deleteUser(String username) {
		String msg = "";
		
		if(urepo.findByUsername(username) != null) {
			urepo.deleteByUsername(username);
			msg = "Account " + username + " is successfully deleted!";
		}
		else
			msg = "Account " + username + " does not exist";
		return msg;
		
	}
	
	/////////////////////
	public UserEntity authenticateUser(String username, String password) {
        // Find user by username from the database
		UserEntity user = urepo.findByUsername(username);

        // Check if the user exists and the password matches
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; // Authentication failed
    }
	
	public int login(AuthRequest authRequest) {
		 String username = authRequest.getUsername();
	     String password = authRequest.getPassword();

	     // Implement your custom logic to authenticate the user
	     UserEntity user = urepo.findByUsername(username);

	     if (user != null && user.getPassword().equals(password)) {
	         return user.getUserType();
	     } else {
	         return 0;
	     }
	 }
}