package com.wikangwiz.WikangWali.Service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Repository.ProgressTrackerRepository;

@Service
public class ProgressTrackerService {
	
	@Autowired
	ProgressTrackerRepository ptRepo;
	
	public ProgressTrackerEntity findProgressTrackerById(int tracker_id) {
        return ptRepo.findById(tracker_id);
	}
	
	//C - Create new record for tblProgressTracker
	public ProgressTrackerEntity insertProgressTracker(ProgressTrackerEntity progressTracker) {
		return ptRepo.save(progressTracker);
	}
	
	//R - Read all records intracker_ide tblProgressTracker
	public List<ProgressTrackerEntity> getAllProgressTrackers(){
		return ptRepo.findAll();
	}
	
	//U - Update a Achievement record in tblProgressTracker
	@SuppressWarnings("finally")
	public ProgressTrackerEntity updateProgressTracker(int tracker_id, ProgressTrackerEntity newProgressTrackerDetails) {
		ProgressTrackerEntity progressTracker = new ProgressTrackerEntity();
		try {
			//1.) Search the id number of the ProgressTracker that will be updated/
			progressTracker = ptRepo.findById(tracker_id);
			
			//2.) Once found, update the Achievement record.
			progressTracker.setActName(newProgressTrackerDetails.getActName());
			progressTracker.setCompleted(newProgressTrackerDetails.isCompleted());
			progressTracker.setProgPerc(newProgressTrackerDetails.getProgPerc());
		}
		catch(NoSuchElementException ex){
			new NoSuchElementException("Achievement "+ tracker_id + " does not exist!");
		}
		finally {
			return ptRepo.save(progressTracker);
		}
	}
	
	//D - delete a ProgressTracker record.
	public String deleteProgressTracker(int tracker_id){
		String msg = "";
		
		//If tracker_id is not equal to null, and is found, it can be deleted.
		if(ptRepo.findById(tracker_id) != null) {
			ptRepo.deleteById(tracker_id);
			msg = "Progress Tracker " + tracker_id + " is successfully deleted!";
		}
		else {
			msg = "Progress Tracker " + tracker_id + " does not exist.";
		}
		return msg;
	}
	
}
