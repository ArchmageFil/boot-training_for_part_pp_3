package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoRole;
import io.github.archmagefil.boottraining.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final DaoRole daoRole;

    @Autowired
    public RoleService(DaoRole daoRole) {
        this.daoRole = daoRole;
    }

    public List<Role> getAllRoles() {
        return daoRole.getAll();
    }

    public Optional<Role> findByName(String role) {
        return daoRole.findByName(role);
    }
}