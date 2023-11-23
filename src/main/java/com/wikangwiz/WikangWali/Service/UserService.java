package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.UserEntity;
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
	public UserEntity updateUser(int user_id, UserEntity newUserDetails) {
		UserEntity user = new UserEntity();
		try {
			//1.) Search the id number of the student that will be updated/
			user = urepo.findById(user_id).get();
			
			//2.) Once found, update the student record.
			user.setUsername(newUserDetails.getUsername());
			user.setPassword(newUserDetails.getPassword());
			user.setEmail(newUserDetails.getEmail());
			
		}
		catch(NoSuchElementException ex){
			new NoSuchElementException("User "+ user_id + " does not exist!");
		}
		finally {
			return urepo.save(user);
		}
	}
	
	//D - delete a user record.
	public String deleteUser(int user_id){
		String msg = "";
		
		//If sid is not equal to null, and is found, it can be deleted.
		if(urepo.findById(user_id) != null) {
			urepo.deleteById(user_id);
			msg = "User " + user_id + "is successfully deleted!";
		}
		else {
			msg = "User " + user_id + " does not exist.";
		}
		return msg;
	}
	
	//FOR LOGGING IN
	public UserEntity authenticateUser(String username, String password) {
        // Find user by username from the database
        UserEntity user = urepo.findByUsername(username);

        // Check if the user exists and the password matches
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; // Authentication failed
    }
}
