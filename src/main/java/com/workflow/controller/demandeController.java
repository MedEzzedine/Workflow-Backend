package com.workflow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.entity.User;
import com.workflow.entity.demande;
import com.workflow.service.ActivityService;
import com.workflow.service.demandeservice;

@RestController
@RequestMapping("/demande")
@CrossOrigin("*")
public class demandeController {
	
	@Autowired
	private demandeservice demandeservice;


	@Autowired
	private ActivityService ActivityService;

	@PostMapping("/adddemande")
 	@ResponseBody
 	public demande adddemande(@RequestBody demande d,HttpServletRequest request )
 	{
	
	demande demande=demandeservice.adddemande(d, request);
		
	return demande;

	}

	@PostMapping("/getdemande")
	@ResponseBody
	public List<demande> getalldemande()
	{
	return demandeservice.getdemande();

	}


	@GetMapping("/getAll_demandeByrole")
	@ResponseBody
	public List<demande>  getAll_demande1Byrole (HttpServletRequest request)
	{
    return demandeservice.getdemandeTraitement(request);
    
	}
	
	
	@PostMapping("/accept_refus_demande/{id}/{AcceptOrRefus}")
	@ResponseBody
	public demande accept_refus_demande (@PathVariable int id ,HttpServletRequest request,@PathVariable Boolean AcceptOrRefus)
	{
		
     return demandeservice.accept_refus_demande(request,id,AcceptOrRefus);
	} 
	
	
	
	
	
	
	
	
	
	
	}


	



