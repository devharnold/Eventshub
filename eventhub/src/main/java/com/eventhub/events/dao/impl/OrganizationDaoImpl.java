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
    public Organizations createOrganizationProfile(Organizations organization) {
        String insert_query = "INSERT INTO organizations (organizationId, organizationName, email1, email2, contactInfo)" + "VALUES (?, ?, ?, ?, ?)";
        String generatedId = UniqueIdGenerator.generateUniqueId();
        organization.setOrganizationId(generatedId);

        String plainPassword = organization.getPassword();
        String hashedPassword = PasswordHash.hashPassword(plainPassword);
        organization.setPassword(hashedPassword);

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, organization.getOrganizationId());
            stmt.setString(2, organization.getOrganizationName());
            stmt.setString(3, organization.getEmail1());
            stmt.setString(4, organization.getEmail2());
            stmt.setString(5, organization.getContactInfo());
            stmt.setString(6, organization.getPassword());

            int rowsInserted = stmt.executeUpdate(insert_query);
            if  (rowsInserted > 0) {
                logger.info("Created organization profile!");
            } else {
                logger.warn("Failed to create organization profile!");
            }
        } catch (SQLException e) {
            logger.error("Error while trying to create organization profile!", e);
        }
        return organization;
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
        Organizations organizations = new Organizations();
        organizations.setOrganizationId(rs.getString("organizationId"));
        organizations.setOrganizationName(rs.getString("organizationName"));
        organizations.setContactInfo(rs.getString("contactInfo"));
        organizations.setEmail1(rs.getString("email1"));
        organizations.setEmail2(rs.getString("email2"));
        return organizations;
    }

}