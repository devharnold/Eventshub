package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.model.Organizations;
import com.eventhub.events.dao.OrganizationsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationsService {
    private final OrganizationsDao organizationsDao;

    @Autowired
    public OrganizationsService(OrganizationsDao organizationsDao) {
        this.organizationsDao = organizationsDao;
    }

    public void createOrganization(Organizations organizations) {
        organizationsDao.createOrganizationProfile(organizations);
    }

    public Organizations findOrganizationByName(String organizationName) {
        return organizationsDao.findByName(organizationName);
    }

    public List<Organizations> findAllOrganizations() {
        return organizationsDao.findAll();
    }
}
