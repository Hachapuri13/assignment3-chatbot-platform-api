package controller;

import data.PostgresDB;
import data.interfaces.IDB;
import model.Bot;
import model.User;
import model.ChatSession;
import repository.BotRepository;
import repository.UserRepository;
import service.ChatService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
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

        repository.ChatSessionRepository sessionRepo = new repository.ChatSessionRepository(db);
        ChatService service = new ChatService(botRepo, userRepo, sessionRepo);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CHATBOT PLATFORM API ===");
            System.out.println("1. Create New Bot");
            System.out.println("2. Create New User");
            System.out.println("3. Show All Bots");
            System.out.println("4. Start Chat Session");
            System.out.println("5. Delete Bot");
            System.out.println("6. Update Bot");
            System.out.println("0. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter Bot Name: ");
                        String bName = scanner.nextLine();
                        System.out.print("Enter Greeting: ");
                        String bGreet = scanner.nextLine();
                        System.out.print("Enter Definition (Prompt): ");
                        String bDef = scanner.nextLine();
                        System.out.print("Enter Token Limit (e.g., 8000): ");
                        int bLimit = Integer.parseInt(scanner.nextLine());

                        service.createBot(bName, bGreet, bDef, bLimit);
                        System.out.println("Bot created successfully!");
                        break;

                    case "2":
                        System.out.print("Enter User Name: ");
                        String uName = scanner.nextLine();
                        System.out.print("Enter Persona Description: ");
                        String uPers = scanner.nextLine();
                        System.out.print("Is Premium? (true/false): ");
                        boolean uPrem = Boolean.parseBoolean(scanner.nextLine());

                        service.createUser(uName, uPers, uPrem);
                        System.out.println("User created successfully!");
                        break;

                    case "3":
                        List<Bot> bots = botRepo.getAll();
                        for (Bot b : bots) {
                            b.displayInfo();
                        }
                        break;

                    case "4":
                        System.out.print("Enter Bot ID: ");
                        int botId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter User ID: ");
                        int userId = Integer.parseInt(scanner.nextLine());

                        Bot b = botRepo.getById(botId);
                        User u = userRepo.getById(userId);

                        if (b != null && u != null) {
                            Date now = new Date();

                            int contextLoad = b.estimateTokenUsage() + u.estimateTokenUsage();

                            service.logChatSession(b, u, now, contextLoad);
                            System.out.println(">> Connection logged to Database successfully.");

                            ChatSession session = new ChatSession(1, b, u, now, contextLoad);
                            session.printSessionDetails();

                        } else {
                            System.out.println("Bot or User not found.");
                        }
                        break;
                    case "5":
                        System.out.print("Enter Bot ID to delete: ");
                        int delId = Integer.parseInt(scanner.nextLine());

                        service.deleteBot(delId);
                        System.out.println("Success: Bot deleted.");
                        break;
                    case "6":
                        System.out.print("Enter Bot ID to update: ");
                        int upId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter New Name: ");
                        String upName = scanner.nextLine();
                        System.out.print("Enter New Greeting: ");
                        String upGreet = scanner.nextLine();
                        System.out.print("Enter New Definition: ");
                        String upDef = scanner.nextLine();
                        System.out.print("Enter New Token Limit: ");
                        int upLimit = Integer.parseInt(scanner.nextLine());

                        service.updateBot(upId, upName, upGreet, upDef, upLimit);
                        System.out.println("Success: Bot updated.");
                        break;
                    case "0":
                        System.out.println("Exiting...");
                        db.close();
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