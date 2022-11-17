package com.workflow.controller;

import com.workflow.entity.Role;
import com.workflow.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@CrossOrigin("*")


public class RoleController {

    @Autowired
    private RoleService roleservice;


    @PostMapping("/addrole")
    public Role addrole(@RequestBody Role role) {
        return roleservice.addrole(role);

    }

    @GetMapping("/allrole")
    public List<Role> allrole() {
        return roleservice.getall();

    }

}
