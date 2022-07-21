package com.workflow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.entity.Demanderecu;
import com.workflow.entity.User;
import com.workflow.entity.demande;
import com.workflow.service.ActivityService;
import com.workflow.service.demandeservice;

import net.bytebuddy.build.HashCodeAndEqualsPlugin.Sorted;

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

	@GetMapping("/getdemande")
	@ResponseBody
	public Page<demande> getalldemande(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,@RequestParam(required = false) String search,HttpServletRequest request) {
	Pageable paging = PageRequest.of(page, size);
	
	return demandeservice.getdemande(paging,search, request);
	
	}


	@GetMapping("/GetDemandeRecue")
	@ResponseBody
	public Demanderecu  getAll_demande1Byrole (HttpServletRequest request)
	{
    return demandeservice.getdemandeTraitement(request);
     
	}
	
	
	/**@PostMapping("/accept_refus_demande/{id}/{AcceptOrRefus}")
	@ResponseBody
	public demande accept_refus_demande (@PathVariable int id ,HttpServletRequest request,@PathVariable Boolean AcceptOrRefus)
	{
		
     return demandeservice.accept_refus_demande(request,id,AcceptOrRefus);
	} **/
	
	//@GetMapping("/getmydemande")
	//@ResponseBody
	// public List<demande> getAlldemandebyUser (HttpServletRequest request)
	//	{
	//return demandeservice.getAlldemandebyUser(request);
	//	}
	
	 
	 @GetMapping("/getalldemandehistory")
		@ResponseBody
		 public Demanderecu getalldemandehistory(HttpServletRequest request )
			{
		return demandeservice.getalldemandehistory(request);
			}
	
		
		@PostMapping("/accept_refus_multidemande/{AcceptOrRefus}/{otherdemande}")
		@ResponseBody
		public List<demande> accept_refus_multidemande (@RequestBody List<demande> demandes ,HttpServletRequest request,@PathVariable Boolean AcceptOrRefus,@PathVariable boolean otherdemande)
		{ 
				
		    return demandeservice.accept_refus_multidemande(request,demandes,AcceptOrRefus,otherdemande);
		}
		
		
		
		
		 @GetMapping("/getdemandedetail/{id}")
		 @ResponseBody
		 public demande demandedetail(@PathVariable int id) {
			   return demandeservice.demandedetail( id);
		   }
		 

		  @GetMapping("/getfinaldemande")
		  @ResponseBody
		  public List<demande> getfinaldemande() {
			   return demandeservice.getfinaldemande( );
		   }
	
		  
		  @PutMapping("/refusfinal")
		  @ResponseBody
		  public demande getfinaldemande(@RequestBody demande demande) {
			 return demandeservice.finalrefus(demande);
		   }
	}


	



