package com.workflow.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.workflow.config.JwtResponse;
import com.workflow.entity.AuthRequest;
import com.workflow.entity.User;
import com.workflow.util.JwtUtil;
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

    @GetMapping("/welcome")
    public String welcome(@RequestBody AuthRequest authRequest) {
        return "Welcome to workflow !!";
        
    }

    @Autowired
    private RuntimeService runtimeService;

    
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
       Map<String, Object> variables = new HashMap<String, Object>();
       variables.put("applicantName", "John Doe");
       variables.put("email", "john.doe@activiti.com");
       variables.put("phoneNumber", "123456789");
       runtimeService.startProcessInstanceByKey("myProcess", variables);
       return "Process started. Number of currently running"
         + "process instances= "
         + runtimeService.createProcessInstanceQuery().count();
   }
   
   
}