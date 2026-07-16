package com.eventhub.events.dao.impl;

import com.eventhub.events.dao.UsersDao;
import com.eventhub.events.model.Users;
import com.eventhub.events.utils.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsersDaoImpl implements UsersDao {

    private static final Logger logger =
            LoggerFactory.getLogger(UsersDaoImpl.class);

    private final DataSource dataSource;

    public UsersDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Users createUserProfile(Users user) {

        String sql = """
                INSERT INTO users
                (
                    username,
                    email,
                    first_name,
                    last_name,
                    phone_number,
                    password,
                    created_at,
                    updated_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        String hashedPassword = PasswordHash.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getFirstName());
            stmt.setString(4, user.getLastName());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setString(6, user.getPassword());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt("id"));
                }
            }

            logger.info("Created user {}", user.getUsername());

        } catch (SQLException e) {
            logger.error("Error creating user", e);
        }

        return user;
    }

    @Override
    public Users findByName(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error finding user {}", username, e);
        }

        return null;
    }

    @Override
    public List<Users> findAll() {

        List<Users> users = new ArrayList<>();

        String sql = "SELECT * FROM users ORDER BY username";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }

        } catch (SQLException e) {
            logger.error("Error retrieving users", e);
        }

        return users;
    }

    private Users mapRow(ResultSet rs) throws SQLException {

        Users user = new Users();

        user.setUserId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        return user;
    }
}