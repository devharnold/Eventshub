package com.eventhub.events.controller;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.service.UsersService;
import com.eventhub.events.model.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testUserController {
    @Mock
    private UsersDao usersDao;

    @InjectMocks
    private UsersController usersController;

    @Spy
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = spy(new UsersService(usersDao));
        usersController = new UsersController(usersDao);
    }

    @Test
    void testGetUserByName_Found() {
        Users mockUser = new Users();
        mockUser.setUsername("mockUsername");
        when(usersDao.findByName("mockUsername")).thenReturn(mockUser);

        ResponseEntity<Users> response = usersController.getUserByName("mockUsername");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("mockUsername", response.getBody(). getUsername());
    }

    @Test
    void testGetUsers_AllUsers() {
        List<Users> mockUsers = Arrays.asList(new Users(), new Users());
        when(usersDao.findAll()).thenReturn(mockUsers);

        ResponseEntity<List<Users>> response = usersController.listUsers();

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(2, response.getBody().size());
    }

}
