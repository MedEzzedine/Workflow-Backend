package com.workflow.repository;

import com.workflow.entity.Groupe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupeRepo extends CrudRepository<Groupe, Integer> {

}
