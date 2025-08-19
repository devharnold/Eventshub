package com.eventhub.events.dao;


import com.eventhub.events.model.Users;
import java.util.List;

public interface UsersDao {
    Users createUserProfile(Users users);
    Users findByName(String name);
    List<Users> findAll();
    //Users login(String userId);
}
