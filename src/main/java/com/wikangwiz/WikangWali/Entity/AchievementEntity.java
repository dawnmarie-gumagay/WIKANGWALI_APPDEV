package com.wikangwiz.WikangWali.Entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    // Enums for Achievement Status
    public enum AchievementStatus {
        IN_PROGRESS,
        COMPLETED,
        UNAVAILABLE
        // Add more status values as needed
    }
	
	public AchievementEntity() {
		super();
	}

	public AchievementEntity(int achievement_id, String achievement_name, String achievement_desc,
			AchievementStatus achievement_status, StudentEntity student) {
		super();
		this.achievement_id = achievement_id;
		this.achievement_name = achievement_name;
		this.achievement_desc = achievement_desc;
		this.achievement_status = achievement_status;
		this.student = student;
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

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}
	
	
}
