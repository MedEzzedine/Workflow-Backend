package com.workflow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.entity.Permissions;
import com.workflow.entity.Role;
import com.workflow.repository.GroupeRepo;
import com.workflow.repository.RoleRepo;
import com.workflow.repository.permissionsRepo;

@Service
public class RoleService {

	@Autowired
	private RoleRepo role;
	@Autowired
	private permissionsRepo permrepo;
	@Autowired
	private GroupeRepo grouperepo;
	
	
	public Role addrole(Role r) {
		List<Integer> idpermission=new ArrayList<>();
	
	    if(r.getPermissions()!=null) {
	    	for(Permissions p :r.getPermissions()) {
	    		idpermission.add(p.getId());
	    	}
	    }
	   List <Permissions> get_permission_fromrepo= (List<Permissions>) permrepo.findAllById(idpermission);
	   Set<Permissions> permissions = new HashSet<Permissions>(get_permission_fromrepo);
	   r.setPermissions((permissions));
	   if(r.getGroupe().getId()!=null) {
		   r.setGroupe(grouperepo.findById(r.getGroupe().getId()).orElse(null));
	   }
		return role.save(r);
		
	}
	
    public List<Role> getall() {
		return role.findAll().stream()
			      .filter(role -> !role.getNom().equals("DRH")&& !role.getNom().equals("tritux") )
			      .collect(Collectors.toList());
		
	}
}
