package com.workflow.repository;

import com.workflow.entity.Permissions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepo extends CrudRepository<Permissions, Integer> {

}
