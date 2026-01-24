package model;

public abstract class ChatParticipantBase implements Loggable {
    protected int id;
    protected String name;

    public ChatParticipantBase(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public abstract String getSystemPrompt();

    public abstract String getRole();

    public abstract void displayInfo();
}