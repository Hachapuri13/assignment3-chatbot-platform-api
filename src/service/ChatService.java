package service;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Bot;
import model.User;
import repository.BotRepository;
import repository.UserRepository;

import java.sql.SQLException;

public class ChatService {
    private final BotRepository botRepository;
    private final UserRepository userRepository;

    public ChatService(BotRepository botRepository, UserRepository userRepository) {
        this.botRepository = botRepository;
        this.userRepository = userRepository;
    }

    public void createBot(String name, String greeting, String definition, int tokenLimit) throws InvalidInputException, SQLException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("Bot name cannot be empty.");
        }
        if (tokenLimit <= 0) {
            throw new InvalidInputException("Token limit must be positive.");
        }
        Bot bot = new Bot(0, name, greeting, definition, tokenLimit);
        botRepository.create(bot);
    }

    public void createUser(String name, String persona, boolean isPremium) throws InvalidInputException, SQLException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("User name cannot be empty.");
        }
        User user = new User(0, name, persona, isPremium);
        userRepository.create(user);
    }

    public void deleteBot(int id) throws SQLException {
        boolean isDeleted = botRepository.delete(id);

        if (!isDeleted) {
            throw new exception.ResourceNotFoundException("Bot with ID " + id + " not found.");
        }
    }
    public void updateBot(int id, String newName, String newGreet, String newDef, int newLimit) throws Exception {
        if (newLimit <= 0) {
            throw new exception.InvalidInputException("Token limit must be positive.");
        }
        if (newName == null || newName.trim().isEmpty()) {
            throw new exception.InvalidInputException("Bot name cannot be empty.");
        }

        model.Bot updatedBot = new model.Bot(id, newName, newGreet, newDef, newLimit);

        boolean isUpdated = botRepository.update(updatedBot);

        if (!isUpdated) {
            throw new exception.ResourceNotFoundException("Bot with ID " + id + " not found.");
        }
    }
}