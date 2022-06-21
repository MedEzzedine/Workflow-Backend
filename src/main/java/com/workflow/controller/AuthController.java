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
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.workflow.config.JwtResponse;
import com.workflow.entity.AuthRequest;
import com.workflow.entity.User;
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
    private ManagementService managementService;


    @Autowired
    private RuntimeService runtimeService;
    

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    
    

    
    
    
    @GetMapping("/welcome")
    public String welcome(@RequestBody AuthRequest authRequest) {
        return "Welcome to workflow !!";
        
    }


    
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
    
    @PostMapping("/register")
	@ResponseBody
	public ResponseEntity<?> addClient(@RequestBody User u)
	{
	return UserService.adduser(u);
	
	}
    
   @PostMapping("/")
   @ResponseBody
    public User getuserFromRequest(HttpServletRequest request) {
    
    return jwtUtil.getuserFromRequest(request);
    }
   
   @GetMapping("/start-process")
   public String startProcess() {
     
       //runtimeService.startProcessInstanceByKey("myProcess");
      // runtimeService.startProcessInstanceByKey("my_Process" );
	   //System.out.println(taskService.createTaskQuery().taskCandidateGroup("userone").list()); 
	   //System.out.println(taskService.createTaskQuery().taskCandidateGroup("userone").singleResult().getName());
	
	   //taskService.complete("2505");
	 // System.out.println(taskService.getTaskEvents("2505"));
	   //ManagementService m =managementService.getTableName(getClass());
	  // System.out.println(managementService.getTableName(Task.class));
	  
	   //for(HistoricIdentityLink h: historyService.getHistoricIdentityLinksForTask("2505") ) {
	//	   System.out.println(h.getGroupId());
	 //  };
	   
	   //runtimeService.deleteProcessInstance("20001", null);
		
	   //taskService.deleteTask("20005");
	   
	   //________________________
	   
	  // Map<String, Object> variables = new HashMap<String, Object>();
	   //variables.put("User_name", "hmed");
	  // variables.put("Role","Java" );
	  // variables.put("Duration", "3 weeks");
	  // runtimeService.startProcessInstanceByKey("myProcess");
	   
	   
	   List<Task> tasks= taskService.createTaskQuery().taskCandidateGroup("DJava").list();
	   for (Task task : tasks) {
		   System.out.println("task available "+ task.getId());
	   }

	   System.out.println();
	 
	   System.out.println(taskService.createTaskQuery().taskCandidateGroup("DJava").list().get(0));
	   System.out.println(
	   runtimeService.getVariables(taskService.createTaskQuery().taskCandidateGroup("DJava").list().get(0).getExecutionId()));
	   
       return "Process started. Number of currently running"
         + "process instances= "+ runtimeService.createProcessInstanceQuery().count();
       
   }
   
   
}