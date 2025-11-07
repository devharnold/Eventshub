package com.eventhub.events.service;

import com.eventhub.events.model.Users;
import com.eventhub.events.dao.UsersDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class testUserService {
    @Mock
    private UsersDao usersDao;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        Users mockUser = new Users();
        mockUser.setUserId("0110");
        doNothing().when(usersDao).createUserProfile(mockUser);

        usersService.createNewUser(mockUser);

        verify(usersDao, times(1))
                .createUserProfile(mockUser);
    }

    @Test
    void testGetUserByName_Found() {
        Users mockUser = new Users();
        mockUser.setUsername("mockUser");
        when(usersDao.findByName("John Doe"))
                .thenReturn(mockUser);

        Users result = usersService.findUserByName("mockUser");

        assertNotNull(result);
        assertEquals(mockUser, result);
        assertEquals("mockUser", result.getUsername());
        verify(usersDao, times(1))
                .findByName("mockUser");
    }

    @Test
    void testGetAllUsers() {
        List<Users> mockUsers = Arrays.asList(new Users(), new Users());
        when(usersDao.findAll()).thenReturn(mockUsers);

        List<Users> result = usersService.findAllUsers();

        assertEquals(mockUsers, result);
        assertEquals(2, result.size());
        verify(usersDao, times(1)).findAll();
    }


}
