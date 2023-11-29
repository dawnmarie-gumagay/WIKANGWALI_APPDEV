package com.wikangwiz.WikangWali.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wikangwiz.WikangWali.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	UserEntity findByUsername(String username);
	void deleteByUsername(String username);
	
}
