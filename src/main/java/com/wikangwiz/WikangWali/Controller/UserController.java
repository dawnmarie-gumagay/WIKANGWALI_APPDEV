package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
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

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;

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
	
  /*@PostMapping("/login")
  public String login(@RequestBody UserEntity user) {
    UserEntity authenticatedUser = userv.authenticateUser(user.getUsername(), user.getPassword());

      if (authenticatedUser != null) {
        return "Login successful"; // You may return a token or other information here
	    } else {
        return "Login failed";
      }
    }*/
	  
		/*@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserEntity user) {
        UserEntity authenticatedUser = userv.authenticateUser(user.getUsername(), user.getPassword());

        if (authenticatedUser != null) {
            String token = generateToken(authenticatedUser.getUsername());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    private String generateToken(String username) {
        // Add any additional information you want to include in the token
        // For simplicity, we're only including the username here
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, "yourSecretKey")
                .compact();
    }*/
}