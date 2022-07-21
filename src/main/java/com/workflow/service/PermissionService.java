package com.workflow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.entity.Permissions;
import com.workflow.repository.permissionsRepo;

@Service
public class PermissionService {

	@Autowired
	private permissionsRepo permissionrepo;
	
	
	public List<Permissions> getall_permissions(){
		return ((List<Permissions>) permissionrepo.findAll()).stream()
			      .filter(permission -> !permission.getNom().equals("tritux") && !permission.getNom().equals("drh") )
			      .collect(Collectors.toList());
	}
}
