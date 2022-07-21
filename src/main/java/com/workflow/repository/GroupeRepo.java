package com.workflow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workflow.entity.Groupe;
@Repository
public interface GroupeRepo extends CrudRepository<Groupe, Integer> {

}
