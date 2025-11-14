package ui;

import interfaces.UserInterface;
import models.InternshipOpportunity;
import java.util.Scanner;
import java.util.List;

public class ConsoleUI implements UserInterface {
    private final Scanner scanner;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
    
    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    @Override
    public int getChoice(String prompt, int maxChoice) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Please enter a choice.");
                    continue;
                }
                
                int choice = Integer.parseInt(input);
                if (choice >= 0 && choice <= maxChoice) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between 0 and " + maxChoice + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    @Override
    public void displayInternship(InternshipOpportunity internship) {
        if (internship != null) {
            internship.displayInfo();
        } else {
            System.out.println("Internship information not available.");
        }
    }
    
    // Simple helper method for confirmation
    public boolean confirmAction(String message) {
        System.out.print(message + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
    
    // Simple helper to wait for user
    public void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}