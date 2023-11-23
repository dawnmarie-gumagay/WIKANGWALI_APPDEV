package com.wikangwiz.WikangWali.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wikangwiz.WikangWali.Entity.UserEntity;
import com.wikangwiz.WikangWali.Repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository urepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserEntity> userOpt = urepo.findByUsername(username);
		
		return userOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
	}
}