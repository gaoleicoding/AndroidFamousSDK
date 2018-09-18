package com.gaolei.dagger_two;

public class UserRepository  {

    public UserRepository() {
    }

    public User getUser() {
        User user = new User();
        user.name = "yxm";
        return user;
    }
}