package io.github.archmagefil.boottraining.dao;

import io.github.archmagefil.boottraining.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class DaoUserJpa implements DaoUser {
    @PersistenceContext
    private EntityManager em;

    @Override
    public long add(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public void update(User user) {
        em.merge(user);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.getReference(User.class, id));
    }

    @Override
    public List<User> getAll() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.id",
                User.class).getResultList();
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.roles " +
                        "WHERE u.email = :email", User.class);
        query.setParameter("email", email);

        try {
            return Optional.of(query.getSingleResult());
        } catch (javax.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        Query query = em.createNativeQuery(
                "SELECT COUNT(email) FROM users WHERE email LIKE :email LIMIT 1");
        query.setParameter("email", email);
        return 0 != Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public String clearDB() {
        int i = em.createQuery("DELETE FROM User").executeUpdate();
        return "Завершено" + i;
    }
}