package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showProfile(Model model, Authentication aut) {
        model.addAttribute("user", userService.getUserByEmail(aut.getName()));
        return "user";
    }

}
