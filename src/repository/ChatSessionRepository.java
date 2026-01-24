package repository;

import data.interfaces.IDB;
import model.Bot;
import model.User;
import java.sql.*;

public class ChatSessionRepository {
    private final IDB db;

    public ChatSessionRepository(IDB db) {
        this.db = db;
    }

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
}