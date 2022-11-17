package com.workflow.controller;

import com.workflow.config.JwtResponse;
import com.workflow.entity.AuthRequest;
import com.workflow.entity.User;
import com.workflow.service.CustomUserDetailsService;
import com.workflow.service.RoleService;
import com.workflow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private RoleService roleService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            throw new Exception("Invalid username or password");
        }
        String jwt = jwtUtil.generateToken(authRequest.getEmail());
        return ResponseEntity.ok(new JwtResponse(jwt, userService.getFulluser(authRequest.getEmail())));
    }

    @GetMapping("/getuser")
    public User getuserFromRequest(HttpServletRequest request) {

        return jwtUtil.getuserFromRequest(request);
    }

    @PostMapping("/tritux")
    public User trituxAccount(@RequestBody User u) {
        return userService.createTrituxAccount(u);
    }
}