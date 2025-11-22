package models;

import interfaces.InternshipViewer;
import services.InternshipViewerService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
/**
 * This is CompanyRepresentative class that creates internship and see student application
 * @author Dai Jiayu
 * @version 1.0
 */
public class CompanyRepresentative extends User implements InternshipViewer {
    private String company;
    private String department;
    private String position;
    private boolean approved;
    private List<InternshipOpportunity> internships;
    private final InternshipViewerService internshipViewer;

    public CompanyRepresentative(String userID, String name, String company, 
                               String department, String position,
                               InternshipViewerService internshipViewer) {
        super(userID, name);
        this.company = company;
        this.department = department;
        this.position = position;
        this.approved = false;
        this.internships = new ArrayList<>();
        this.internshipViewer = internshipViewer;
    }

    // Getters and setters
    public String getCompany() { return company; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public List<InternshipOpportunity> getInternships() { return internships; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    // Internship management
    public void createInternshipOpportunity(String title, String description, String level, String preferredMajor,
                                            LocalDate openDate, LocalDate closeDate, int slots) {
        InternshipOpportunity opp = new InternshipOpportunity(title, description, level, preferredMajor,
                                                              openDate, closeDate, slots, this.company);
        internships.add(opp);
        System.out.println("Created internship: " + title + " | Company: " + this.company);
    }

    // InternshipViewer implementation
    @Override
    public void viewInternshipsWithFilter(List<InternshipOpportunity> internships, InternshipFilter filter) {
        // Filter to only show company's internships
        List<InternshipOpportunity> myInternships = new ArrayList<>();
        for (InternshipOpportunity opp : internships) {
            if (this.company.equals(opp.getCompany())) {
                myInternships.add(opp);
            }
        }
    
        internshipViewer.viewInternshipsWithFilter(myInternships, filter, getViewerType());
    }
    
    @Override
    public String getViewerType() {
        return "company";
    }

    // Edit internship before approval
    public void editInternshipBeforeApproval() {
        Scanner sc = new Scanner(System.in);
        if (internships.isEmpty()) {
            System.out.println("No internships available to edit.");
            return;
        }

        System.out.println("Your pending internships:");
        List<InternshipOpportunity> pending = new ArrayList<>();
        for (InternshipOpportunity i : internships) {
            if ("Pending".equalsIgnoreCase(i.getStatus())) {
                pending.add(i);
                System.out.println(pending.size() + ". " + i.getTitle());
            }
        }

        if (pending.isEmpty()) {
            System.out.println("No pending internships to edit.");
            return;
        }

        System.out.print("Select internship number to edit: ");
        int choice;
        try { 
            choice = Integer.parseInt(sc.nextLine().trim()); 
        } catch (Exception e) { 
            System.out.println("Invalid input.");
            return; 
        }
        
        if (choice < 1 || choice > pending.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        InternshipOpportunity opp = pending.get(choice - 1);

        System.out.println("\n=== Editing: " + opp.getTitle() + " ===");
        System.out.println("Current details:");
        System.out.println("1. Title: " + opp.getTitle());
        System.out.println("2. Description: " + opp.getDescription());
        System.out.println("3. Level: " + opp.getLevel());
        System.out.println("4. Preferred Major: " + opp.getPreferredMajor());
        System.out.println("5. Open Date: " + opp.getOpenDate());
        System.out.println("6. Close Date: " + opp.getCloseDate());
        System.out.println("7. Slots: " + opp.getSlots());
        
        String currentTitle = opp.getTitle();
        String currentDesc = opp.getDescription();
        String currentLevel = opp.getLevel();
        String currentMajor = opp.getPreferredMajor();
        LocalDate currentOpen = opp.getOpenDate();
        LocalDate currentClose = opp.getCloseDate();
        int currentSlots = opp.getSlots();

        System.out.println("\nWhich fields do you want to change? (Enter numbers separated by commas, e.g., 1,3,5)");
        System.out.print("Your choice: ");
        String[] fieldsToChange = sc.nextLine().trim().split(",");
    
        String newTitle = currentTitle;
        String newDesc = currentDesc;
        String newLevel = currentLevel;
        String newMajor = currentMajor;
        LocalDate newOpen = currentOpen;
        LocalDate newClose = currentClose;
        int newSlots = currentSlots;

        for (String field : fieldsToChange) {
            field = field.trim();
            switch (field) {
                case "1":
                    System.out.print("New title (current: " + currentTitle + "): ");
                    newTitle = sc.nextLine().trim();
                    if (newTitle.isEmpty()) newTitle = currentTitle;
                    break;
                
                case "2":
                    System.out.print("New description (current: " + currentDesc + "): ");
                    newDesc = sc.nextLine().trim();
                    if (newDesc.isEmpty()) newDesc = currentDesc;
                    break;
                
                case "3":
                    System.out.print("New level (current: " + currentLevel + "): ");
                    newLevel = sc.nextLine().trim();
                    if (newLevel.isEmpty()) newLevel = currentLevel;
                    break;
                
                case "4":
                    System.out.print("New preferred major (current: " + currentMajor + "): ");
                    newMajor = sc.nextLine().trim();
                    if (newMajor.isEmpty()) newMajor = currentMajor;
                    break;
                
                case "5":
                    System.out.print("New open date (yyyy-mm-dd) (current: " + currentOpen + "): ");
                    String openInput = sc.nextLine().trim();
                    if (!openInput.isEmpty()) {
                        try {
                            newOpen = LocalDate.parse(openInput);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Keeping current date.");
                            newOpen = currentOpen;
                        }
                    }
                    break;
                
                case "6":
                    System.out.print("New close date (yyyy-mm-dd) (current: " + currentClose + "): ");
                    String closeInput = sc.nextLine().trim();
                    if (!closeInput.isEmpty()) {
                        try {
                            newClose = LocalDate.parse(closeInput);
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Keeping current date.");
                            newClose = currentClose;
                        }
                    }
                    break;
                
                case "7":
                    System.out.print("New slots (current: " + currentSlots + "): ");
                    String slotsInput = sc.nextLine().trim();
                    if (!slotsInput.isEmpty()) {
                        try {
                            newSlots = Integer.parseInt(slotsInput);
                            if (newSlots > 10) newSlots = 10;
                            if (newSlots < 1) newSlots = 1;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number. Keeping current slots.");
                            newSlots = currentSlots;
                        }
                    }
                    break;
                
                default:
                    System.out.println("Invalid field number: " + field);
            }
        }
    
        // Apply the changes
        opp.editDetails(newTitle, newDesc, newLevel, newMajor, newOpen, newClose, newSlots);
    }
        
    // Delete internship (only if it belongs to this company)
    public void deleteInternship() {
        Scanner sc = new Scanner(System.in);
    
        if (internships.isEmpty()) {
            System.out.println("No internships available to delete.");
            return;
        }

        System.out.println("\n=== Delete Internship Opportunity ===");
        System.out.println("Your internships:");
    
        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            System.out.println((i + 1) + ". " + opp.getTitle() + " | Status: " + opp.getStatus());
        }

        System.out.print("Select internship number to delete (0 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice == 0) {
            System.out.println("Deletion cancelled.");
            return;
        }

        if (choice < 1 || choice > internships.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        InternshipOpportunity selected = internships.get(choice - 1);

        // prevent deleting approved internships
        if ("Approved".equalsIgnoreCase(selected.getStatus())) {
            System.out.println("Cannot delete approved internship opportunities.");
            return;
        }

        System.out.print("Are you sure you want to delete \"" + selected.getTitle() + "\"? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            internships.remove(selected);
            System.out.println("Internship \"" + selected.getTitle() + "\" has been deleted successfully.");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Toggle visibility of approved internships
    public void toggleVisibility() {
        Scanner sc = new Scanner(System.in);
        if (internships.isEmpty()) {
            System.out.println("No internships available.");
            return;
        }

        System.out.println("Your approved internships:");
        List<InternshipOpportunity> approvedList = new ArrayList<>();
        for (InternshipOpportunity i : internships) {
            if ("Approved".equalsIgnoreCase(i.getStatus())) {
                approvedList.add(i);
                System.out.println(approvedList.size() + ". " + i.getTitle() +
                        " (Currently: " + (i.isVisible() ? "Visible" : "Hidden") + ")");
            }
        }

        if (approvedList.isEmpty()) {
            System.out.println("No approved internships available to toggle visibility.");
            return;
        }

        System.out.print("Select internship number to toggle visibility: ");
        int choice;
        try { 
            choice = Integer.parseInt(sc.nextLine().trim()); 
        } catch (Exception e) { 
            System.out.println("Invalid input.");
            return; 
        }
        
        if (choice < 1 || choice > approvedList.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        InternshipOpportunity opp = approvedList.get(choice - 1);
        opp.toggleVisibility();
    }

    // Review student applications
    public void reviewStudentApplications() {
        Scanner sc = new Scanner(System.in);

        for (InternshipOpportunity i : internships) {
            List<InternshipApplication> apps = i.getApplications();

            if (apps.isEmpty()) {
                System.out.println("No applications for internship: " + i.getTitle());
                continue;
            }

            System.out.println("\n--- Applications for " + i.getTitle() + " ---");
            for (int j = 0; j < apps.size(); j++) {
                InternshipApplication app = apps.get(j);
                System.out.println((j + 1) + ". " + app.getStudent().getName()
                        + " | Major: " + app.getStudent().getMajor()
                        + " | Year: " + app.getStudent().getYear()
                        + " | Status: " + app.getStatus());
            }

            System.out.print("Enter number to approve (0 to skip): ");
            int choice;
            try { 
                choice = Integer.parseInt(sc.nextLine().trim()); 
            } catch (Exception e) { 
                System.out.println("Invalid input, skipping...");
                continue; 
            }
            
            if (choice == 0) continue;

            if (choice > 0 && choice <= apps.size()) {
                InternshipApplication selected = apps.get(choice - 1);
                if ("Pending".equalsIgnoreCase(selected.getStatus())) {
                    approveApplication(selected);
                } else {
                    System.out.println("Cannot approve. Current status: " + selected.getStatus());
                }
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    // Approve student application
    public void approveApplication(InternshipApplication app) {
        if (!"Pending".equalsIgnoreCase(app.getStatus())) {
            System.out.println("Cannot approve application with status: " + app.getStatus());
            return;
        }
        app.approve();
        System.out.println("Application approved for " + app.getStudent().getName());
    }

    // Reject student application
    public void rejectApplication(InternshipApplication app) {
        if (!"Pending".equalsIgnoreCase(app.getStatus())) {
            System.out.println("Cannot reject application with status: " + app.getStatus());
            return;
        }
        app.reject();
        System.out.println("Application rejected for " + app.getStudent().getName());
    }

    // View company representative info
    public void displayInfo() {
        System.out.println("\n=== Company Representative Info ===");
        System.out.println("Name: " + getName());
        System.out.println("Company: " + company);
        System.out.println("Department: " + department);
        System.out.println("Position: " + position);
        System.out.println("Approved: " + approved);
        System.out.println("Internships Created: " + internships.size());
        
        int pendingCount = (int) internships.stream()
                .filter(opp -> "Pending".equalsIgnoreCase(opp.getStatus()))
                .count();
        int approvedCount = (int) internships.stream()
                .filter(opp -> "Approved".equalsIgnoreCase(opp.getStatus()))
                .count();
        
        System.out.println(" - Pending: " + pendingCount);
        System.out.println(" - Approved: " + approvedCount);
    }

    // Get pending internships count
    public int getPendingInternshipsCount() {
        int count = 0;
        for (InternshipOpportunity opp : internships) {
            if ("Pending".equalsIgnoreCase(opp.getStatus())) {
                count++;
            }
        }
        return count;
    }

    // Get approved internships count
    public int getApprovedInternshipsCount() {
        int count = 0;
        for (InternshipOpportunity opp : internships) {
            if ("Approved".equalsIgnoreCase(opp.getStatus())) {
                count++;
            }
        }
        return count;
    }

    // Find internship by title
    public InternshipOpportunity findInternshipByTitle(String title) {
        for (InternshipOpportunity opp : internships) {
            if (opp.getTitle().equalsIgnoreCase(title)) {
                return opp;
            }
        }
        return null;
    }

}
