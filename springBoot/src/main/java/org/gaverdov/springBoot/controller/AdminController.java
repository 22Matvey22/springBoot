package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.models.Role;
import org.gaverdov.springBoot.models.User;
import org.gaverdov.springBoot.repositories.RoleRepository;
import org.gaverdov.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("welcomeUser", userList);
        return "index";
    }


    @GetMapping("/admin")
    public String showUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("users", userList);
        return "show";
    }

    @GetMapping(value = "/admin/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesId") List<String> roles) {
        Long idRole = Long.parseLong(roles.get(0));
        if (idRole != 1) {
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleRepository.getById(1L)); //добавляю ROLE_USER
            rolesUser.add(roleRepository.getById(idRole)); //добавляю роль, которую выберет пользователь
            user.setRoles(rolesUser);
        } else {
            user.setRoles(Collections.singleton(roleRepository.getById(1L))); //добавляю только ROLE_USER
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") Long id,
                         @RequestParam(value = "rolesId") List<String> roles) {
        Long idRole = Long.parseLong(roles.get(0));
        if (idRole != 1) {
            Set<Role> rolesUser = new HashSet<>();
            rolesUser.add(roleRepository.getById(1L)); //добавляю ROLE_USER
            rolesUser.add(roleRepository.getById(idRole)); //добавляю роль, которую выберет пользователь
            user.setRoles(rolesUser);
        } else {
            user.setRoles(Collections.singleton(roleRepository.getById(1L))); //добавляю только ROLE_USER
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }
}
