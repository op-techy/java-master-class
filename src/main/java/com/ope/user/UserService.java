package com.ope.user;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public Optional<User> findUserById(UUID id){
        User[] users = userDao.getAllUsers();

        for (User user : users){
            if(id.equals(user.getId())) return Optional.of(user);
        }

        return Optional.empty();
    }

    public User[] findAllUsers(){
        return userDao.getAllUsers();
    }
}
