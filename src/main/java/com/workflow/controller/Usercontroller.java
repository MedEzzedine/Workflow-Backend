package com.workflow.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.entity.User;
import com.workflow.service.CustomUserDetailsService;
import com.workflow.util.JwtUtil;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class Usercontroller {
	 @Autowired
	    private JwtUtil jwtUtil;

	 @Autowired
	    private CustomUserDetailsService UserService;
	 
	
	 
	@PostMapping("/firstuser")
 	
	public User firstuser (@RequestBody User u,HttpServletRequest request) {
		User admin=jwtUtil.getuserFromRequest(request);
		admin.setFirstaccount(false);
		return UserService.firstuser(u);
	}
	
	@PostMapping("/updateuser")
 	
	public ResponseEntity<?> updateuser (@RequestBody User u) {
		return UserService.update(u);
	}

	@PostMapping("/adduser")
	@ResponseBody
	public ResponseEntity<?> addClient(@RequestBody User u)
	{
	return UserService.adduser(u); 
	}
	
	@GetMapping("/getalluser")
	@ResponseBody
	public Page<User> getalluser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,@RequestParam(required = false) String search)
	{
		Pageable paging = PageRequest.of(page, size);
		
	return UserService.getalluser(paging,search); 
	
	}
	
}
