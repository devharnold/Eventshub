package com.eventhub.events.service;

import com.eventhub.events.model.Organizations;
import com.eventhub.events.dao.OrganizationsDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class testOrgService {
    @Mock
    private OrganizationsDao organizationsDao;

    @InjectMocks
    private OrganizationsService organizationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrganization() {
        Organizations mockOrganization = new Organizations();
        mockOrganization.setOrganizationId("1234");
        doNothing().when(organizationsDao).createOrganizationProfile(mockOrganization);

        organizationsService.createOrganization(mockOrganization);

        verify(organizationsDao, times(1)).createOrganizationProfile(mockOrganization);
    }

    @Test
    void testGetOrganization_Found() {
        Organizations mockOrganization = new Organizations();
        mockOrganization.setOrganizationName("mockOrganization");
        when(organizationsDao.findByName("mockOrganization")).thenReturn(mockOrganization);

        Organizations result = organizationsService.findOrganizationByName("mockOrganization");

        assertNotNull(result);
        assertEquals(mockOrganization, result);
        assertEquals("mockOrganization", result.getOrganizationName());
        verify(organizationsDao, times(1)).findByName("mockOrganization");
    }

    @Test
    void testGetAllOrganizations_Found() {
        List<Organizations> mockOrganizations = Arrays.asList(new Organizations(), new Organizations());
        when(organizationsDao.findAll()).thenReturn(mockOrganizations);

        List<Organizations> result = organizationsService.findAllOrganizations();

        assertEquals(mockOrganizations, result);
        assertEquals(2, result.size());
        verify(organizationsDao, times(1)).findAll();
    }
}
