package com.wikangwiz.WikangWali.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbladmin")
public class AdminEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int admin_id;
	
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

	public AdminEntity() {
		super();
	}

	public AdminEntity(int admin_id, String username, String fname, String lname, String password, String email) {
		super();
		this.admin_id = admin_id;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.password = password;
		this.email = email;
	}

	public int getAdmin_id() {
		return admin_id;
	}
	
	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
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
}
