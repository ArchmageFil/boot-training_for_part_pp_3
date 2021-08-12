package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoUser;
import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.util.UserTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.archmagefil.boottraining.util.UserTableUtil.transformUserDto;

@Service
public class UserServiceImpl implements UserService {
    private DaoUser dao;
    private RoleService roleService;
    private UserTableUtil util;
    private PasswordEncoder bCrypt;

    @Override
    public List<User> getAllUsers() {
        return dao.getAll();
    }

    @Override
    @Transactional
    public Long addUser(UnverifiedUser tempUser) {
        if (isInvalidSyntax(tempUser, true)) {
            return null;
        }
        if (dao.isEmailExist(tempUser.getEmail())) {
            throw new IllegalArgumentException("duplicate_email");
        }
        tempUser.setRoles(roleService.findListByIds(tempUser.getRoles()));
        tempUser.setPassword(bCrypt.encode(tempUser.getPassword()));
        return dao.add(tempUser.createUser());
    }

    @Override
    @Transactional
    public Long updateUser(UnverifiedUser tempUser) {
        User user = findById(tempUser.getId());
        if (user == null) {
            throw new IllegalArgumentException("no_id_in_db");
        }
        if (tempUser.getPassword() != null) {
            user.setPassword(bCrypt.encode(tempUser.getPassword()));
        }
        if (isInvalidSyntax(tempUser, false)) {
            throw new IllegalArgumentException("wrong_syntax");
        }
        if ((!tempUser.getEmail().equals(user.getEmail())
                && dao.isEmailExist(tempUser.getEmail()))) {
            throw new IllegalArgumentException("duplicate_email");
        }
        user.setRoles(roleService.findListByIds(tempUser.getRoles()));
        user.setName(tempUser.getName().trim());
        user.setSurname(tempUser.getSurname().trim());
        user.setAge(tempUser.getAge());
        user.setEmail(tempUser.getEmail());
        user.setGoodAcc(tempUser.getGoodAcc());

        dao.update(user);
        return user.getId();
    }

    @Override
    @Transactional
    public boolean deleteUser(long id) {
        if (findById(id) == null) {
            return false;
        }
        dao.deleteById(id);
        return true;
    }

    @Override
    public User findById(long id) {
        return dao.findById(id);
    }

    @Override
    public List<UserDto> getDtoUserList() {
        return getAllUsers().stream()
                .map(transformUserDto())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto getDtoUser(long id) {
        return transformUserDto().apply(findById(id));
    }

    private boolean isInvalidSyntax(UnverifiedUser user, boolean isNew) {
        if (isNew) {
            return util.isInvalidUser(user);
        }
        user.setPassword("42");
        return util.isInvalidUser(user);
    }

    @Autowired
    public void setDao(DaoUser dao) {
        this.dao = dao;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @Autowired
    public void setUtil(UserTableUtil util) {
        this.util = util;
    }

    @Autowired
    public void setBCrypt(PasswordEncoder bCrypt) {
        this.bCrypt = bCrypt;
    }
}
