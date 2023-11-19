package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.wikangwiz.WikangWali.Entity.UserEntity;
import com.wikangwiz.WikangWali.Service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userv;
	
	@GetMapping("/print")
	public String printHello(){
		return "Welcome to Wikang Wali!";
	}
	
	//C - Create new record for tblUser
	@PostMapping("/insertUser")
	public UserEntity insertUser(@RequestBody UserEntity user) {
		return userv.insertUser(user);
	}
	
	//R - Read all records inside tblUser
	@GetMapping("/getAllUsers")
	public List<UserEntity> getAllUsers(){
		return userv.getAllUsers();
	}
	
	//U - Update a User record.
	@PutMapping("/updateUser")
	public UserEntity updateUser(@RequestParam int user_id, @RequestBody UserEntity newUserDetails) {
		return userv.updateUser(user_id, newUserDetails);
	}
	
	//D - Delete a User record.
	@DeleteMapping("/deleteUser/{user_id}")
	public String deleteUser(@PathVariable int user_id){
		return userv.deleteUser(user_id);
	}

}