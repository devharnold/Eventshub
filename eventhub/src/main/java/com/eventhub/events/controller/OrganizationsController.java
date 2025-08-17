package com.eventhub.events.controller;

import com.eventhub.events.model.Organizations;
import com.eventhub.events.service.OrganizationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eventhub.events.dao.OrganizationsDao;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationsController {
    private final OrganizationsService organizationsService;

    @Autowired
    public OrganizationsController(OrganizationsDao organizationsDao) {
        this.organizationsService = new OrganizationsService(organizationsDao);
    }

    @PostMapping("/organizations/new-organization")
    public ResponseEntity<Organizations> createOrganization (@RequestBody Organizations organizations) {
        organizationsService.createOrganization(organizations);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/organization/find-organization")
    public ResponseEntity<Organizations> getOrganizationByName(@PathVariable String organizationName) {
        Organizations organizations = organizationsService.findOrganizationByName(organizationName);
        return (organizations != null) ? ResponseEntity.ok(organizations) : ResponseEntity.notFound().build();
    }
}
