package com.wikangwiz.WikangWali.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wikangwiz.WikangWali.Entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer>{
	StudentEntity findByUsername(String username);
	StudentEntity findById(int student_id);
	StudentEntity findByEmail(String email);
	StudentEntity findByUsernameAndIsDeletedTrue(String username);
	StudentEntity findByUsernameAndIsDeletedFalse(String username);
	
	void deleteByUsername(String username);
	
	List<StudentEntity> findByIsDeletedTrue();
	List<StudentEntity> findByIsDeletedFalse();
	List<StudentEntity> findByUsernameAndIsDeleted(String username, boolean isDeleted);
}
