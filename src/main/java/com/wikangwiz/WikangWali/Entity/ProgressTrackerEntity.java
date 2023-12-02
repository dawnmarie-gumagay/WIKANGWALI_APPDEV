package com.wikangwiz.WikangWali.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	 
	 @ManyToOne
	 @JoinColumn(name = "student_id")
	 private StudentEntity student;

	public ProgressTrackerEntity() {
		super();
	}

	public ProgressTrackerEntity(int tracker_id, String actName, boolean isCompleted, int progPerc,
			StudentEntity student) {
		super();
		this.tracker_id = tracker_id;
		this.actName = actName;
		this.isCompleted = isCompleted;
		this.progPerc = progPerc;
		this.student = student;
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

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

}
