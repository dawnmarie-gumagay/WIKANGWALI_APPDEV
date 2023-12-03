package com.wikangwiz.WikangWali.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tblachievement")
public class AchievementEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int achievement_id;
	
	private String achievement_name;
	
	private String achievement_desc;
	
    @Enumerated(EnumType.STRING)
    private AchievementStatus achievement_status;

    @JsonIgnore
	@ManyToMany(mappedBy = "achievements")
    private List<StudentEntity> studentsAchieve;

    // Enums for Achievement Status
    public enum AchievementStatus {
        IN_PROGRESS,
        COMPLETED,
        UNAVAILABLE
        // Add more status values as needed
    }
	
	public AchievementEntity() {
		super();
    	this.studentsAchieve = new ArrayList<>(); // Initialize the students list
	}

	
	
	public AchievementEntity(int achievement_id, String achievement_name, String achievement_desc,
			AchievementStatus achievement_status, List<StudentEntity> studentsAchieve) {
		super();
		this.achievement_id = achievement_id;
		this.achievement_name = achievement_name;
		this.achievement_desc = achievement_desc;
		this.achievement_status = achievement_status;
		this.studentsAchieve = studentsAchieve;
	}



	public int getAchievement_id() {
		return achievement_id;
	}

	public void setAchievement_id(int achievement_id) {
		this.achievement_id = achievement_id;
	}

	public String getAchievement_name() {
		return achievement_name;
	}

	public void setAchievement_name(String achievement_name) {
		this.achievement_name = achievement_name;
	}

	public String getAchievement_desc() {
		return achievement_desc;
	}

	public void setAchievement_desc(String achievement_desc) {
		this.achievement_desc = achievement_desc;
	}

	public AchievementStatus getAchievement_status() {
		return achievement_status;
	}

	public void setAchievement_status(AchievementStatus achievement_status) {
		this.achievement_status = achievement_status;
	}

	public List<StudentEntity> getStudentsAchieve() {
		return studentsAchieve;
	}

	public void setStudentsAchieve(List<StudentEntity> studentsAchieve) {
		this.studentsAchieve = studentsAchieve;
	}


	////////////////////////
	public void addStudent(StudentEntity student) {
		studentsAchieve.add(student);
	}

}
