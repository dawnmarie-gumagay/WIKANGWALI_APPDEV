package com.wikangwiz.WikangWali.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wikangwiz.WikangWali.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	Optional<UserEntity> findByUsername(String username);
}
