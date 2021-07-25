package io.github.archmagefil.boottraining.dao;

import io.github.archmagefil.boottraining.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class DaoRoleJpa implements DaoRole {
    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Role> findByName(String role) {
        TypedQuery<Role> query = em.createQuery("SELECT r from Role r " +
                "WHERE r.roleTitle = :role", Role.class);
        query.setParameter("role", role);
        try {
            return Optional.of(query.getSingleResult());
        } catch (javax.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r " +
                "WHERE r.id = :id", Role.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (javax.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> getAll() {
        return em.createQuery("SELECT r FROM Role r", Role.class)
                .getResultList();
    }
}