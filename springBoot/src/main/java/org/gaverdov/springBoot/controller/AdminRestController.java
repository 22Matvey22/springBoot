package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.models.Role;
import org.gaverdov.springBoot.models.User;
import org.gaverdov.springBoot.repositories.RoleRepository;
import org.gaverdov.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/users")
public class AdminRestController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminRestController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> showUsers() {
        List<User> userList = userService.getAllUsers();

        return userList != null && !userList.isEmpty()
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user,
                                         @RequestParam(value = "rolesId") List<String> roles) {
        Long idRole = Long.parseLong(roles.get(0));
        HttpHeaders httpHeaders = new HttpHeaders();

        if (idRole != 1) {
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleRepository.getById(1L)); //добавляю ROLE_USER
            rolesUser.add(roleRepository.getById(idRole)); //добавляю роль, которую выберет пользователь
            user.setRoles(rolesUser);
        } else {
            user.setRoles(Collections.singleton(roleRepository.getById(1L))); //добавляю только ROLE_USER
        }
        userService.addUser(user);
        return user != null ?
                new ResponseEntity<>(user, httpHeaders, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.removeUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.getUserById(id);
        return user != null ?
                new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("")
    public ResponseEntity<User> update(@RequestBody User user,
                                       @RequestParam(value = "rolesId") List<String> roles) {
        Long idRole = Long.parseLong(roles.get(0));
        HttpHeaders httpHeaders = new HttpHeaders();

        if (idRole != 1) {
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleRepository.getById(1L)); //добавляю ROLE_USER
            rolesUser.add(roleRepository.getById(idRole)); //добавляю роль, которую выберет пользователь
            user.setRoles(rolesUser);
        } else {
            user.setRoles(Collections.singleton(roleRepository.getById(1L))); //добавляю только ROLE_USER
        }
        userService.updateUser(user);
        return user != null ?
                new ResponseEntity<>(user, httpHeaders, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
