package model;

import java.util.Date;

public class ChatSession {
    private int id;
    private Bot bot;
    private User user;
    private Date startedAt;

    public ChatSession(int id, Bot bot, User user, Date startedAt) {
        this.id = id;
        this.bot = bot;
        this.user = user;
        this.startedAt = startedAt;
    }

    public void printSessionDetails() {
        System.out.println("=== Chat Session #" + id + " ===");
        System.out.println("Participants:");
        bot.displayInfo();
        user.displayInfo();
        System.out.println("Total Context Load: " + (bot.estimateTokenUsage() + user.estimateTokenUsage()) + " tokens.");
        System.out.println("Started at: " + startedAt);
        System.out.println("============================");
    }
}