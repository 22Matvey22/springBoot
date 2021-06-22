package org.gaverdov.springBoot.controller;

import org.gaverdov.springBoot.models.User;
import org.gaverdov.springBoot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GreetingController {

    private final UserService userService;

    @Autowired
    public GreetingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("welcomeUser", userList);
        return "redirect:/user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
