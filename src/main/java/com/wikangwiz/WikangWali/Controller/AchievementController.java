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

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;
import com.wikangwiz.WikangWali.Service.AchievementService;

@RestController
@RequestMapping("/achievement")
@CrossOrigin(origins = "http://localhost:3000")
public class AchievementController {
	
	@Autowired
	AchievementService achieveServ;
	
	@Autowired
	AchievementRepository achieveRepo;
	
	@Autowired
	StudentRepository srepo;
	
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - Achievement";
	}
	
	//C - Create new record for tblachievement
	@PostMapping("/insertAchievement")
	public AchievementEntity insertAchievement(@RequestBody AchievementEntity achievement) {
		return achieveServ.insertAchievement(achievement);
	}
	
	//R - Read all records inachievement_ide tblachievement
	@GetMapping("/getAllAchievements")
	public List<AchievementEntity> getAllAchievements(){
		return achieveServ.getAllAchievements();
	}
	
	//U - Update a achievement record.
	@PutMapping("/updateAchievement")
	public AchievementEntity updateAchievement(@RequestParam int achievement_id, @RequestBody AchievementEntity newAchievementDetails) {
		return achieveServ.updateAchievement(achievement_id, newAchievementDetails);
	}
	
	//D - Delete a achievement record.
	@DeleteMapping("/deleteAchievement/{achievement_id}")
	public String deleteAchievement(@PathVariable int achievement_id){
		return achieveServ.deleteAchievement(achievement_id);
	}
	
	
}