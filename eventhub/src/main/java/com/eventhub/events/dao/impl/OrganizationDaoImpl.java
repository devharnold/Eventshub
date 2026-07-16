package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.OrganizationsDao;
import com.eventhub.events.model.Organizations;
import com.eventhub.events.utils.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrganizationDaoImpl implements OrganizationsDao {

    private static final Logger logger =
            LoggerFactory.getLogger(OrganizationDaoImpl.class);

    private final DataSource dataSource;

    public OrganizationDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Organizations createOrganizationProfile(Organizations organization) {

        String sql = """
                INSERT INTO organizations
                (
                    organization_name,
                    email1,
                    email2,
                    contact_info,
                    password,
                    created_at
                )
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        organization.setPassword(
                PasswordHash.hashPassword(organization.getPassword())
        );

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organization.getOrganizationName());
            stmt.setString(2, organization.getEmail1());
            stmt.setString(3, organization.getEmail2());
            stmt.setString(4, organization.getContactInfo());
            stmt.setString(5, organization.getPassword());
            stmt.setTimestamp(6, Timestamp.valueOf(organization.getCreatedAt()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    organization.setOrganizationId(rs.getInt("id"));
                }
            }

            logger.info("Created organization {}", organization.getOrganizationName());

        } catch (SQLException e) {
            logger.error("Error creating organization", e);
        }

        return organization;
    }

    @Override
    public Organizations findByName(String organizationName) {

        String sql =
                "SELECT * FROM organizations WHERE organization_name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, organizationName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding organization {}", organizationName, e);
        }

        return null;
    }

    @Override
    public List<Organizations> findAll() {

        List<Organizations> organizations = new ArrayList<>();

        String sql =
                "SELECT * FROM organizations ORDER BY organization_name";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                organizations.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Error retrieving organizations", e);
        }

        return organizations;
    }

    private Organizations mapRow(ResultSet rs) throws SQLException {

        Organizations organization = new Organizations();

        organization.setOrganizationId(rs.getInt("id"));
        organization.setOrganizationName(rs.getString("organization_name"));
        organization.setEmail1(rs.getString("email1"));
        organization.setEmail2(rs.getString("email2"));
        organization.setContactInfo(rs.getString("contact_info"));
        organization.setPassword(rs.getString("password"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            organization.setCreatedAt(createdAt.toLocalDateTime());
        }

        return organization;
    }
}