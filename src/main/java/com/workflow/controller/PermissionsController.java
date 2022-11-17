package com.workflow.controller;

import com.workflow.entity.Permissions;
import com.workflow.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@CrossOrigin("*")
public class PermissionsController {
    @Autowired
    private PermissionService permessionService;


    @GetMapping("/getall")
    public List<Permissions> getAllPermissions() {
        return permessionService.getAllPermissions();
    }
}
