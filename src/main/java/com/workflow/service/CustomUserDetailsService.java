package com.workflow.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.repository.RoleRepo;
import com.workflow.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepo RoleRepo;
 
    @Bean
    public PasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) repository.findByUserName(username);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
    public User getFulluser(String username) throws UsernameNotFoundException {
        return repository.findByUserName(username);
    }
    
	public ResponseEntity<?>  adduser(User u,int roleid) {
		
		Role r =RoleRepo.findById(roleid).orElse(null);
		u.getRoles().add(r);
		u.setPassword(passwordEncoder().encode(u.getPassword()));
		repository.save(u);
		return ResponseEntity.ok("success") ;	
	}
}