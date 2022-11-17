package com.workflow.service;

import com.workflow.entity.Groupe;
import com.workflow.entity.Permissions;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.repository.GroupeRepo;
import com.workflow.repository.RoleRepo;
import com.workflow.repository.UserRepository;
import com.workflow.repository.PermissionsRepo;
import lombok.AllArgsConstructor;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private GroupeRepo groupeRepo;
    @Autowired
    private PermissionsRepo permissionsRepo;

    @Autowired
    private LoginEmailService loginEmailService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = (User) userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public User getFulluser(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public ResponseEntity<?> addUser(User u) {
        String rawPassword = u.getPassword();
        Set<Role> roles = new HashSet<Role>();
        int roleid = u.getRoles().stream().findFirst().get().getId();
        Role role = roleRepo.findById(roleid).orElse(null);
        roles.add(role);
        u.setRoles(roles);
        u.setPassword(passwordEncoder().encode(u.getPassword()));
        User user = userRepository.save(u);
        if(user.isFirstlogin()) {
            System.out.println("Sending login details to " + user.getEmail());
            loginEmailService.sendLoginMail(user, rawPassword);
        }
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> updateExistingUser(User u) {
        User user = userRepository.findById(u.getId()).orElse(null);

        if(user!=null) {
            user.setAdresse(u.getAdresse());
            user.setDatenaissance(u.getDatenaissance());
            user.setEmail(u.getEmail());
            user.setNom(u.getNom());
            user.setPrenom(u.getPrenom());
            user.setPassword(passwordEncoder().encode(u.getPassword()));
            user.setFirstlogin(u.isFirstlogin());
            System.out.println(u.isFirstlogin());
            User newSavedUser = userRepository.save(user);
            return ResponseEntity.ok(newSavedUser);
        }
        return  ResponseEntity.status(500).body("User with id " + u.getId() + " doesn't exist");
    }


//    public User firstUser(User u) {
//
//        Set<Role> roles = new HashSet<Role>();
//        Set<Permissions> permissons = new HashSet<Permissions>();
//        permissons.add(new Permissions("add_role"));
//        //permissons.add(new Permissions("upd_role"));
//        //permissons.add(new Permissions("add_perm"));
//        permissons.add(new Permissions("add_user"));
//        permissons.add(new Permissions("list_user"));
//        permissons.add(new Permissions("A/R_conge"));
//        permissons.add(new Permissions("drh"));
//        Groupe g = new Groupe("Ressources Humaines");
//        Role role = new Role("Directeur RH", 2);
//        role.setPermissions(permissons);
//        role.setGroupe(g);
//        roles.add(role);
//        u.setRoles(roles);
//        u.setPassword(passwordEncoder().encode(u.getPassword()));
//        permissionsRepo.save(new Permissions("add_conge"));
//        return repository.save(u);
//    }

    public void populateDatabase() {

        Set<Role> roles = new HashSet<Role>();
        Set<Permissions> permissons = new HashSet<Permissions>();

        Permissions p1 = new Permissions("add_role");
        p1 = permissionsRepo.save(p1);
        //permissons.add(new Permissions("upd_role"));
        //permissons.add(new Permissions("add_perm"));

        Permissions p2 = new Permissions("add_user");
        p2 = permissionsRepo.save(p2);

        Permissions p3 = new Permissions("list_user");
        p3 = permissionsRepo.save(p3);

        Permissions p4 = new Permissions("A/R_conge");
        p4 = permissionsRepo.save(p4);

        Permissions p5 = new Permissions("drh");
        p5 = permissionsRepo.save(p5);

        permissons.add(p1);
        permissons.add(p2);
        permissons.add(p3);
        permissons.add(p4);
        permissons.add(p5);


        Groupe g = new Groupe("Ressources Humaines");
        g = groupeRepo.save(g);

        Role role = new Role("Directeur RH", 2);
        role.setPermissions(permissons);
        role.setGroupe(g);

        roleRepo.save(role);
        permissionsRepo.save(new Permissions("add_conge"));
    }

    public User createTrituxAccount(User u) {
        u.setPassword(passwordEncoder().encode(u.getPassword()));
        return userRepository.save(u);
    }


    //paginated search query for users
    public Page<User> getAllUsers(Pageable pageable, String search) {
        if (search.equals(""))
            return userRepository.findAll(pageable);
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getNom().contains(search) || user.getPrenom().contains(search))
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = (int) (Math.min((start + pageable.getPageSize()), users.size()));
        return new PageImpl<>(users.subList(start, end), pageable, users.size());
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}