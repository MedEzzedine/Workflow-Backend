package com.workflow.util;

import com.workflow.entity.Permissions;
import com.workflow.entity.Role;
import com.workflow.entity.User;
import com.workflow.repository.PermissionsRepo;
import com.workflow.repository.RoleRepo;
import com.workflow.repository.UserRepository;
import com.workflow.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class TrituxAccountCreation implements CommandLineRunner {

    @Value("${tritux-email}")
    private String trituxEmail;

    @Value("${tritux-password}")
    private String trituxPassword;


    @Autowired
    private CustomUserDetailsService userService;


    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PermissionsRepo permissionsRepo;

    @Override
    public void run(String... args) throws Exception {

        if(permissionsRepo.count() <= 1)
            userService.populateDatabase();

            if(userService.userExists(trituxEmail))
                return;
            Permissions trituxPermission = new Permissions("tritux");
            Permissions savedPermission = permissionsRepo.save(trituxPermission);
            HashSet<Permissions> trituxPermissions = new HashSet<Permissions>();
            trituxPermissions.add(savedPermission);

            Role trituxRole = new Role("TrituxGroup", 2);
            trituxRole.setPermissions(trituxPermissions);
            Role savedRole = roleRepo.save(trituxRole);
            User trituxAdmin = new User();
            trituxAdmin.setEmail(trituxEmail);
            trituxAdmin.setNom("Tritux");
            trituxAdmin.setPrenom("Group");
            trituxAdmin.setPassword(trituxPassword);
            trituxAdmin.setFirstaccount(false);
            trituxAdmin.setFirstlogin(false);
            HashSet<Role> trituxRoles = new HashSet<Role>();
            trituxRoles.add(savedRole);
            trituxAdmin.setRoles(trituxRoles);

            userService.createTrituxAccount(trituxAdmin);
        }


}

//package com.workflow.service;
//
//import com.workflow.entity.Role;
//import com.workflow.entity.User;
//import com.workflow.repository.RoleRepo;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//
//@AllArgsConstructor
//@Component
//public class InitializeDataService implements CommandLineRunner {
//
//    @Autowired
//    private CustomUserDetailsService userService;
//
//    @Autowired
//    private RoleRepo roleRepo;
//    @Autowired
//    private RoleService roleService;
//
//    @Override
//    public void run(String... args) throws Exception {
//        Role moudir = roleRepo.save(new Role("Moudir", 2));
//        Role djava = roleRepo.save(new Role("DJava", 1, moudir));
//        Role dev = roleRepo.save(new Role("Dev", 0, djava));
//        userService.addUserViaRegistration(new User("A", "1111", "test@gmail.com"), moudir.getId());
//        userService.addUserViaRegistration(new User("B", "2222", "testtest@gmail.com"), djava.getId());
//        userService.addUserViaRegistration(new User("C", "3333", "gladjua77@gmail.com"), dev.getId());
//    }
//}
