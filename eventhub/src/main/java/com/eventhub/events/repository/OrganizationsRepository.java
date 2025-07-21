package com.eventhub.events.repository;

import java.sql.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.time.LocalDateTime;

import com.eventhub.events.utils.UniqueIdGenerator;

import javax.sql.DataSource;
import com.eventhub.events.model.Organizations;

import org.springframework.stereotype.Repository;

@Repository

public class OrganizationsRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(OrganizationsRepository.class);

    public OrganizationsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Organizations findByName(String organizationName) {
        String fetch_query = "SELECT * FROM organizations WHERE organizationName = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(fetch_query)) {

            stmt.setString(1, organizationName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error trying to find the organization!:", e);
        }
        return null;
    }

    public Organizations createOrganization(Organizations organizations) {
        String insert_query = "INSERT INTO organizations (organizationName) VALUES (?)";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, organizations.getOrganizationId());
            stmt.setString(2, organizations.getOrganizationName());
            stmt.setString(3, organizations.getEmail1());
            stmt.setString(4, organizations.getEmail2());
            stmt.setString(5, organizations.getContactInfo());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String generatedId = UniqueIdGenerator.generateUniqueId();
                    organizations.setOrganizationId(generatedId);
                    logger.info("Created Organization with ID: {}", organizations.getOrganizationId());
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating organization!", e);
        }
        return organizations;
    }

}
