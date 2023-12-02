package com.wikangwiz.WikangWali.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;
import com.wikangwiz.WikangWali.Service.ProgressTrackerService;

@RestController
@RequestMapping("/progTracker")
@CrossOrigin(origins = "http://localhost:3000")
public class ProgressTrackerController {
	
	@Autowired
	ProgressTrackerService ptServ;
	
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - ProgressTracker";
	}
	
	//C - Create new record for tblprogTracker
	@PostMapping("/insertAchievement")
	public ProgressTrackerEntity insertAchievement(@RequestBody ProgressTrackerEntity progTracker) {
		return ptServ.insertProgressTracker(progTracker);
	}
	
	//R - Read all records tblprogTracker
	@GetMapping("/getAllProgressTrackers")
	public List<ProgressTrackerEntity> getAllProgressTrackers(){
		return ptServ.getAllProgressTrackers();
	}
	
	//U - Update a progTracker record.
	@PutMapping("/updateProgressTracker")
	public ProgressTrackerEntity updateProgressTracker(@RequestParam int tracker_id, @RequestBody ProgressTrackerEntity newProgressTrackerDetails) {
		return ptServ.updateProgressTracker(tracker_id, newProgressTrackerDetails);
	}
	
	//D - Delete a progTracker record.
	@DeleteMapping("/deleteProgressTracker/{tracker_id}")
	public String deleteAchievement(@PathVariable int tracker_id){
		return ptServ.deleteProgressTracker(tracker_id);
	}

}