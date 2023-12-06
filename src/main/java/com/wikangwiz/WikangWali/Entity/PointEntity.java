package com.wikangwiz.WikangWali.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tblpoint")
public class PointEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int point_id;
	
	private String point_name;
	
	private int point_num;

    @JsonIgnore
	@ManyToMany(mappedBy = "points")
    private List<StudentEntity> studentsPoints;

	
	public PointEntity() {
		super();
    	this.studentsPoints = new ArrayList<>(); // Initialize the students list
	}
	

	public PointEntity(int point_id, String point_name, int point_num, List<StudentEntity> studentsPoints) {
		super();
		this.point_id = point_id;
		this.point_name = point_name;
		this.point_num = point_num;
		this.studentsPoints = studentsPoints;
	}

	


	public int getPoint_id() {
		return point_id;
	}


	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}


	public String getPoint_name() {
		return point_name;
	}


	public void setPoint_name(String point_name) {
		this.point_name = point_name;
	}


	public int getPoint_num() {
		return point_num;
	}


	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}


	public List<StudentEntity> getStudentsPoints() {
		return studentsPoints;
	}


	public void setStudentsPoints(List<StudentEntity> studentsPoints) {
		this.studentsPoints = studentsPoints;
	}


	////////////////////////
	public void addStudent(StudentEntity student) {
		studentsPoints.add(student);
	}

}
