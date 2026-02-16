package com.example.Blogging_platform2.service;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.dao.UserDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @CachePut(value = "users", key = "#result.id")
    public User registerUser(User user) {
        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#id")
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "'all'")
    public List<User> findAll() {
        return userDao.findAll();
    }

    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userDao.deleteById(id);
    }
}
