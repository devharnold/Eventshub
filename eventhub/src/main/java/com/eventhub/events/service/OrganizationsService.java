package com.eventhub.events.service;

import java.util.List;

import com.eventhub.events.model.Organizations;
import com.eventhub.events.repository.OrganizationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationsService {
    private final OrganizationsRepository organizationsRepository;

    @Autowired
    public OrganizationsService(OrganizationsRepository organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    public void createOrganization(Organizations organizations) {
        organizationsRepository.createOrganization(organizations);
    }

    public Organizations findOrganizationByName(String organizationName) {
        return organizationsRepository.findByName(organizationName);
    }
}
