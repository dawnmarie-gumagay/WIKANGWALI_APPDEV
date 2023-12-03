package com.wikangwiz.WikangWali.Entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tblprogresstracker")
public class ProgressTrackerEntity {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int tracker_id;
	 
	 @Column(name = "activityname")
	 private String actName;
	 
	 private boolean isCompleted;
	 
	@Column(name = "progresspercent")
	private int progPerc;
	 
	@JsonIgnore
	@ManyToMany(mappedBy = "progTrackers")
    private List<StudentEntity> studentsProg;
	 

	public ProgressTrackerEntity() {
		super();
		this.isCompleted = false;
	}

	public ProgressTrackerEntity(int tracker_id, String actName, boolean isCompleted, int progPerc,
			List<StudentEntity> studentsProg) {
		super();
		this.tracker_id = tracker_id;
		this.actName = actName;
		this.isCompleted = isCompleted;
		this.progPerc = progPerc;
		this.studentsProg = studentsProg;
	}



	public int getTracker_id() {
		return tracker_id;
	}

	public void setTracker_id(int tracker_id) {
		this.tracker_id = tracker_id;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public int getProgPerc() {
		return progPerc;
	}

	public void setProgPerc(int progPerc) {
		this.progPerc = progPerc;
	}

	public List<StudentEntity> getStudentsProg() {
		return studentsProg;
	}

	public void setStudentsProg(List<StudentEntity> studentsProg) {
		this.studentsProg = studentsProg;
	}
	
}
