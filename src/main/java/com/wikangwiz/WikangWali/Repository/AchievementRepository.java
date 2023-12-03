package com.wikangwiz.WikangWali.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wikangwiz.WikangWali.Entity.AchievementEntity;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, Integer>{
	AchievementEntity findById(int achievement_id);
}
