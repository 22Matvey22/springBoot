package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.models.Role;
import org.gaverdov.springBoot.models.User;
import org.gaverdov.springBoot.repositories.RoleRepository;
import org.gaverdov.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/admin")
    public String showUsers(Model model, Authentication aut) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("userAuthentication", userService.getUserByEmail(aut.getName()));
        model.addAttribute("users", userList);
        return "show";
    }

    @PostMapping("/admin/save")
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

    @DeleteMapping("/admin/delete")
    public String delete(Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit")
    @ResponseBody
    public User edit(Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/admin/update")
    public String update(@ModelAttribute("user") User user,
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
