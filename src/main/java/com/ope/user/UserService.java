package com.ope.user;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public Optional<User> findUserById(UUID id){
        return userDao.findUserById(id);
    }

    public User[] findAllUsers(){
        return userDao.getUsers();
    }
}
