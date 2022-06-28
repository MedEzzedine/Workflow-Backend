package com.workflow.controller;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.workflow.config.JwtResponse;
import com.workflow.entity.AuthRequest;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.repository.UserRepository;
import com.workflow.util.JwtUtil;

import ch.qos.logback.classic.Logger;

import com.workflow.service.*;



@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    

    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService UserService;
    
    @Autowired
    private RoleService roleService;


    
    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("invalid username/password");
        }
        String jwt=jwtUtil.generateToken(authRequest.getUserName());
        return ResponseEntity.ok(new JwtResponse(jwt,UserService.getFulluser(authRequest.getUserName())));
    }
    
    @PostMapping("/register/{roleid}")
	@ResponseBody
	public ResponseEntity<?> addClient(@RequestBody User u,@PathVariable int roleid)
	{
	return UserService.adduser(u,roleid);
	 	
	}
    
   @PostMapping("/")
   @ResponseBody
    public User getuserFromRequest(HttpServletRequest request) {
     
    return jwtUtil.getuserFromRequest(request);
    }
   
   
   @PostMapping("/addrole/{rolesupid}")
   @ResponseBody
   public Role addrole(@RequestBody Role r,@PathVariable int rolesupid) {
	   return roleService.addrole(r,rolesupid);
   }
}