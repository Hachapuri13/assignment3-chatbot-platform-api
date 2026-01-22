package service;

import exception.InvalidInputException;
import model.Bot;
import model.User;
import repository.BotRepository;
import repository.UserRepository;

import java.sql.SQLException;

public class ChatService {
    private BotRepository botRepository = new BotRepository();
    private UserRepository userRepository = new UserRepository();

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
}