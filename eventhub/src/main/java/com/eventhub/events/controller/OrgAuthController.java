package com.eventhub.events.controller;

import com.eventhub.events.dao.OrganizationsDao;
import com.eventhub.events.model.Organizations;
import com.eventhub.events.service.OrgAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/org-auth")
public class OrgAuthController {
    @Autowired
    private OrganizationsDao organizationsDao;

    @Autowired
    private OrgAuthService authService;

    @PostMapping("/signup")
    public Organizations signup(@RequestBody Organizations organizations) {
        return organizationsDao.createOrganizationProfile(organizations);
    }

    @PostMapping("/login")
    public String login(@RequestParam String organizationName, @RequestParam String password) {
        return authService.login(organizationName, password);
    }
}
