package com.wikangwiz.WikangWali.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wikangwiz.WikangWali.Entity.ProgressTrackerEntity;

public interface ProgressTrackerRepository extends JpaRepository<ProgressTrackerEntity, Integer>{
	ProgressTrackerEntity findById(int tracker_id);
}
