package com.workflow.service;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.entity.demande;
import com.workflow.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
@Service
public class ActivityService {
	
    @Autowired
    private ManagementService managementService;


    @Autowired
    private RuntimeService runtimeService;
    

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

	@Autowired
	private JwtUtil jwtUtil;
    

	
	  
	  
	  
	  public void startProcess( String to,int iddemande,int level) {
		  
		   Map<String, Object> variables = new HashMap<String, Object>();
		 
		   if (level==0) {

				  variables.put("traitement1role", to);
				  variables.put("level",1);

			   variables.put("iddemande",iddemande);
		   }
		   else
		   {
			   variables.put("traitement2role", to);
			   variables.put("level",2);

				  variables.put("iddemande",iddemande); 
		   }
		   ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("myProcess",variables);
		   
		   System.out.println("Process started. Number of currently running"
			         + "process instances= "+ runtimeService.createProcessInstanceQuery().count());
		     
	       
	   }
	
	  
	  public   List<Integer> getalltask(HttpServletRequest request ) {
		  User u=jwtUtil.getuserFromRequest(request);
		  String role=u.getRoles().stream().findFirst().get().getNom();
		  List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(role).list();
		  List<Integer> demandeid=new ArrayList<>();
		  for (Task task : tasks) {  
		    System.out.println("Task available: " + task.getId());
		    demandeid.add((Integer) runtimeService.getVariables(task.getExecutionId()).get("iddemande"));
		    System.out.println(runtimeService.getVariables(task.getExecutionId()).get("iddemande"));
		    
		    
		  }
		  System.out.println(demandeid);
		  return demandeid;
	     
	   }
	  
	  public User traitement(HttpServletRequest request,int id ,boolean AcceptOrRefus) {

		  User u=jwtUtil.getuserFromRequest(request);
		  Role  role=u.getRoles().stream().findFirst().get();
		  
		  List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(role.getNom()).list();
		  String taskid=null;
		  for (Task task : tasks) {  
		      if((Integer) runtimeService.getVariables(task.getExecutionId()).get("iddemande")==id) 
		    	taskid=task.getId();
		    
		 // System.out.println(runtimeService.getVariables(task.getExecutionId()).get("iddemande"));  
		  }  
		  Map<String, Object> variables = new HashMap<String, Object>(); 

		  
		  if (AcceptOrRefus) {

			  if (role.getNiveau()==2) {variables.put("accept", true);}else{
			  		
			  variables.put("traitement1", true);
			  variables.put("traitement2role",role.getRolesup().getNom());
			  }
		  }else {

			  		if (role.getNiveau()==2) variables.put("accept", false);
			  		else{
			  variables.put("traitement1", false);}
		  }
		  
		  taskService.complete(taskid,variables);
		  System.out.println(taskid);
	     return u;
	   }
}
