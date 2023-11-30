package com.wikangwiz.WikangWali.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbluser")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int user_id;
	
	@Column(unique = true, nullable = false)
	private String username;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	private int userType;
	
	private int isDeleted;
	
	public UserEntity() {
		super();
	}

	public UserEntity(int user_id, String username, String email, String password, int userType, int isDeleted) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.userType = 0;
		this.isDeleted = isDeleted;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}