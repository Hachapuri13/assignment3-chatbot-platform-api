package repository;

import data.interfaces.IDB;
import model.Bot;
import model.User;
import model.ChatSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatSessionRepository {
    private final IDB db;

    public ChatSessionRepository(IDB db) {
        this.db = db;
    }

    // create
    public void logSession(Bot bot, User user, Timestamp startedAt, int totalTokens) throws SQLException {
        String sql = "INSERT INTO chat_sessions (bot_id, user_id, started_at, total_tokens_used) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bot.getId());
            pstmt.setInt(2, user.getId());
            pstmt.setTimestamp(3, startedAt);
            pstmt.setInt(4, totalTokens);
            pstmt.executeUpdate();
        }
    }

    public List<ChatSession> getAll() throws SQLException {
        String sql = "SELECT * FROM chat_sessions";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            List<ChatSession> sessions = new ArrayList<>();
            while (rs.next()) {
                sessions.add(mapRowToSession(rs));
            }
            return sessions;
        }
    }

    public ChatSession getById(int id) throws SQLException {
        String sql = "SELECT * FROM chat_sessions WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSession(rs);
                }
            }
        }
        return null;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM chat_sessions WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean update(int id, int newTokens) throws SQLException {
        String sql = "UPDATE chat_sessions SET total_tokens_used = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newTokens);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    // extra method to create dummy
    private ChatSession mapRowToSession(ResultSet rs) throws SQLException {
        Bot botStub = new Bot(rs.getInt("bot_id"), "Unknown", "", "", 0);
        User userStub = new User(rs.getInt("user_id"), "Unknown", "", false);

        return new ChatSession(
                rs.getInt("id"),
                botStub,
                userStub,
                rs.getTimestamp("started_at"),
                rs.getInt("total_tokens_used")
        );
    }
}