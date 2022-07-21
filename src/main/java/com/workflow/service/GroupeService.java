package com.workflow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workflow.entity.Groupe;
import com.workflow.repository.GroupeRepo;

@Service
public class GroupeService {
	
	@Autowired
	private GroupeRepo grouperepo;

	
  public List<Groupe> Allgroupe(){
	  return ((List<Groupe>) grouperepo.findAll()).stream()
		      .filter(groupe -> !groupe.getNom().equals("tritux"))
		      .collect(Collectors.toList());
  }
  
  
}
