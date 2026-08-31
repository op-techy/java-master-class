package com.ope.user;

import java.util.UUID;

public class UserDao {
    private static final User[] users;

    static {
        users = new User[]{
                new User(UUID.fromString("2f1f54d7-08d8-48e5-b9d7-e1f445f3cf19"),"Ope"),
                new User(UUID.fromString("34e09ef5-7735-4712-b55f-7c57e04e1ff0"),"Yusuf"),
                new User(UUID.fromString("cc86e6d9-e360-415e-ace1-97b4b823bbf7"),"Nelson"),
                new User(UUID.fromString("2e8c2617-0796-4ad9-9563-284b26192a59"),"Admin"),
                new User(UUID.fromString("86fe7f91-a99c-4718-9617-bd0be0d9c52b"),"Lamine"),
                new User(UUID.fromString("425a25b2-9bf8-4094-8fc0-472445e007b4"),"Tyrese"),
                new User(UUID.fromString("fb9397be-3717-4f85-bca7-26d827550df3"),"Sam"),
                new User(UUID.fromString("aee107b6-1da8-4274-8972-b1af9f82ca18"),"Jon"),
                new User(UUID.fromString("59ea6d1d-441f-4ef0-ad72-d8b590ab8fb3"),"Subi")
        };
    }

    public User[] getAllUsers() {
        return users;
    }

}
