package com.wikangwiz.WikangWali.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	private int achievement_status;
	
	@ManyToOne(optional=true)
	private UserEntity user;
	
	//private UserEntity userAchieve;
	
	public AchievementEntity() {
		super();
	}
	
	public AchievementEntity(int achievement_id, String achievement_name, String achievement_desc,
			int achievement_status, UserEntity user) {
		super();
		this.achievement_id = achievement_id;
		this.achievement_name = achievement_name;
		this.achievement_desc = achievement_desc;
		this.achievement_status = achievement_status;
		this.user = user;
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
	public int getAchievement_status() {
		return achievement_status;
	}
	public void setAchievement_status(int achievement_status) {
		this.achievement_status = achievement_status;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	
}
