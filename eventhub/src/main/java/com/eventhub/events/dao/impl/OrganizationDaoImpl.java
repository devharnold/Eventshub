package com.eventhub.events.dao.impl;


import java.sql.*;
import java.util.*;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.eventhub.events.model.Organizations;
import com.eventhub.events.dao.OrganizationsDao;
import com.eventhub.events.utils.UniqueIdGenerator;
import com.eventhub.events.utils.PasswordHash;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizationDaoImpl implements OrganizationsDao {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(OrganizationDaoImpl.class);

    public OrganizationDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Organizations createOrganizationProfile(Organizations organizations) {
        String insert_query = "INSERT INTO organizations (organizationId, organizationName, email1, email2, contactInfo)" + "VALUES (?, ?, ?, ?, ?)";
        String generatedId = UniqueIdGenerator.generateUniqueId();
        organizations.setOrganizationId(generatedId);

        String plainPassword = organizations.getPassword();
        String hashedPassword = PasswordHash.hashPassword(plainPassword);
        organizations.setPassword(hashedPassword);

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, organizations.getOrganizationId());
            stmt.setString(2, organizations.getOrganizationName());
            stmt.setString(3, organizations.getEmail1());
            stmt.setString(4, organizations.getEmail2());
            stmt.setString(5, organizations.getContactInfo());
            stmt.setString(6, organizations.getPassword());

            int rowsInserted = stmt.executeUpdate(insert_query);
            if  (rowsInserted > 0) {
                logger.info("Created organization profile!");
            } else {
                logger.warn("Failed to create organization profile!");
            }
        } catch (SQLException e) {
            logger.error("Error while trying to create organization profile!", e);
        }
        return organizations;
    }

    @Override
    public Organizations findByName(String organizationName) {
        String query = "SELECT * FROM organizations WHERE organizationName = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, organizationName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while trying to find organization", e);
        }
        return null;
    }

    @Override
    public List<Organizations> findAll() {
        List<Organizations> organizations = new ArrayList<>();
        String fetch_query = "SELECT * FROM organizations where city = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(fetch_query)) {

           try (ResultSet rs = stmt.executeQuery(fetch_query)) {
               while(rs.next()) {
                   organizations.add(mapRow(rs));
               }
           }
        } catch (SQLException e) {
            logger.error("Error while trying to find organizations", e);
        }
        return organizations;
    }

    private Organizations mapRow(ResultSet rs) throws SQLException {
        return new Organizations();
    }

}