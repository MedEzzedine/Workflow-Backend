package com.workflow.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workflow.entity.Groupe;
import com.workflow.entity.Permissions;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.entity.demande;
import com.workflow.repository.GroupeRepo;
import com.workflow.repository.RoleRepo;
import com.workflow.repository.UserRepository;
import com.workflow.repository.permissionsRepo;
import com.workflow.util.JwtUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepo RoleRepo;
    @Autowired
    private GroupeRepo GroupeRepo;
    @Autowired
    private permissionsRepo permissionsRepo;
    
  
    
    @Bean
    public PasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) repository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
    public User getFulluser(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }
    
	public ResponseEntity<?> adduser(User u) {
		Set<Role> roles = new HashSet<Role>();
		int roleid=u.getRoles().stream().findFirst().get().getId();
		Role role =RoleRepo.findById(roleid).orElse(null);
		roles.add(role);
		u.setRoles(roles);
		u.setPassword(passwordEncoder().encode(u.getPassword()));
		User user =repository.save(u);
		return ResponseEntity.ok(user) ;	
	}
	
	public ResponseEntity<?>  update(User u) {
		User user=repository.findById(u.getId()).orElse(null);
		user.setAdresse(u.getAdresse());
		user.setDatenaissance(u.getDatenaissance());
		user.setEmail(u.getEmail());
		user.setNom(u.getNom()); 
		user.setPrenom(u.getPrenom());
		user.setPassword(passwordEncoder().encode(u.getPassword()));
		user.setFirstlogin(u.isFirstlogin());
		System.out.println(u.isFirstlogin());
		User U =repository.save(user);
		return ResponseEntity.ok(U) ;	
	}
 

	public User firstuser(User u) {
		
		Set<Role> roles =new HashSet<Role>();
		Set <Permissions> permissons = new HashSet<Permissions>();
		permissons.add(new Permissions("add_role"));
		//permissons.add(new Permissions("upd_role"));
		//permissons.add(new Permissions("add_perm"));
		permissons.add(new Permissions("add_user"));
		permissons.add(new Permissions("list_user"));
		permissons.add(new Permissions("A/R_conge"));
		permissons.add(new Permissions("drh"));
		Groupe g = new Groupe("RH");
	    Role role =new Role("DRH",2);
	    role.setPermissions(permissons);
	    role.setGroupe(g);
	    roles.add(role);
		u.setRoles(roles);
		u.setPassword(passwordEncoder().encode(u.getPassword()));
		permissionsRepo.save(new Permissions("add_conge"));
	    return repository.save(u);
} 
	
	 public User trituxaccount(User u) { 
		 u.setPassword(passwordEncoder().encode(u.getPassword()));
		return repository.save(u);
	}
	 
	 
	 public Page<User> getalluser(Pageable pageable,String search){
		 if(search.equals("")) 
		 return repository.findAll(pageable);
		 List<User> users= repository.findAll().stream()
			      .filter(user -> user.getNom().contains(search) || user.getPrenom().contains(search))
			      .collect(Collectors.toList());
		 int start = (int) pageable.getOffset();
		 int end = (int) ((start + pageable.getPageSize()) > users.size() ? users.size()
				   : (start + pageable.getPageSize()));
		 Page<User> alluserpage = new PageImpl<>(users.subList(start, end),pageable,users.size());
		 return alluserpage;
	 }
}