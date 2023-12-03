package com.wikangwiz.WikangWali.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="tblstudent")
public class StudentEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int student_id;
	
	@Column(nullable = false)
	private String username;
	
	@Column(name = "firstname")
	private String fname;
	
	@Column(name = "lastname")
	private String lname;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String email;
	
	private boolean isDeleted;
	
	private boolean isAdmin;
	
	@ManyToMany
    @JoinTable(
    		name="students_achievements",
    		joinColumns = @JoinColumn(name = "student_id"),
    		inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private List<AchievementEntity> achievements;
    
	@ManyToMany
    @JoinTable(
    		name="students_progTrackers",
    		joinColumns = @JoinColumn(name = "student_id"),
    		inverseJoinColumns = @JoinColumn(name = "tracker_id")
    )
    private List<ProgressTrackerEntity> progTrackers;
	
    public StudentEntity() {
        this.achievements = new ArrayList<>(); // Initialize the achievements list
        this.progTrackers = new ArrayList<>(); // Initialize the progTrackers list
        this.isAdmin = false;
    }

	public StudentEntity(int student_id, String username, String fname, String lname, String password, String email,
			boolean isDeleted, boolean isAdmin, List<AchievementEntity> achievements, List<ProgressTrackerEntity> progTrackers) {
		super();
		this.student_id = student_id;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.password = password;
		this.email = email;
		this.isDeleted = isDeleted;
		this.isAdmin = isAdmin;
		this.achievements = achievements != null ? achievements : new ArrayList<>(); // Initialize the achievements list
		this.progTrackers = progTrackers != null ? progTrackers : new ArrayList<>(); // Initialize the progTrackers list
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
		public List<AchievementEntity> getAchievements() {
		return achievements;
	}


	public void setAchievements(List<AchievementEntity> achievements) {
		this.achievements = achievements;
	}

	public List<ProgressTrackerEntity> getProgTrackers() {
		return progTrackers;
	}

	public void setProgTrackers(List<ProgressTrackerEntity> progTrackers) {
		this.progTrackers = progTrackers;
	}

	public void addAchievement(AchievementEntity achievement) {
		achievements.add(achievement);
	}

	public void addProgressTracker(ProgressTrackerEntity progTrack) {
		progTrackers.add(progTrack);
		
	}
	
}
