package com.workflow.repository;

import com.workflow.entity.Demande;
import com.workflow.entity.Etat;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepo extends JpaRepository<Demande, Integer> {

    @Query("SELECT d FROM Demande d WHERE d.owner.id= :id")
    Page<Demande> getAllDemandebyUser(@Param("id") int id, Pageable pageable);

    @Query("SELECT d FROM Demande d WHERE d.traitement1.by=:name")
    List<Demande> getMyDemande(@Param("name") String name);

    @Query("SELECT d FROM Demande d WHERE d.traitement2.by=:name")
    List<Demande> getOtherDemande(@Param("name") String name);


    Page<Demande> findByTypeCongeContaining(String search, Pageable pageable);

    @Query("SELECT d FROM Demande d WHERE d.decision=:encours")
    List<Demande> getfinaldemande(Etat encours);

}
