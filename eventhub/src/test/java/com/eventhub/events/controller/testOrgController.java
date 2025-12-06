package com.eventhub.events.controller;

import com.eventhub.events.dao.OrganizationsDao;
import com.eventhub.events.model.Organizations;
import com.eventhub.events.service.OrganizationsService;
import com.eventhub.events.model.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testOrgController {
    @Mock
    private OrganizationsDao organizationsDao;

    @InjectMocks
    private OrganizationsController organizationsController;

    @Spy
    private OrganizationsService organizationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organizationsService = spy(new OrganizationsService(organizationsDao));
        organizationsController = new OrganizationsController(organizationsDao);
    }

    @Test
    void testGetOrganizationById_Found() {
        // Arrange
        Organizations mockOrganization = new Organizations();
        mockOrganization.setOrganizationName("Test Org");
        when(organizationsDao.findByName("Test Org")).thenReturn(mockOrganization);

        // Act
        ResponseEntity<Organizations> response = organizationsController.getOrganizationByName("Test Org");

        // Assert
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Test Org", response.getBody().getOrganizationName());
    }
}
