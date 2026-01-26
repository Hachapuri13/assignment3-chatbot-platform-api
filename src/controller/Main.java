package controller;

import data.PostgresDB;
import data.interfaces.IDB;
import model.Bot;
import model.User;
import model.ChatSession;
import repository.BotRepository;
import repository.UserRepository;
import repository.ChatSessionRepository;
import service.ChatService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static ChatService service;
    private static Scanner scanner;

    public static void main(String[] args) {
        String host = System.getenv("DB_HOST");
        if (host == null) host = "localhost:5432";

        String dbUser = System.getenv("DB_USER");
        if (dbUser == null) dbUser = "postgres";

        String dbPass = System.getenv("DB_PASSWORD");
        if (dbPass == null) dbPass = "1234";

        String dbName = System.getenv("DB_NAME");
        if (dbName == null) dbName = "chatbot_platform";

        IDB db = new PostgresDB(host, dbUser, dbPass, dbName);
        BotRepository botRepo = new BotRepository(db);
        UserRepository userRepo = new UserRepository(db);
        ChatSessionRepository sessionRepo = new ChatSessionRepository(db);

        service = new ChatService(botRepo, userRepo, sessionRepo);
        scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CHATBOT PLATFORM API ===");
            System.out.println("CRUD operations (Create, Read, Update, Delete)");
            System.out.println("1. Manage BOTS");
            System.out.println("2. Manage USERS");
            System.out.println("3. Manage SESSIONS");
            System.out.println("0. Exit");
            System.out.print("Select entity: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        handleBotOperations();
                        break;
                    case "2":
                        handleUserOperations();
                        break;
                    case "3":
                        handleSessionOperations();
                        break;
                    case "0":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleBotOperations() {
        while (true) {
            System.out.println("\n--- MANAGE BOTS ---");
            System.out.println("1. Create Bot");
            System.out.println("2. Show All Bots");
            System.out.println("3. Find Bot by ID");
            System.out.println("4. Update Bot");
            System.out.println("5. Delete Bot");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select operation: ");

            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Greeting: ");
                        String greet = scanner.nextLine();
                        System.out.print("Definition: ");
                        String def = scanner.nextLine();
                        System.out.print("Token Limit: ");
                        int limit = Integer.parseInt(scanner.nextLine());
                        service.createBot(name, greet, def, limit);
                        System.out.println("Success: Bot created.");
                        break;

                    case "2":
                        List<Bot> bots = service.getAllBots();
                        System.out.println("--- List of Bots ---");
                        bots.forEach(Bot::displayInfo);
                        break;

                    case "3":
                        System.out.print("Enter ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        Bot b = service.getBotById(id);
                        b.displayInfo();
                        break;

                    case "4":
                        System.out.print("Enter ID to update: ");
                        int upId = Integer.parseInt(scanner.nextLine());
                        System.out.print("New Name: ");
                        String nName = scanner.nextLine();
                        System.out.print("New Greeting: ");
                        String nGreet = scanner.nextLine();
                        System.out.print("New Definition: ");
                        String nDef = scanner.nextLine();
                        System.out.print("New Limit: ");
                        int nLimit = Integer.parseInt(scanner.nextLine());
                        service.updateBot(upId, nName, nGreet, nDef, nLimit);
                        System.out.println("Success: Bot updated.");
                        break;

                    case "5":
                        System.out.print("Enter ID to delete: ");
                        int delId = Integer.parseInt(scanner.nextLine());
                        service.deleteBot(delId);
                        System.out.println("Success: Bot deleted.");
                        break;

                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleUserOperations() {
        while (true) {
            System.out.println("\n--- MANAGE USERS ---");
            System.out.println("1. Create User");
            System.out.println("2. Show All Users");
            System.out.println("3. Find User by ID");
            System.out.println("4. Update User");
            System.out.println("5. Delete User");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select operation: ");

            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Persona: ");
                        String persona = scanner.nextLine();
                        System.out.print("Is Premium (true/false): ");
                        boolean prem = Boolean.parseBoolean(scanner.nextLine());
                        service.createUser(name, persona, prem);
                        System.out.println("Success: User created.");
                        break;

                    case "2":
                        List<User> users = service.getAllUsers();
                        System.out.println("--- List of Users ---");
                        users.forEach(System.out::println);
                        break;

                    case "3":
                        System.out.print("Enter ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.println(service.getUserById(id));
                        break;

                    case "4":
                        System.out.print("Enter ID to update: ");
                        int upId = Integer.parseInt(scanner.nextLine());
                        System.out.print("New Name: ");
                        String nName = scanner.nextLine();
                        System.out.print("New Persona: ");
                        String nPers = scanner.nextLine();
                        System.out.print("Is Premium (true/false): ");
                        boolean nPrem = Boolean.parseBoolean(scanner.nextLine());
                        service.updateUser(upId, nName, nPers, nPrem);
                        System.out.println("Success: User updated.");
                        break;

                    case "5":
                        System.out.print("Enter ID to delete: ");
                        int delId = Integer.parseInt(scanner.nextLine());
                        service.deleteUser(delId);
                        System.out.println("Success: User deleted.");
                        break;

                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void handleSessionOperations() {
        while (true) {
            System.out.println("\n--- MANAGE SESSIONS ---");
            System.out.println("1. Start Chat (Create Session)");
            System.out.println("2. Show Session History");
            System.out.println("3. Find Session by ID");
            System.out.println("4. Update Token Usage");
            System.out.println("5. Delete Session Log");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select operation: ");

            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter Bot ID: ");
                        int botId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter User ID: ");
                        int userId = Integer.parseInt(scanner.nextLine());

                        Bot b = service.getBotById(botId);
                        User u = service.getUserById(userId);

                        Date now = new Date();
                        int load = b.estimateTokenUsage() + u.estimateTokenUsage();

                        service.logChatSession(b, u, now, load);
                        System.out.println(">> Chat started! Context load: " + load + " tokens.");
                        System.out.println(">> Session saved to database.");
                        break;

                    case "2":
                        List<ChatSession> sessions = service.getAllSessions();
                        System.out.println("--- Session History ---");
                        sessions.forEach(System.out::println);
                        break;

                    case "3":
                        System.out.print("Enter Session ID: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.println(service.getSessionById(id));
                        break;

                    case "4":
                        System.out.print("Enter Session ID: ");
                        int upId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new total tokens: ");
                        int tokens = Integer.parseInt(scanner.nextLine());
                        service.updateSessionTokens(upId, tokens);
                        System.out.println("Success: Session tokens updated.");
                        break;

                    case "5":
                        System.out.print("Enter Session ID: ");
                        int delId = Integer.parseInt(scanner.nextLine());
                        service.deleteSession(delId);
                        System.out.println("Success: Session deleted.");
                        break;

                    case "0":
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
