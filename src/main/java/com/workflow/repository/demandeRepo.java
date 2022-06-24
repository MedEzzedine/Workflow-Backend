package com.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workflow.entity.demande;

@Repository
public interface demandeRepo extends JpaRepository<demande, Integer>{

}
