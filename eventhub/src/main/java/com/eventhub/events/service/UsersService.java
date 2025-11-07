package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import org.springframework.stereotype.Service;


@Service
public class UsersService {
    private final UsersDao usersDao;

    public UsersService(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public void createNewUser(Users users) {
        usersDao.createUserProfile(users);
    }

    public List<Users> findAllUsers() {
        return usersDao.findAll();
    }

    public Users findUserByName(String username) {
        return usersDao.findByName(username);
    }
}
