package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoRole;
import io.github.archmagefil.boottraining.model.Role;
import io.github.archmagefil.boottraining.model.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final DaoRole daoRole;

    @Autowired
    public RoleServiceImpl(DaoRole daoRole) {
        this.daoRole = daoRole;
    }

    public List<RoleDto> getAllRoles() {
        return RoleDto.listOf(daoRole.getAll());
    }

    public Optional<Role> findById(Long id) {
        return daoRole.findById(id);
    }

    @Override
    public List<Role> findListByIds(List<Role> ids) {
        ids.forEach(role -> role = findById(role.getId()).orElse(null));
        return ids;
    }
}