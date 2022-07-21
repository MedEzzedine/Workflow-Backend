package com.workflow.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.workflow.entity.demande;
import com.workflow.entity.etat;

@Repository
public interface demandeRepo extends JpaRepository<demande, Integer>{

	@Query("SELECT d FROM demande d WHERE d.owner.id= :id")
	Page<demande> getAlldemandebyUser(@Param("id") int id,Pageable pageable);
	
	@Query("SELECT d FROM demande d WHERE d.traitement1.by=:name")
	List<demande> getmydemande(@Param("name") String name);
	
	@Query("SELECT d FROM demande d WHERE d.traitement2.by=:name")
	List<demande> getotherdemande(@Param("name") String name);
	
	
	Page<demande> findByTypeCongeContaining(String search,Pageable pageable);
	
	@Query("SELECT d FROM demande d WHERE d.decision=:encours")
	List<demande> getfinaldemande(etat encours);
	
}
