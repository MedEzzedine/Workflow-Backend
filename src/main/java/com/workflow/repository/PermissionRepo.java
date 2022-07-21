package com.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workflow.entity.Permissions;

public interface PermissionRepo extends JpaRepository<Permissions, Integer> {

}
