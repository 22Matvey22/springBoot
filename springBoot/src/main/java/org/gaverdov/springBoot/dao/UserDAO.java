package org.gaverdov.springBoot.dao;


import org.gaverdov.springBoot.models.User;

import java.util.List;

public interface UserDAO {
    void addUser(User user);

    List<User> getAllUsers();

    public void removeUserById(Long id);

    User getUserById(long id);

    User getUserByEmail(String email);

    void updateUser(User user);
}
