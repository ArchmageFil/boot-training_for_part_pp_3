package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoUser;
import io.github.archmagefil.boottraining.model.UnverifiedUser;
import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.util.UserTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public void addUser(UnverifiedUser tempUser) {
        if (isInvalidSyntax(tempUser, true)) {
            return;
        }
        if (dao.isEmailExist(tempUser.getEmail())) {
            util.result("duplicate_email");
            return;
        }
        tempUser.setRoles(roleService.findListByIds(tempUser.getRoles()));
        tempUser.setPassword(bCrypt.encode(tempUser.getPassword()));
        dao.add(tempUser.createUser());
        util.result("user_added");
    }

    @Override
    @Transactional
    public void updateUser(UnverifiedUser tempUser) {
        User user = find(tempUser.getId());
        if (user == null) {
            util.result("no_id_in_db");
            return;
        }
        if (tempUser.getPassword() != null) {
            user.setPassword(bCrypt.encode(tempUser.getPassword()));
        }
        if (isInvalidSyntax(tempUser, false)) {
            return;
        }
        if ((!tempUser.getEmail().equals(user.getEmail())
                && dao.isEmailExist(tempUser.getEmail()))) {
            util.result("duplicate_email");
            return;
        }
        user.setRoles(roleService.findListByIds(tempUser.getRoles()));
        user.setName(tempUser.getName().trim());
        user.setSurname(tempUser.getSurname().trim());
        user.setAge(tempUser.getAge());
        user.setEmail(tempUser.getEmail());
        user.setGoodAcc(tempUser.getGoodAcc());

        dao.update(user);
        util.result("updated");
    }

    @Override
    @Transactional
    public String deleteUser(long id) {
        User user = find(id);
        if (user == null) {
            return util.getWords().getProperty("no_id_in_db");
        }
        dao.deleteById(id);
        return util.getWords().getProperty("deleted");
    }

    @Override
    public User find(long id) {
        return dao.findById(id);
    }

    @Override
    public User findByUsername(String email) {
        return (dao.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException(
                        email + util.getWords().getProperty("no_email_in_db")));
    }

    @Transactional
    @Override
    public String clearDB() {
        return dao.clearDB();
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
