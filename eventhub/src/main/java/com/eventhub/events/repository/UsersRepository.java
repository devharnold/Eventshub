package com.eventhub.events.repository;

import java.sql.*;
import java.util.List;
import org.slf4j.Logger;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

import com.eventhub.events.utils.UniqueIdGenerator;

import com.eventhub.events.model.Users;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepository {
    private final DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(UsersRepository.class);

    public UsersRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Users createUser(Users users) {
        String sql = "INSERT INTO users (email, firstName, lastName, phoneNumber, password) VALUES (?, ?, ?, ?, ?) RETURNING userId";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, users.getUserId());
            stmt.setString(2, users.getEmail());
            stmt.setString(3, users.getFirstName());
            stmt.setString(4, users.getLastName());
            stmt.setString(5, users.getPhoneNumber());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
//                    String.generatedId = rs.getString("userId");
                    String generatedId = UniqueIdGenerator.generateUniqueId();
                    users.setUserId(generatedId);
                    logger.info("Created user with ID: {}", users.getUserId());
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating user with ID: {}", users, e);
        }
        return users;
    }

    public List<Users> findAllUsers() {
        String sql = "SELECT * FROM users";
        List<Users> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.addRow(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("Error occurred while trying to find users", e);
        }
        return users;
    }

    public Users findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching user with email: {}", email, e);
        }
        return null;
    }

    private Users mapRow(ResultSet rs) throws SQLException {
        return new Users(
//                rs.getString("userId"),
//                rs.getString("email"),
//                rs.getString("firstName"),
//                rs.getString("lastName"),
//                rs.getString("phoneNumber"),
//                rs.getTimestamp("createdAt"),
//                rs.getTimestamp("updatedAt")
        );
    }
}