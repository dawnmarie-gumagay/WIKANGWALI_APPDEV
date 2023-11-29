package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import javax.transaction.Transactional;

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
import com.wikangwiz.WikangWali.Methods.AuthRequest;
import com.wikangwiz.WikangWali.Service.UserService;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	UserService userv;
	
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - User";
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
	public UserEntity updateUser(@RequestParam String username, @RequestBody UserEntity newUserDetails) {
		return userv.updateUser(username, newUserDetails);
	}
	
	//D - Delete a User record.
	@Transactional
	@DeleteMapping("/deleteUser/{username}")
	public String deleteUser(@PathVariable String username){
		return userv.deleteUser(username);
	}
	
	////////////////
	@PostMapping("/login")
	 public int login(@RequestBody AuthRequest authRequest) {
		return userv.login(authRequest);
  }
    
}