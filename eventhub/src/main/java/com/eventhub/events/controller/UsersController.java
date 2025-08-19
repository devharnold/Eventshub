package com.eventhub.events.controller;

import java.util.List;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersDao usersDao) {
        this.usersService = new UsersService(usersDao);
    }

    @PostMapping("/users/create_profile")
    public ResponseEntity<Users> createUser (@RequestBody Users users) {
        usersService.createNewUser(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/find-user")
    public ResponseEntity<Users> getUserByEmail(@PathVariable String email) {
        Users users = usersService.findUserByEmail(email);
        return (users != null) ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/list-users")
    public ResponseEntity<List<Users>> listUsers() {
        return ResponseEntity.ok(usersService.findAllUsers());
    }
}