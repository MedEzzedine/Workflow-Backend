package com.workflow.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.workflow.entity.demande;

@Repository
public interface demandeRepo extends JpaRepository<demande, Integer>{

	@Query("SELECT d FROM demande d WHERE d.owner.id= :id")
	List<demande> getAlldemandebyUser(@Param("id") int id);
}
