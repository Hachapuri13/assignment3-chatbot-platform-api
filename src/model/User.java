package model;

public class User extends ChatParticipantBase implements Tokenizable {
    private String persona;
    private boolean isPremium;

    public User(int id, String name, String persona, boolean isPremium) {
        super(id, name);
        this.persona = persona;
        this.isPremium = isPremium;
    }

    @Override
    public String getSystemPrompt() {
        return "[User Context] Name: " + name + ". Persona: " + persona;
    }

    @Override
    public void displayInfo() {
        System.out.println("[USER] ID: " + id + " | Name: " + name + " | Premium: " + isPremium);
    }

    @Override
    public int estimateTokenUsage() {
        return (name.length() + persona.length()) / 4;
    }

    public String getPersona() { return persona; }
    public boolean isPremium() { return isPremium; }
}