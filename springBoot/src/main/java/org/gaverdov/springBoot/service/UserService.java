package org.gaverdov.springBoot.service;


import org.gaverdov.springBoot.models.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    List<User> getAllUsers();

    void removeUserById(Long id);

    User getUserById(long id);

    User getUserByEmail(String email);

    void updateUser(User user);
}
