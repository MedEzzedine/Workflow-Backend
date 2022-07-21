

package com.workflow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.entity.Groupe;
import com.workflow.entity.Permissions;
import com.workflow.entity.demande;
import com.workflow.service.GroupeService;
import com.workflow.service.PermissionService;

@RestController
@RequestMapping("/groupe")
@CrossOrigin("*")
public class groupecontroller {

@Autowired
	private GroupeService gs;



@GetMapping("/getall")
@ResponseBody
public List<Groupe>  getAll_Permissions ()
{
return gs.Allgroupe();

}
}
