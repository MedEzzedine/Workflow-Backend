package com.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.entity.Role;
import com.workflow.repository.RoleRepo;

@Service
public class RoleService {

	@Autowired
	RoleRepo role;
	
	
	public Role addrole(Role r,int rolesupid) {
	    
		Role rolesup = role.findById(rolesupid).orElse(null);
		r.setRolesup(rolesup);
		return role.save(r);
		
	}
}
