package com.wikangwiz.WikangWali.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wikangwiz.WikangWali.Entity.PointEntity;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, Integer>{
	PointEntity findById(int point_id);
}
