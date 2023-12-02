package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Repository.AchievementRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;

@Service
public class AchievementService {
	
	@Autowired
	AchievementRepository achieveRepo;
	
	@Autowired
	StudentRepository srepo;
	
	@Autowired
    private StudentService sserv;
	
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
	
	///////////////////////////////////
	
	//ADD ACHIEVEMENT TO USER
	public AchievementEntity linkAchievementToStudent(int student_id, int achievement_id) {
	    // Check if the student exists
	    StudentEntity student = sserv.getStudentById(student_id);

	    // Check if the achievement exists
	    AchievementEntity achievement = achieveRepo.findById(achievement_id);

	    if (student != null) {
	        if (achievement != null) {
	            // Check if the user is not deleted
	            if (!student.getIsDeleted()) {
	                // Check if the achievement is not already associated with the user
	                if (!student.getAchievements().contains(achievement)) {
	                    student.getAchievements().add(achievement);
	                    // Save the updated student to reflect the new association
	                    sserv.updateStudentObject(student);
	                    return achievement;
	                } else {
	                    throw new IllegalArgumentException("Achievement already linked to the student");
	                }
	            } else {
	                throw new IllegalArgumentException("Cannot link achievement to a deleted user");
	            }
	        } else {
	            throw new NoSuchElementException("Achievement with ID " + achievement_id + " not found");
	        }
	    } else {
	        throw new NoSuchElementException("Student with ID " + student_id + " does not exist!");
	    }
	}
	
}
