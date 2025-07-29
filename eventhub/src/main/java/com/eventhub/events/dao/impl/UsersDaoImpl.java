package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.utils.UniqueIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import com.eventhub.events.utils.PasswordHash;

@Repository
public class UsersDaoImpl implements UsersDao {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(UsersDaoImpl.class);

    public UsersDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Users createUserProfile(Users users) {
        String insert_query = "INSERT INTO users (userId, username, email, phone, password)" +  " VALUES (?, ?, ?, ?, ?)";
        String generatedId = UniqueIdGenerator.generateUniqueId();
        users.setUserId(generatedId);

        String plainPassword = users.getPassword();
        String hashedPassword = PasswordHash.hashPassword(plainPassword);
        users.setPassword(hashedPassword);

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(insert_query)) {

            stmt.setString(1, users.getUserId());
            stmt.setString(2, users.getUsername());
            stmt.setString(3, users.getEmail());
            stmt.setString(4, users.getPhoneNumber());
            stmt.setString(5, users.getPassword());

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Created user profile with ID: " + generatedId);
            } else {
                logger.warn("User insert_query failed");
            }
        } catch (SQLException e) {
            logger.error("Error while creating user profile", e);
        }
        return users;
    }

    @Override
    public Users findByName(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error while trying to find user", e);
        }
        return null;
    }

    @Override
    public List<Users> findAll() {
        List<Users> users = new ArrayList<>();
        String query = "SELECT username FROM users WHERE event = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    users.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error while executing query", e);
        }
        return users;
    }

    private Users mapRow(ResultSet rs) throws SQLException {
        return new Users();
    }
}