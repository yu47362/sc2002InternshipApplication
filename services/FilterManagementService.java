package services;

import interfaces.FilterManager;
import interfaces.InternshipFilterService;
import models.InternshipOpportunity;
import models.InternshipFilter;
import models.Student;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FilterManagementService implements FilterManager {
    private final Student student;
    
    public FilterManagementService(Student student) {
        this.student = student;
    }
    
    @Override
    public void filterInternships(List<InternshipOpportunity> allInternships,
                                           InternshipFilterService filterService,
                                           InternshipFilter currentFilter) {
        Scanner sc = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== Internship Filters ===");
            System.out.println("Current Filters: " + currentFilter.getFilterSummary());
            System.out.println("1. Filter by Status");
            System.out.println("2. Filter by Level");
            System.out.println("3. Filter by Closing Date Range");
            System.out.println("4. Filter by Company");
            System.out.println("5. Filter by Preferred Major");
            System.out.println("6. Change Sorting");
            System.out.println("7. Apply Filters and View Results");
            System.out.println("8. Clear All Filters");
            System.out.println("9. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            String choice = sc.nextLine().trim();
            
            switch (choice) {
                case "1" -> setStatusFilter(currentFilter, filterService.getAvailableStatuses(allInternships));
                case "2" -> setLevelFilter(currentFilter, filterService.getAvailableLevels(allInternships));
                case "3" -> setDateFilter(currentFilter);
                case "4" -> setCompanyFilter(currentFilter, filterService.getAvailableCompanies(allInternships));
                case "5" -> setPreferredMajorFilter(currentFilter, filterService.getAvailablePreferredMajors(allInternships));
                case "6" -> setSorting(currentFilter);
                case "7" -> { 
                    System.out.println("Filters applied successfully!");
                    return; 
                }
                case "8" -> {
                    currentFilter.clearFilters();
                    System.out.println("All filters cleared.");
                }
                case "9" -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void setStatusFilter(InternshipFilter filter, List<String> availableStatuses) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Filter by Status ===");
        System.out.println("Available Statuses:");
        System.out.println("0. Clear status filter");
        for (int i = 0; i < availableStatuses.size(); i++) {
            System.out.println((i + 1) + ". " + availableStatuses.get(i));
        }
        System.out.print("Select status (0 to clear): ");
        
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 0) {
                filter.setStatus(null);
                System.out.println("Status filter cleared.");
            } else if (choice > 0 && choice <= availableStatuses.size()) {
                filter.setStatus(availableStatuses.get(choice - 1));
                System.out.println("Status filter set to: " + availableStatuses.get(choice - 1));
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    

    private void setLevelFilter(InternshipFilter filter, List<String> availableLevels) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Filter by Level ===");
        System.out.println("Available Levels:");
        System.out.println("0. Clear level filter");
        for (int i = 0; i < availableLevels.size(); i++) {
            System.out.println((i + 1) + ". " + availableLevels.get(i));
        }
        System.out.print("Select level (0 to clear): ");
        
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 0) {
                filter.setLevel(null);
                System.out.println("Level filter cleared.");
            } else if (choice > 0 && choice <= availableLevels.size()) {
                filter.setLevel(availableLevels.get(choice - 1));
                System.out.println("Level filter set to: " + availableLevels.get(choice - 1));
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    
    private void setDateFilter(InternshipFilter filter) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Filter by Closing Date Range ===");
        
        System.out.print("Closing after date (yyyy-mm-dd or empty for no limit): ");
        String afterInput = sc.nextLine().trim();
        if (!afterInput.isEmpty()) {
            try {
                filter.setClosingDateAfter(LocalDate.parse(afterInput));
                System.out.println("Closing after date set to: " + afterInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            }
        } else {
            filter.setClosingDateAfter(null);
            System.out.println("Closing after date cleared.");
        }
        
        System.out.print("Closing before date (yyyy-mm-dd or empty for no limit): ");
        String beforeInput = sc.nextLine().trim();
        if (!beforeInput.isEmpty()) {
            try {
                filter.setClosingDateBefore(LocalDate.parse(beforeInput));
                System.out.println("Closing before date set to: " + beforeInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            }
        } else {
            filter.setClosingDateBefore(null);
            System.out.println("Closing before date cleared.");
        }
    }
    
    private void setCompanyFilter(InternshipFilter filter, List<String> availableCompanies) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Filter by Company ===");
        System.out.println("Available Companies:");
        System.out.println("0. Clear company filter");
        for (int i = 0; i < availableCompanies.size(); i++) {
            System.out.println((i + 1) + ". " + availableCompanies.get(i));
        }
        System.out.print("Select company (0 to clear): ");
        
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 0) {
                filter.setCompany(null);
                System.out.println("Company filter cleared.");
            } else if (choice > 0 && choice <= availableCompanies.size()) {
                filter.setCompany(availableCompanies.get(choice - 1));
                System.out.println("Company filter set to: " + availableCompanies.get(choice - 1));
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    private void setPreferredMajorFilter(InternshipFilter filter, List<String> availableMajors) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Filter by Preferred Major ===");
        System.out.println("Available Preferred Majors:");
        System.out.println("0. Clear preferred major filter");
        for (int i = 0; i < availableMajors.size(); i++) {
            System.out.println((i + 1) + ". " + availableMajors.get(i));
        }
        System.out.print("Select preferred major (0 to clear): ");
        
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
            if (choice == 0) {
                filter.setPreferredMajor(null);
                System.out.println("Preferred major filter cleared.");
            } else if (choice > 0 && choice <= availableMajors.size()) {
                filter.setPreferredMajor(availableMajors.get(choice - 1));
                System.out.println("Preferred major filter set to: " + availableMajors.get(choice - 1));
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
    
    private void setSorting(InternshipFilter filter) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Change Sorting ===");
        System.out.println("1. Title (Alphabetical)");
        System.out.println("2. Company");
        System.out.println("3. Closing Date");
        System.out.println("4. Level");
        System.out.println("5. Status");
        System.out.println("6. Preferred Major");
        System.out.print("Choose field to sort by: ");
        String fieldChoice = sc.nextLine().trim();
        
        String sortField = "title";
        switch (fieldChoice) {
            case "2": sortField = "company"; break;
            case "3": sortField = "closingdate"; break;
            case "4": sortField = "level"; break;
            case "5": sortField = "status"; break;
            case "6": sortField = "preferredmajor"; break;
        }
        filter.setSortBy(sortField);
        
        System.out.print("Sort order (1=Ascending, 2=Descending): ");
        String orderChoice = sc.nextLine().trim();
        filter.setSortAscending(!"2".equals(orderChoice));
        
        System.out.println("Sorting set to: " + sortField + " (" + 
                          (filter.isSortAscending() ? "Ascending" : "Descending") + ")");
    }
}
