package com.workflow.service;

import com.workflow.entity.*;
import com.workflow.repository.UserRepository;
import com.workflow.repository.DemandeRepo;
import com.workflow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepo demandeRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ActivityService ActivityService;

    @Autowired
    private UserRepository repository;

    public Demande adddemande(Demande d, HttpServletRequest request) {
        User u = jwtUtil.getuserFromRequest(request);
        d.setOwner(u);
        d.setDate(new Date());
        d.setPdf(false);
        Role role = u.getRoles().stream().findFirst().get();
        Demande demande = demandeRepo.save(d);
        ActivityService.startProcess(demande.getId(), role.getNiveau());
        return demande;


    }


    public Page<Demande> getdemande(Pageable pageable, String search, HttpServletRequest request) {
        // Page<demande> page = new PageImpl<>(demandeRepo.findAll(),pageable,demandeRepo.findAll().size());
        User u = jwtUtil.getuserFromRequest(request);
        Role role = u.getRoles().stream().findFirst().get();
        if (role.getNiveau() == 3) {
            if (search.equals("")) {
                return demandeRepo.findAll(pageable);

            }

            return demandeRepo.findByTypeCongeContaining(search, pageable);
        } else {
            if (search.equals("")) {
                return demandeRepo.getAllDemandebyUser(u.getId(), pageable);
            }

            List<Demande> mydemande = demandeRepo.findAll().stream()
                    .filter(demande -> demande.getTypeConge().contains(search) || demande.getDateDebut().toString().contains(search))
                    .collect(Collectors.toList());
            int start = (int) pageable.getOffset();
            int end = (int) Math.min((start + pageable.getPageSize()), mydemande.size());

            return new PageImpl<>(mydemande.subList(start, end), pageable, mydemande.size());

        }

    }

    public Demande getdemandebyid(int id) {

        return demandeRepo.findById(id).orElse(null);
    }


    public Demanderecu getdemandeTraitement(HttpServletRequest request) {

        Demanderecu demandes = new Demanderecu(new ArrayList<Demande>(), new ArrayList<Demande>());
        User u = jwtUtil.getuserFromRequest(request);
        Demanderecu_id demanderecu_id = ActivityService.getalltask(request);
        if (demanderecu_id.getDemande_enattente_id().size() > 0) {
            for (Integer integer : demanderecu_id.getDemande_enattente_id()) {
                Demande demande = demandeRepo.findById(integer).orElse(null);
                if (demande != null) {
                    if (demande.getOwner().getRoles().stream().findFirst().get().getGroupe().equals(u.getRoles().stream().findFirst().get().getGroupe()))
                        demandes.getDemande_enattente().add(demande);
                }
            }
        }
        if (demanderecu_id.getDemande_nonarrive_id().size() > 0) {
            for (Integer integer : demanderecu_id.getDemande_nonarrive_id()) {
                Demande demande = demandeRepo.findById(integer).orElse(null);
                if (demande != null) {
                    if (demande.getOwner().getRoles().stream().findFirst().get().getGroupe().equals(u.getRoles().stream().findFirst().get().getGroupe()))
                        demandes.getDemande_nonarrive().add(demande);
                }
            }
        }
        return demandes;
    }


    // public List<demande> getAlldemandebyUser (HttpServletRequest request)
    //{
    //User u =jwtUtil.getuserFromRequest(request);
    //return demandeRepo.getAlldemandebyUser(u.getId());


    //}
    public Demanderecu getAllDemandeHistory(HttpServletRequest request) {
        Demanderecu demandehistory = new Demanderecu(new ArrayList<>(), new ArrayList<>());
        User u = jwtUtil.getuserFromRequest(request);
        Role role = u.getRoles().stream().findFirst().get();
        if (role.getNiveau() == 1) {
            demandehistory.setDemande_enattente(demandeRepo.getMyDemande(u.getNom()));
        } else if (role.getNiveau() == 2) {

            demandehistory.setDemande_enattente(demandeRepo.getOtherDemande(u.getNom()));
            demandehistory.setDemande_nonarrive(demandeRepo.getMyDemande(u.getNom()));
        }
        return demandehistory;


    }


    public List<Demande> accept_refus_multidemande(HttpServletRequest request, List<Demande> demandes, boolean AcceptOrRefus, boolean otherdemande) {
        //demande demande=getdemandebyid(id);

        User u = ActivityService.multitraitement(request, demandes, AcceptOrRefus, otherdemande);
        System.out.println(u.getEmail());
        Role role = u.getRoles().stream().findFirst().get();

        for (Demande demande : demandes) {
            if (AcceptOrRefus) {
                if (role.getNiveau() == 2) {
                    if (otherdemande) {
                        demande.setTraitement1(new Traitement(Etat.accepted, u.getNom()));
                        demande.setTraitement2(new Traitement(Etat.encours));
                    } else {
                        demande.setTraitement2(new Traitement(Etat.accepted, u.getNom()));
                        demande.setDecision(Etat.encours);
                    }
                } else {
                    demande.setTraitement1(new Traitement(Etat.accepted, u.getNom()));
                    demande.setTraitement2(new Traitement(Etat.encours));
                }
            } else {
                if (role.getNiveau() == 1) {

                    demande.setTraitement1(new Traitement(Etat.refused, u.getNom()));

                } else {
                    if (otherdemande)
                        demande.setTraitement1(new Traitement(Etat.refused, u.getNom()));
                    else
                        demande.setTraitement2(new Traitement(Etat.refused, u.getNom()));
                }

            }


        }

        List<Demande> l = demandeRepo.saveAll(demandes);

        return demandes;
    }

    public Demande demandedetail(int id) {
        return demandeRepo.findById(id).orElse(null);
    }

    public List<Demande> getfinaldemande() {

        return demandeRepo.getfinaldemande(Etat.encours);
    }

    public Demande getdDemandebyid(int id) {
        return this.demandeRepo.getById(id);
    }

    public Demande savedeDemande(Demande demande) {
        return this.demandeRepo.save(demande);
    }

    public Demande finalrefus(Demande demande) {
        demande.setDecision(Etat.refused);
        return this.demandeRepo.save(demande);
    }

}
