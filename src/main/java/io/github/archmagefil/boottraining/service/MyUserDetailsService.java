package io.github.archmagefil.boottraining.service;

import io.github.archmagefil.boottraining.dao.DaoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final DaoUser dao;

    @Autowired
    public MyUserDetailsService(DaoUser dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return dao.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("Пользователь с логином %s не найден в БД", username)));
    }
}