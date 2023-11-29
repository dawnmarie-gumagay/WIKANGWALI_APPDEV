package com.wikangwiz.WikangWali.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wikangwiz.WikangWali.Entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer>{
	StudentEntity findByUsername(String username);
}
