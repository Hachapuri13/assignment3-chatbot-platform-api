package repository;

import data.interfaces.IDB;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final IDB db;

    public UserRepository(IDB db) {
        this.db = db;
    }

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, persona, is_premium) VALUES (?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPersona());
            pstmt.setBoolean(3, user.isPremium());
            pstmt.executeUpdate();
        }
    }

    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("persona"),
                        rs.getBoolean("is_premium")
                ));
            }
        }
        return users;
    }

    public User getById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("persona"),
                        rs.getBoolean("is_premium")
                );
            }
        }
        return null;
    }
}