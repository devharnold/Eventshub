package com.eventhub.events.dao;

import java.util.List;
import com.eventhub.events.model.Organizations;

public interface OrganizationsDao {
    Organizations createOrganizationProfile(Organizations organization);
    Organizations findByName(String name);
    List<Organizations> findAll();
}