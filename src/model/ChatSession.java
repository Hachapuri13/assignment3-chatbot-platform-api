package model;

import java.util.Date;

public class ChatSession {
    private int id;
    private Bot bot;
    private User user;
    private Date startedAt;
    private int totalTokensUsed;

    public ChatSession(int id, Bot bot, User user, Date startedAt, int totalTokensUsed) {
        this.id = id;
        this.bot = bot;
        this.user = user;
        this.startedAt = startedAt;
        this.totalTokensUsed = totalTokensUsed;
    }

    public void printSessionDetails() {
        System.out.println("=== Chat Session #" + id + " ===");
        System.out.println("Participants:");
        bot.displayInfo();
        user.displayInfo();
        System.out.println("Total Context Load: " + totalTokensUsed + " tokens.");
        System.out.println("Started at: " + startedAt);
        System.out.println("============================");
    }

    @Override
    public String toString() {
        return "Session ID: " + id +
                " | Bot ID: " + (bot != null ? bot.getId() : "null") +
                " | User ID: " + (user != null ? user.getId() : "null") +
                " | Started: " + startedAt +
                " | Tokens: " + totalTokensUsed;
    }
}