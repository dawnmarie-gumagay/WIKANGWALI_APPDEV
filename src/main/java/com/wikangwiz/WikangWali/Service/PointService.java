package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.PointEntity;
import com.wikangwiz.WikangWali.Entity.StudentEntity;
import com.wikangwiz.WikangWali.Repository.PointRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;

@Service
public class PointService {
	
	@Autowired
	PointRepository pRepo;
	
	@Autowired
	StudentRepository srepo;
	
	@Autowired
    private StudentService sserv;
	
	public PointEntity findPointById(int point_id) {
        return pRepo.findById(point_id);
	}
	
	//C - Create new record for tblPoint
	public PointEntity insertPoint(PointEntity point) {
		return pRepo.save(point);
	}
	
	//R - Read all records inpoint_ide tblPoint
	public List<PointEntity> getAllPoints(){
		return pRepo.findAll();
	}
	
	//U - Update a Point record in tblPoint
	@SuppressWarnings("finally")
	public PointEntity updatePoint(int point_id, PointEntity newPointDetails) {
		PointEntity point = new PointEntity();
		try {
			//1.) Search the id number of the Point that will be updated/
			point = pRepo.findById(point_id);
			
			//2.) Once found, update the Point record.
			point.setPoint_name(newPointDetails.getPoint_name());
			point.setPoint_num(newPointDetails.getPoint_num());
			
		}
		catch(NoSuchElementException ex){
			new NoSuchElementException("Point "+ point_id + " does not exist!");
		}
		finally {
			return pRepo.save(point);
		}
	}
	
	//D - delete a Point record.
	public String deletePoint(int point_id){
		String msg = "";
		
		//If point_id is not equal to null, and is found, it can be deleted.
		if(pRepo.findById(point_id) != null) {
			pRepo.deleteById(point_id);
			msg = "Point " + point_id + " is successfully deleted!";
		}
		else {
			msg = "Point " + point_id + " does not exist.";
		}
		return msg;
	}
	
	///////////////////////////////////
	
	//ADD POINT TO USER
	public PointEntity linkPointToStudent(int student_id, int point_id) {
	    // Check if the student exists
	    StudentEntity student = sserv.getStudentById(student_id);

	    // Check if the point exists
	    PointEntity point = pRepo.findById(point_id);

	    if (student != null) {
	        if (point != null) {
	            // Check if the user is not deleted
	            if (!student.getIsDeleted()) {
	                // Check if the point is not already associated with the user
	                if (!student.getPoints().contains(point)) {
	                    student.getPoints().add(point);
	                    // Save the updated student to reflect the new association
	                    sserv.updateStudentObject(student);
	                    return point;
	                } else {
	                    throw new IllegalArgumentException("Point already linked to the student");
	                }
	            } else {
	                throw new IllegalArgumentException("Cannot link point to a deleted user");
	            }
	        } else {
	            throw new NoSuchElementException("Point with ID " + point_id + " not found");
	        }
	    } else {
	        throw new NoSuchElementException("Student with ID " + student_id + " does not exist!");
	    }
	}
}
