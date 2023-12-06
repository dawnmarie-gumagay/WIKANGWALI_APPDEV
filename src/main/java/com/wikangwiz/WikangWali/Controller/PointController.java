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

import com.wikangwiz.WikangWali.Entity.PointEntity;
import com.wikangwiz.WikangWali.Repository.PointRepository;
import com.wikangwiz.WikangWali.Repository.StudentRepository;
import com.wikangwiz.WikangWali.Service.PointService;

@RestController
@RequestMapping("/point")
@CrossOrigin(origins = "http://localhost:3000")
public class PointController {
	
	@Autowired
	PointService pServ;
	
	@Autowired
	PointRepository pRepo;
	
	@Autowired
	StudentRepository srepo;
	
	@GetMapping("/print")
	public String printHello(){
		return "WikangWali - Point";
	}
	
	//C - Create new record for tblpoint
	@PostMapping("/insertPoint")
	public PointEntity insertPoint(@RequestBody PointEntity achievement) {
		return pServ.insertPoint(achievement);
	}
	
	//R - Read all records in tblpoint
	@GetMapping("/getAllPoints")
	public List<PointEntity> getAllPoints(){
		return pServ.getAllPoints();
	}
	
	//U - Update a achievement record.
	@PutMapping("/updatePoint")
	public PointEntity updatePoint(@RequestParam int point_id, @RequestBody PointEntity newPointDetails) {
		return pServ.updatePoint(point_id, newPointDetails);
	}
	
	//D - Delete a achievement record.
	@DeleteMapping("/deletePoint/{point_id}")
	public String deletePoint(@PathVariable int point_id){
		return pServ.deletePoint(point_id);
	}

}