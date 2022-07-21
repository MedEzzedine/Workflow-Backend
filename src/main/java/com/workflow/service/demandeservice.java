package com.workflow.service;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.workflow.entity.Demanderecu;
import com.workflow.entity.Demanderecu_id;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.entity.demande;
import com.workflow.entity.etat;
import com.workflow.entity.traitement;
import com.workflow.repository.UserRepository;
import com.workflow.repository.demandeRepo;
import com.workflow.util.JwtUtil;

@Service
public class demandeservice  {
	
	@Autowired
	private demandeRepo demandeRepo;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ActivityService ActivityService;
	
    @Autowired
    private UserRepository repository;
    
   public demande adddemande(demande d,HttpServletRequest request) {
	    User u=jwtUtil.getuserFromRequest(request);
	    d.setOwner(u);
	    d.setDate(new Date());
	    d.setPdf(false);
		Role role=u.getRoles().stream().findFirst().get();
	    demande demande = demandeRepo.save(d);
		ActivityService.startProcess(demande.getId(),role.getNiveau() );
	   	return demande ;
	   	
	   	
   }
   
   
   public Page<demande> getdemande(Pageable pageable,String search,HttpServletRequest request) {
	  // Page<demande> page = new PageImpl<>(demandeRepo.findAll(),pageable,demandeRepo.findAll().size());
	   User u=jwtUtil.getuserFromRequest(request);
	   Role  role=u.getRoles().stream().findFirst().get();
	   if(role.getNiveau()==3) {
		   if(search.equals("")) {
			   	return demandeRepo.findAll(pageable);
			   	
		   }
		   
		   	return demandeRepo.findByTypeCongeContaining(search,pageable);
	   }else {
		   if(search.equals("")) {
		  return demandeRepo.getAlldemandebyUser(u.getId(),pageable);
		   }
		   
		   List<demande> mydemande= demandeRepo.findAll().stream()
				      .filter(demande -> demande.getTypeConge().contains(search) || demande.getDateDebut().toString().contains(search))
				      .collect(Collectors.toList());
		   int start = (int) pageable.getOffset();
		   int end = (int) ((start + pageable.getPageSize()) > mydemande.size() ? mydemande.size()
				   : (start + pageable.getPageSize()));
		   Page<demande> mydemandepage = new PageImpl<>(mydemande.subList(start, end),pageable,mydemande.size());
		   
		   return mydemandepage;
		  
	   }
	 
   }
   
   public demande getdemandebyid(int id) {
	   
	   	return demandeRepo.findById(id).orElse(null);
  }

   
   public Demanderecu getdemandeTraitement (HttpServletRequest request)
	{
	   
	   Demanderecu demandes=new Demanderecu(new ArrayList<demande>(),new ArrayList<demande>());
	   User u=jwtUtil.getuserFromRequest(request);
	   Demanderecu_id demanderecu_id =ActivityService.getalltask(request);
	   if  (demanderecu_id.getDemande_enattente_id().size()>0) {
	   for (Integer integer : demanderecu_id.getDemande_enattente_id()) {
		   demande demande=demandeRepo.findById(integer).orElse(null);
		   if(demande!=null) {
		   if (demande.getOwner().getRoles().stream().findFirst().get().getGroupe().equals(u.getRoles().stream().findFirst().get().getGroupe()))
		   demandes.getDemande_enattente().add(demande);
		   }}
	   }
	   if  (demanderecu_id.getDemande_nonarrive_id().size()>0) {
	   for (Integer integer : demanderecu_id.getDemande_nonarrive_id()) {
		   demande demande=demandeRepo.findById(integer).orElse(null);
		   if(demande!=null) {
		   if (demande.getOwner().getRoles().stream().findFirst().get().getGroupe().equals(u.getRoles().stream().findFirst().get().getGroupe()))
		   demandes.getDemande_nonarrive().add(demande);
		   }} 
	}
	   return demandes;
	}
   
   
   
   
   
  // public List<demande> getAlldemandebyUser (HttpServletRequest request)
	//{
	//User u =jwtUtil.getuserFromRequest(request);
	//return demandeRepo.getAlldemandebyUser(u.getId());
	   
	   
	//}
   public Demanderecu getalldemandehistory (HttpServletRequest request )
	{
	   Demanderecu demandehistory= new Demanderecu(new ArrayList<>(),new ArrayList<>());
	   User u =jwtUtil.getuserFromRequest(request);
	   Role  role=u.getRoles().stream().findFirst().get();
	   if(role.getNiveau()==1) {
		   demandehistory.setDemande_enattente(demandeRepo.getmydemande(u.getNom()));  
	   }else if (role.getNiveau()==2) {

		   demandehistory.setDemande_enattente(demandeRepo.getotherdemande(u.getNom()));
		   demandehistory.setDemande_nonarrive(demandeRepo.getmydemande(u.getNom()));
	   }
	return demandehistory;
	   
	   
	}

   
   
   public List<demande> accept_refus_multidemande (HttpServletRequest request,List<demande> demandes,boolean AcceptOrRefus,boolean otherdemande)
  	{
  	   //demande demande=getdemandebyid(id);
  	 
  	 User u = ActivityService.multitraitement(request, demandes,AcceptOrRefus,otherdemande);
  	 System.out.println(u.getEmail());
  	 Role  role=u.getRoles().stream().findFirst().get();
  	 
  	 for (demande demande : demandes) {
  		if (AcceptOrRefus) {
  			if (role.getNiveau()==2) {
  			if(otherdemande) {
  			demande.setTraitement1(new traitement(etat.accepted,u.getNom()));
  		  	demande.setTraitement2(new traitement(etat.encours));
  			}
  			else {
  			demande.setTraitement2(new traitement(etat.accepted,u.getNom()));
  			demande.setDecision(etat.encours);
  			}	
  			} 
  			else 
  			{
  			demande.setTraitement1(new traitement(etat.accepted,u.getNom()));
  			demande.setTraitement2(new traitement(etat.encours));
  			}
  		 	}
  		    else {
  			if (role.getNiveau()==1)
  			{
  				
  			demande.setTraitement1(new traitement(etat.refused,u.getNom()));
  				
  			}else 
  			{
  			if(otherdemande) 
  				demande.setTraitement1(new traitement(etat.refused,u.getNom()));
  			else 
  				demande.setTraitement2(new traitement(etat.refused,u.getNom()));
  			}
  			 
  		}
  	 
  	  	 
  	 }

  	 List<demande> l=demandeRepo.saveAll(demandes);
  	
  		return demandes;
  	}
   
    public demande demandedetail(int id) {
	   return demandeRepo.findById(id).orElse(null);
   }
    
    public List<demande> getfinaldemande(){
    	
    return demandeRepo.getfinaldemande(etat.encours);
    }
    
    public demande getdDemandebyid(int id) {
    	return this.demandeRepo.getById(id);
    }
    
    public demande savedeDemande(demande demande) {
    	return this.demandeRepo.save(demande);
    }
    
    public demande finalrefus(demande demande) {
    	demande.setDecision(etat.refused);
    	return this.demandeRepo.save(demande);
    }
   
}
