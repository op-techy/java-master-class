package com.ope.user;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> findUserById(UUID id){
        return userDao.findUserById(id);
    }

    public User[] findAllUsers(){
        return userDao.getUsers();
    }
}
