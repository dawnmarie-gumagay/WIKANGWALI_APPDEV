package com.wikangwiz.WikangWali.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tblstudent")
public class StudentEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int student_id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(name = "firstname")
	private String fname;
	
	@Column(name = "lastname")
	private String lname;
	
	@Column(nullable = false)
	private String password;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private int isDeleted;
	
	private boolean isAdmin;
	
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AchievementEntity> achievements;
    

    public StudentEntity() {
        this.achievements = new ArrayList<>(); // Initialize the achievements list
    }

	public StudentEntity(int student_id, String username, String fname, String lname, String password, String email,
			int isDeleted, boolean isAdmin, List<AchievementEntity> achievements) {
		super();
		this.student_id = student_id;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.password = password;
		this.email = email;
		this.isDeleted = isDeleted;
		this.isAdmin = false;
		this.achievements = achievements != null ? achievements : new ArrayList<>(); // Initialize the achievements list
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


	public int getIsDeleted() {
		return isDeleted;
	}


	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}


	public List<AchievementEntity> getAchievements() {
		return achievements;
	}


	public void setAchievements(List<AchievementEntity> achievements) {
		this.achievements = achievements;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
	
}
