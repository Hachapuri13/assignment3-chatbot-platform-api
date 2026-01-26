package service;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Bot;
import model.User;
import repository.BotRepository;
import repository.ChatSessionRepository;
import repository.UserRepository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Date;

public class ChatService {
    private final BotRepository botRepository;
    private final UserRepository userRepository;
    private final ChatSessionRepository sessionRepository;

    public ChatService(BotRepository botRepository, UserRepository userRepository, ChatSessionRepository chatSessionRepository) {
        this.botRepository = botRepository;
        this.userRepository = userRepository;
        this.sessionRepository = chatSessionRepository;
    }

    // Bot CRUD operations
    public List<model.Bot> getAllBots() throws SQLException {
        return botRepository.getAll();
    }

    public model.Bot getBotById(int id) throws SQLException, exception.ResourceNotFoundException {
        model.Bot bot = botRepository.getById(id);

        if (bot == null) {
            throw new exception.ResourceNotFoundException("Bot with ID " + id + " not found.");
        }
        return bot;
    }

    public void createBot(String name, String greeting, String definition, int tokenLimit)
            throws InvalidInputException {

        if (name == null || name.trim().isEmpty()) {
            throw new exception.InvalidInputException("Bot name cannot be empty.");
        }
        if (tokenLimit <= 0) {
            throw new exception.InvalidInputException("Token limit must be positive.");
        }

        model.Bot bot = new model.Bot(0, name, greeting, definition, tokenLimit);

        try {
            botRepository.create(bot);
        } catch (SQLException e) {
            throw new exception.DatabaseOperationException("Critical error: Could not save bot to database.", e);
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

    public void deleteBot(int id) throws SQLException {
        boolean isDeleted = botRepository.delete(id);

        if (!isDeleted) {
            throw new exception.ResourceNotFoundException("Bot with ID " + id + " not found.");
        }
    }

    // User CRUD operations
    public List<model.User> getAllUsers() throws SQLException {
        return userRepository.getAll();
    }

    public model.User getUserById(int id) throws SQLException, exception.ResourceNotFoundException {
        model.User user = userRepository.getById(id);
        if (user == null) {
            throw new exception.ResourceNotFoundException("User with ID " + id + " not found.");
        }
        return user;
    }

    public void createUser(String name, String persona, boolean isPremium) throws InvalidInputException, SQLException {
        if (name == null || name.isEmpty()) {
            throw new InvalidInputException("User name cannot be empty.");
        }
        User user = new User(0, name, persona, isPremium);
        userRepository.create(user);
    }

    public void updateUser(int id, String newName, String newPersona, boolean isPremium)
            throws SQLException, exception.ResourceNotFoundException, exception.InvalidInputException {

        if (newName == null || newName.trim().isEmpty()) {
            throw new exception.InvalidInputException("User name cannot be empty.");
        }

        model.User updatedUser = new model.User(id, newName, newPersona, isPremium);

        boolean success = userRepository.update(updatedUser);
        if (!success) {
            throw new exception.ResourceNotFoundException("Cannot update: User with ID " + id + " not found.");
        }
    }

    public void deleteUser(int id) throws SQLException, exception.ResourceNotFoundException {
        boolean success = userRepository.delete(id);
        if (!success) {
            throw new exception.ResourceNotFoundException("Cannot delete: User with ID " + id + " not found.");
        }
    }

    // ChatSession CRUD operations
    public List<model.ChatSession> getAllSessions() throws SQLException {
        return sessionRepository.getAll();
    }

    public model.ChatSession getSessionById(int id) throws SQLException, exception.ResourceNotFoundException {
        model.ChatSession session = sessionRepository.getById(id);
        if (session == null) {
            throw new exception.ResourceNotFoundException("Session with ID " + id + " not found.");
        }
        return session;
    }

    public void logChatSession(Bot bot, User user, Date startTime, int tokensUsed) throws SQLException {
        Timestamp sqlStartTime = new Timestamp(startTime.getTime());
        sessionRepository.logSession(bot, user, sqlStartTime, tokensUsed);
    }

    public void updateSessionTokens(int id, int newTotalTokens)
            throws SQLException, exception.ResourceNotFoundException, exception.InvalidInputException {

        if (newTotalTokens < 0) {
            throw new exception.InvalidInputException("Token count cannot be negative.");
        }

        boolean success = sessionRepository.update(id, newTotalTokens);
        if (!success) {
            throw new exception.ResourceNotFoundException("Cannot update: Session with ID " + id + " not found.");
        }
    }

    public void deleteSession(int id) throws SQLException, exception.ResourceNotFoundException {
        boolean success = sessionRepository.delete(id);
        if (!success) {
            throw new exception.ResourceNotFoundException("Cannot delete: Session with ID " + id + " not found.");
        }
    }
}