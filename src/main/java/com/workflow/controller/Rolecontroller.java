package com.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.workflow.entity.Role;
import com.workflow.service.RoleService;

@RestController
@RequestMapping("/role")
@CrossOrigin("*")


public class Rolecontroller {

@Autowired
	private RoleService roleservice;



@PostMapping("/addrole")
@ResponseBody
public Role  addrole (@RequestBody Role role)
{
return roleservice.addrole(role);

}

@GetMapping("/allrole")
@ResponseBody
public List<Role> allrole ()
{
return roleservice.getall();

}

}
