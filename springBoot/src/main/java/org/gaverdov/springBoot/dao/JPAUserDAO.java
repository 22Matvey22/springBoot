package org.gaverdov.springBoot.dao;

import org.gaverdov.springBoot.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JPAUserDAO implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void removeUserById(Long id) {
        entityManager.createQuery("delete from User u where u.id = : id")
                .setParameter("id", id).executeUpdate();
    }

    @Override
    public User getUserById(long id) {
        TypedQuery<User> userTypedQuery = entityManager.createQuery("select  u from User u where u.id = : id", User.class);
        userTypedQuery.setParameter("id", id);
        return userTypedQuery.getResultList().stream().findAny().orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        TypedQuery<User> userTypedQuery = entityManager.createQuery("select  u from User u where u.email = : email", User.class);
        userTypedQuery.setParameter("email", email);
        return userTypedQuery.getResultList().stream().findAny().orElse(null);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }
}
