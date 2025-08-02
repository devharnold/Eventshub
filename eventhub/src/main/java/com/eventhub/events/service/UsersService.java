package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.dao.impl.UsersDaoImpl;
import org.springframework.stereotype.Service;


@Service
public class UsersService {
    //private final UsersRepository usersRepository;
    private final UsersDao usersDao;

    public UsersService() {
        this.usersDao = new UsersDaoImpl();
    }

    public void createNewUser(Users users) {
        usersDao.createUserProfile(users);
    }

    public List<Users> findAllUsers() {
        return usersDao.findAll();
    }

    public Users findUserByEmail(String username) {
        return usersDao.findByName(username);
    }
}
