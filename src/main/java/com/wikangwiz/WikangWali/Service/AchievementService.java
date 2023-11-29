package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;

@Service
public class AchievementService {
	
	@Autowired
	AchievementRepository achieveRepo;
	
	public AchievementEntity findAchievementById(int achievement_id) {
        return achieveRepo.findById(achievement_id);
	}
	
	//C - Create new record for tblAchievement
	public AchievementEntity insertAchievement(AchievementEntity achievement) {
		return achieveRepo.save(achievement);
	}
	
	//R - Read all records inachievement_ide tblAchievement
	public List<AchievementEntity> getAllAchievements(){
		return achieveRepo.findAll();
	}
	
	//U - Update a Achievement record in tblAchievement
	@SuppressWarnings("finally")
	public AchievementEntity updateAchievement(int achievement_id, AchievementEntity newAchievementDetails) {
		AchievementEntity achievement = new AchievementEntity();
		try {
			//1.) Search the id number of the Achievement that will be updated/
			achievement = achieveRepo.findById(achievement_id);
			
			//2.) Once found, update the Achievement record.
			achievement.setAchievement_name(newAchievementDetails.getAchievement_name());
			achievement.setAchievement_desc(newAchievementDetails.getAchievement_desc());
			achievement.setAchievement_status(newAchievementDetails.getAchievement_status());
			
		}
		catch(NoSuchElementException ex){
			new NoSuchElementException("Achievement "+ achievement_id + " does not exist!");
		}
		finally {
			return achieveRepo.save(achievement);
		}
	}
	
	//D - delete a Achievement record.
	public String deleteAchievement(int achievement_id){
		String msg = "";
		
		//If achievement_id is not equal to null, and is found, it can be deleted.
		if(achieveRepo.findById(achievement_id) != null) {
			achieveRepo.deleteById(achievement_id);
			msg = "Achievement " + achievement_id + " is successfully deleted!";
		}
		else {
			msg = "Achievement " + achievement_id + " does not exist.";
		}
		return msg;
	}
	
}
