package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoUser;
import io.github.archmagefil.boottraining.model.User;
import io.github.archmagefil.boottraining.model.UserDto;
import io.github.archmagefil.boottraining.util.UserTableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    public String addUser(UserDto tempUser) {
        if (util.isInvalidUser(tempUser)) {
            return util.getMessage();
        }
        if (dao.findByEmail(tempUser.getEmail()).isPresent()) {
            return util.getWords().getProperty("duplicate_email");
        }
        // Находим соответствие ролей в БД и добавляем.
        tempUser.setRoles(tempUser.getRoles().stream()
                .map(r -> roleService.findById(r.getId()))
                .map(r -> r.orElseThrow(() -> new IllegalArgumentException(
                        util.getWords().getProperty("wrong_role" + r))))
                .collect(Collectors.toList()));
        // Шифруем пароль нового юзера.
        tempUser.setPassword(bCrypt.encode(tempUser.getPassword()));
        // Проверки закончены, можно сохранять.
        User user = tempUser.createUser();
        dao.add(user);
        return String.format(util.getWords().getProperty("user_added"),
                user.getName(), user.getSurname());
    }

    @Override
    @Transactional
    public String updateUser(UserDto tempUser) {
        // Проверяем синтаксис мыла
        tempUser.setEmail(tempUser.getEmail().trim());
        if (util.isInvalidEmail(tempUser.getEmail())) {
            return util.getWords().getProperty("wrong_email");
        }
        //Не успели ли удалить пользователя.
        User user = find(tempUser.getId());
        if (null == user) {
            return util.getWords().getProperty("no_id_in_db");
        }
        // Проверяем почту на дублирование, с Optional
        if (dao.findByEmail(tempUser.getEmail())
                .filter(x -> !(x.getId().equals(user.getId())))
                .isPresent()) {
            return util.getWords().getProperty("duplicate_email");
        }
        // Находим соответствие ролей в БД и добавляем.
        user.setRoles(tempUser.getRoles().stream()
                .map(r -> roleService.findById(r.getId()))
                .map(r -> r.orElseThrow(() -> new IllegalArgumentException(
                        util.getWords().getProperty("wrong_role" + r))))
                .collect(Collectors.toList()));
        // Обновляем
        user.setName(tempUser.getName().trim());
        user.setSurname(tempUser.getSurname().trim());
        user.setAge(tempUser.getAge());
        user.setEmail(tempUser.getEmail());
        // Смена пароля в форме редактирования не обязательна.
        if (tempUser.getPassword() != null) {
            user.setPassword(bCrypt.encode(tempUser.getPassword()));
        }
        user.setGoodAcc(tempUser.getGoodAcc());
        dao.update(user);
        return util.getWords().getProperty("updated");
    }

    @Override
    @Transactional
    public String deleteUser(long id) {
        User user = find(id);
        if (user == null) {
            return util.getWords().getProperty("no_id_in_db");
        }
        user.setRoles(null);
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
