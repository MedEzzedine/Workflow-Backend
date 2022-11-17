package com.workflow.controller;

import com.workflow.entity.User;
import com.workflow.service.CustomUserDetailsService;
import com.workflow.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userService;


    @PostMapping("/firstuser")

    public User firstUser(@RequestBody User u, HttpServletRequest request) {
        User admin = jwtUtil.getuserFromRequest(request);
        admin.setFirstaccount(false);
        return new User();
    }

    @PostMapping("/updateuser")

    public ResponseEntity<?> updateUser(@RequestBody User u) {
        return userService.updateExistingUser(u);
    }

    @PostMapping("/adduser")
    public ResponseEntity<?> addClient(@RequestBody User u) {
        return userService.addUser(u);
    }

    @GetMapping("/getalluser")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(required = false) String search) {

        Pageable paging = PageRequest.of(page, size);

        return userService.getAllUsers(paging, search);

    }

}
