package com.workflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		Role role=u.getRoles().stream().findFirst().get();
	    demande demande = demandeRepo.save(d);
		ActivityService.startProcess(role.getRolesup().getNom(), demande.getId(),role.getNiveau() );
	   	return demande ;
	   	
	   	
   }
   
   
   public List<demande> getdemande() {
	   
	   	return demandeRepo.findAll();
   }
   
   public demande getdemandebyid(int id) {
	   
	   	return demandeRepo.findById(id).orElse(null);
  }

   
   public List<demande> getdemandeTraitement (HttpServletRequest request)
	{
	   List<demande> demande=new ArrayList<>();
	   
	   List<Integer> demandeid=ActivityService.getalltask(request);
	   System.out.println(demandeid);
	   if  (demandeid.size()>0) {
	   for (Integer integer : demandeid) {
		   demande.add(demandeRepo.findById(integer).orElse(null));
		    }
	}
	   return demande;
	}
   
   
   
   public demande accept_refus_demande (HttpServletRequest request,int id,boolean AcceptOrRefus)
	{
	   demande demande=getdemandebyid(id);
	 
	 User u = ActivityService.traitement(request, id,AcceptOrRefus);
	 Role  role=u.getRoles().stream().findFirst().get();
		 if (AcceptOrRefus) {
			if (role.getNiveau()==2) {
				demande.setTraitement2(new traitement(etat.accepted,u.getUserName()));}
			else 
			{
			demande.setTraitement1(new traitement(etat.accepted,u.getUserName()));
			demande.setTraitement2(new traitement(etat.encours));
			}
		 	}else {
			if (role.getNiveau()==1)
			{
				demande.setTraitement1(new traitement(etat.refused,u.getUserName()));
			}else 
			{
			demande.setTraitement2(new traitement(etat.refused,u.getUserName()));
			}
			 
		}
	demandeRepo.save(demande);
		return demande;
	}
   
   

}
