package com.workflow.controller;

import com.workflow.entity.Groupe;
import com.workflow.service.GroupeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groupe")
@CrossOrigin("*")
public class GroupeController {

    @Autowired
    private GroupeService groupeService;


    @GetMapping("/getall")
    public List<Groupe> getAllPermissions() {
        return groupeService.Allgroupe();

    }
}
