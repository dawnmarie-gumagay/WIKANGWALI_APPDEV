package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wikangwiz.WikangWali.Entity.AdminEntity;
import com.wikangwiz.WikangWali.Service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService aserv;
	
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - Admin";
	}
	
	@GetMapping("/getAllAdmins")
	public List<AdminEntity> getAllAdmins(){
		return aserv.getAllAdmins();
	}
	
	@GetMapping("/getAdminById/{admin_id}")
	public ResponseEntity<AdminEntity> getAdminById(@PathVariable int admin_id){
		return aserv.getAdminById(admin_id);
	}
}

