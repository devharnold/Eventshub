package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.model.Users;
import com.eventhub.events.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsersService {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void createNewUser(Users users) {
        usersRepository.createUser(users);
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAllUsers();
    }

    public Users findUserByEmail(String email) {
        return usersRepository.findUserByEmail(email);
    }
}
