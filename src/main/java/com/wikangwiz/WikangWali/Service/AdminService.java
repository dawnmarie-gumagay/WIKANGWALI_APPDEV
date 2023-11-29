package com.wikangwiz.WikangWali.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.AdminEntity;
import com.wikangwiz.WikangWali.Repository.AdminRepository;
import com.wikangwiz.WikangWali.Repository.UserRepository;

@Service
public class AdminService {

	@Autowired
	AdminRepository adrepo;
	
	@Autowired
	UserRepository urepo;
	
	@Autowired
	UserService userv;
	
	public List<AdminEntity> getAllAdmins(){
		return adrepo.findAll();
	}
	
	public ResponseEntity<AdminEntity> getAdminById(int adminId){
		AdminEntity admin = adrepo.findById(adminId).orElse(null);
		if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}