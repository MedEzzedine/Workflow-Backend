package com.workflow.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.workflow.entity.Permissions;
@Repository
public interface permissionsRepo extends CrudRepository<Permissions, Integer> {

}
