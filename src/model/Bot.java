package model;

public class Bot extends ChatParticipantBase implements Tokenizable {
    private String greeting;
    private String definition;
    private int tokenLimit;

    public Bot(int id, String name, String greeting, String definition, int tokenLimit) {
        super(id, name);
        this.greeting = greeting;
        this.definition = definition;
        this.tokenLimit = tokenLimit;
    }

    @Override
    public String getSystemPrompt() {
        return "System Instruction: You are " + name + ". " + definition;
    }

    @Override
    public void displayInfo() {
        System.out.println("[BOT] ID: " + id + " | Name: " + name + " | Limit: " + tokenLimit);
    }

    @Override
    public int estimateTokenUsage() {
        return (name.length() + definition.length() + greeting.length()) / 4;
    }

    @Override
    public String getRole() {
        return "AI Assistant";
    }

    @Override
    public String getLogMessage() {
        return "Bot Log [ID=" + id + "]: " + name + " (Limit: " + tokenLimit + ")";
    }

    public String getGreeting() { return greeting; }
    public String getDefinition() { return definition; }
    public int getTokenLimit() { return tokenLimit; }
}