package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.models.Role;
import org.gaverdov.springBoot.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users/roles")
public class RoleRestController {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleRestController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Role>> showUsers() {
        List<Role> roleList = roleRepository.findAll();

        return roleList != null && !roleList.isEmpty()
                ? new ResponseEntity<>(roleList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
