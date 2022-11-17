package com.workflow.service;

import com.workflow.entity.Permissions;
import com.workflow.repository.PermissionsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionsRepo permissionsRepo;


    public List<Permissions> getAllPermissions() {
        return ((List<Permissions>) permissionsRepo.findAll()).stream()
                .filter(permission -> !permission.getNom().equals("tritux") && !permission.getNom().equals("drh"))
                .collect(Collectors.toList());
    }
}
