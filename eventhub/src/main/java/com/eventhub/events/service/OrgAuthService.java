package com.eventhub.events.service;

import com.eventhub.events.dao.OrganizationsDao;
import com.eventhub.events.model.Organizations;
import com.eventhub.events.utils.OrgJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgAuthService {

    @Autowired
    private OrganizationsDao organizationsDao;

    @Autowired
    private OrgJwtUtil orgJwtUtil;

    public String login(String organizationName, String password) {
        Organizations organization = organizationsDao.findByName(organizationName);
        if (organization != null && organization.getPassword().equals(password)) {
            return orgJwtUtil.generateToken(organization);
        }
        throw new RuntimeException("Invalid Organization Credentials");
    }
}
