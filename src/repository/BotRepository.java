package repository;

import data.interfaces.IDB;
import model.Bot;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BotRepository {
    private final IDB db;

    public BotRepository(IDB db) {
        this.db = db;
    }
    public void create(Bot bot) throws SQLException {
        String sql = "INSERT INTO bots (name, greeting, definition, token_limit) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bot.getName());
            pstmt.setString(2, bot.getGreeting());
            pstmt.setString(3, bot.getDefinition());
            pstmt.setInt(4, bot.getTokenLimit());
            pstmt.executeUpdate();
        }
    }

    public List<Bot> getAll() throws SQLException {
        List<Bot> bots = new ArrayList<>();
        String sql = "SELECT * FROM bots";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                bots.add(new Bot(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("greeting"),
                        rs.getString("definition"),
                        rs.getInt("token_limit")
                ));
            }
        }
        return bots;
    }

    public Bot getById(int id) throws SQLException {
        String sql = "SELECT * FROM bots WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Bot(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("greeting"),
                        rs.getString("definition"),
                        rs.getInt("token_limit")
                );
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM bots WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public boolean update(Bot bot) throws SQLException {
        String sql = "UPDATE bots SET name = ?, greeting = ?, definition = ?, token_limit = ? WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bot.getName());
            pstmt.setString(2, bot.getGreeting());
            pstmt.setString(3, bot.getDefinition());
            pstmt.setInt(4, bot.getTokenLimit());
            pstmt.setInt(5, bot.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}