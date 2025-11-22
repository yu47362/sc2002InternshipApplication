import models.*;
import interfaces.*;
import services.*;
import session.*;
import data.*;
import ui.ConsoleUI;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static List<Student> students;
    private static List<CompanyRepresentative> companyReps = new ArrayList<>();
    private static List<CareerCenterStaff> staffList = new ArrayList<>();
    
    // Service instances
    private static final InternshipFilterService filterService = new InternshipFilterServiceImpl();
    private static final InternshipViewerService internshipViewer = new InternshipViewerService(filterService);
    private static final InternshipService internshipService = new InternshipService();
    private static final ConsoleUI consoleUI = new ConsoleUI();

    public static void main(String[] args) {
        // Initialize data loaders
        StudentDataLoader studentLoader = new StudentDataLoader(internshipViewer);
        StaffDataLoader staffLoader = new StaffDataLoader(internshipViewer);
        
        // Load data
        System.out.println("Loading application data...");
        students = studentLoader.readFromCSV("student_list.csv");
        staffList = staffLoader.readFromCSV("staffList.csv");
        System.out.println("System initialized successfully!");
        System.out.println("Loaded " + students.size() + " students, " + staffList.size() + " staff members, and " + companyReps.size() + " company representatives.");

        // for cleaner shut down of system in abrupt closure
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down session manager...");
            SessionManager.shutdown();
        }));

        System.out.println("\nWelcome to the Internship Placement Management System!");

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Student Login");
            System.out.println("2. Company Representative Login / Register");
            System.out.println("3. Career Center Staff Login");
            System.out.println("4. System Statistics");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            String choice = consoleUI.getInput("");

            switch (choice) {
                case "1":
                    studentMenu();
                    break;
                case "2":
                    companyRepMenu();
                    break;
                case "3":
                    staffMenu();
                    break;
                case "4":
                    showSystemStatistics();
                    break;
                case "5":
                    System.out.println("Thank you for using the Internship Placement Management System! Goodbye!");
                    SessionManager.shutdown();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // =================== Student Menu ===================
    private static void studentMenu() {
        String id = consoleUI.getInput("Enter Student ID: ");
        Student student = null;
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            if (s.getUserID().equals(id)) {
                student = s;
                break;
            }
        }

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        String pass = consoleUI.getInput("Enter password: ");
        if (!student.login(id, pass)) {
            System.out.println("Incorrect password.");
            return;
        }

        UserSession session = SessionManager.getSession(student.getUserID());
        if (session == null) {
            session = SessionManager.createSession(student);
        }

        System.out.println("Welcome " + student.getName() + "!");

        while (true) {
            System.out.println("\n=== Student Menu - " + student.getName() + " ===");
            System.out.println("1. View Applied Internships");
            System.out.println("2. View & Apply for Internships");
            System.out.println("3. Filter Internships");
            System.out.println("4. Accept Internship Offer");
            System.out.println("5. Request Withdrawal");
            System.out.println("6. View My Applications (Detailed)");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            String choice = consoleUI.getInput("");

            switch (choice) {
                case "1" -> student.viewApplicationStatus();
                case "2" -> {
                    List<InternshipOpportunity> available = internshipService.getAvailableInternshipsForStudent(student, companyReps);
                    if (available.isEmpty()) {
                        System.out.println("No internships available for application at the moment.");
                    } else {
                        studentApplyInternship(student, session, available);
                    }
                }
                case "3" -> studentFilterInternships(student, session);
                case "4" -> studentAcceptOffer(student);
                case "5" -> studentWithdrawal(student);
                case "6" -> student.viewMyApplicationsDetailed();
                case "7" -> changePassword(student);
                case "8" -> {
                    SessionManager.removeSession(student.getUserID());
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option.");
        
            }

            consoleUI.waitForEnter();
        }
    }

    private static void studentApplyInternship(Student student, UserSession session, List<InternshipOpportunity> available) {
        InternshipFilter currentFilter = session.getCurrentFilter();
        
        if (available.isEmpty()) {
            System.out.println("No internships available for application at the moment.");
            return;
        }
        
        // Show filtered internships
        System.out.println("Available Internships (with your current filters):");
        student.viewInternshipsWithFilter(available, currentFilter);

        String title = consoleUI.getInput("Enter internship title to apply (or 'cancel' to go back): ");
        if (title.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipOpportunity opp = internshipService.findInternshipByTitle(companyReps, title);
        if (opp != null) {
            if (consoleUI.confirmAction("Apply for '" + opp.getTitle() + "' at " + opp.getCompany() + "?")) {
                student.applyForInternship(opp);
            }
        } else {
            System.out.println("Internship not found. Please check the title and try again.");
        }
    }

    private static void studentFilterInternships(Student student, UserSession session) {
        List<InternshipOpportunity> allInternships = internshipService.getAllInternships(companyReps);
        
        if (allInternships.isEmpty()) {
            System.out.println("No internships available in the system.");
            return;
        }
        
        student.filterInternships(allInternships, filterService, session.getCurrentFilter());
        
        List<InternshipOpportunity> available = internshipService.getAvailableInternshipsForStudent(student, companyReps);
        System.out.println("Filtered Results:");
        student.viewInternshipsWithFilter(available, session.getCurrentFilter());
    }

    private static void studentAcceptOffer(Student student) {
        List<InternshipApplication> offers = student.getAppliedInternships().stream()
                .filter(a -> "Offered".equalsIgnoreCase(a.getStatus()))
                .toList();
                
        if (offers.isEmpty()) {
            System.out.println("No offers available to accept.");
            return;
        }
        
        System.out.println("\nYour Internship Offers:");
        for (int i = 0; i < offers.size(); i++) {
            InternshipApplication a = offers.get(i);
            System.out.println((i + 1) + ". " + a.getInternship().getTitle()
                    + " | Company: " + a.getInternship().getCompany()
                    + " | Level: " + a.getInternship().getLevel());
        }
        
        int choice = consoleUI.getChoice("Enter number to accept (0 to cancel): ", offers.size());
        if (choice == 0) return;
        
        InternshipApplication chosen = offers.get(choice - 1);
        if (consoleUI.confirmAction("Accept offer for '" + chosen.getInternship().getTitle() + "'?")) {
            student.acceptInternship(chosen);
        }
    }

    private static void studentWithdrawal(Student student) {
        List<InternshipApplication> apps = student.getAppliedInternships();
        if (apps.isEmpty()) {
            System.out.println("No applications to withdraw.");
            return;
        }
        
        // Show current applications
        System.out.println("\nYour Current Applications:");
        for (int i = 0; i < apps.size(); i++) {
            InternshipApplication app = apps.get(i);
            System.out.println((i + 1) + ". " + app.getInternship().getTitle()
                    + " | Status: " + app.getStatus());
        }
        
        int choice = consoleUI.getChoice("Select application to withdraw (0 to cancel): ", apps.size());
        if (choice == 0) return;
        
        InternshipApplication appToWithdraw = apps.get(choice - 1);
        if (consoleUI.confirmAction("Request withdrawal from '" + appToWithdraw.getInternship().getTitle() + "'?")) {
            student.requestWithdrawal(appToWithdraw);
        }
    }

    // =================== Company Representative Menu ===================
    private static void companyRepMenu() {
        String id = consoleUI.getInput("Enter Representative ID/Email: ");
            CompanyRepresentative rep = null;
            for (int i = 0; i < companyReps.size(); i++) {
                CompanyRepresentative r = companyReps.get(i);
                if (r.getUserID().equals(id)) {
                    rep = r;
                    break; 
                }
            }

            if (rep == null) {
                System.out.println("Representative not found. Registering new representative...");

                String name = consoleUI.getInput("Enter name: ");
                String company = consoleUI.getInput("Enter company: ");
                String dept = consoleUI.getInput("Enter department: ");
                String pos = consoleUI.getInput("Enter position: ");
                rep = new CompanyRepresentative(id, name, company, dept, pos, internshipViewer);
                companyReps.add(rep);
        
                System.out.println("Registered successfully! Waiting for staff approval.");
                return;
            }

        if (!rep.isApproved()) {
            System.out.println("Account not yet approved by staff. Please wait for approval.");
            return;
        }

        String pass = consoleUI.getInput("Enter password: ");
        if (!rep.login(id, pass)) {
            System.out.println("Incorrect password.");
            return;
        }

        UserSession session = SessionManager.getSession(rep.getUserID());
        if (session == null) {
            session = SessionManager.createSession(rep);
        }

        System.out.println("Welcome " + rep.getName() + " from " + rep.getCompany() + "!");

        while (true) {
            System.out.println("\n=== Company Representative Menu - " + rep.getName() + " ===");
            System.out.println("1. Create Internship Opportunity");
            System.out.println("2. Edit Internship (before approval)");
            System.out.println("3. Toggle Internship Visibility");
            System.out.println("4. Review Student Applications");
            System.out.println("5. View My Internships");
            System.out.println("6. Change Password");
            System.out.println("7. Delete Internship(before approval)");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            String choice = consoleUI.getInput("");

            switch (choice) {
                case "1":
                    createInternship(rep);
                    break;
                case "2":
                    rep.editInternshipBeforeApproval();
                    break;
                case "3":
                    rep.toggleVisibility();
                    break;
                case "4":
                    rep.reviewStudentApplications();
                    break;
                case "5":
                    companyRepViewInternships(rep, session);
                    break;
                case "6":
                    changePassword(rep);
                    break;
                case "7":
                    deleteInternship(rep);
                    break;
                case "8":
                    SessionManager.removeSession(rep.getUserID());
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            
            consoleUI.waitForEnter();
        }
    }

    private static void createInternship(CompanyRepresentative rep) {
        System.out.println("Create New Internship Opportunity");
        
        String title = consoleUI.getInput("Title: ");
        String desc = consoleUI.getInput("Description: ");
        String level = consoleUI.getInput("Level (Basic/Intermediate/Advanced): ");
        String major = consoleUI.getInput("Preferred Major: ");
        
        LocalDate openDate, closeDate;
        try {
            openDate = LocalDate.parse(consoleUI.getInput("Open Date (yyyy-mm-dd): "));
            closeDate = LocalDate.parse(consoleUI.getInput("Close Date (yyyy-mm-dd): "));
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-mm-dd.");
            return;
        }
        
        int slots;
        try {
            slots = Integer.parseInt(consoleUI.getInput("Slots (max 10): "));
            if (slots > 10) slots = 10;
            if (slots < 1) slots = 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using default: 1 slot.");
            slots = 1;
        }

        if (consoleUI.confirmAction("Create internship '" + title + "'?")) {
            rep.createInternshipOpportunity(title, desc, level, major, openDate, closeDate, slots);
        }
    }

    private static void companyRepViewInternships(CompanyRepresentative rep, UserSession session) {
        List<InternshipOpportunity> myInternships = rep.getInternships();
        if (myInternships.isEmpty()) {
            System.out.println("You have no internships.");
            return;
        }
        System.out.println("Your Internships:");
        rep.viewInternshipsWithFilter(myInternships, session.getCurrentFilter());
    }

    private static void deleteInternship(CompanyRepresentative rep) {
        rep.deleteInternship();
    }


    // =================== Staff Menu ===================
    private static void staffMenu() {
        String id = consoleUI.getInput("Enter Staff ID: ");
        CareerCenterStaff staff = staffList.stream()
                .filter(s -> s.getUserID().equals(id))
                .findFirst()
                .orElse(null);

        if (staff == null) {
            System.out.println("Staff not found.");
            return;
        }

        String pass = consoleUI.getInput("Enter password: ");
        if (!staff.login(id, pass)) {
            System.out.println("Incorrect password.");
            return;
        }
        UserSession session = SessionManager.getSession(staff.getUserID());
        if(session == null){
            session = SessionManager.createSession(staff);
        }
        System.out.println("Welcome " + staff.getName() + "!");

        while (true) {
            System.out.println("\n=== Staff Menu - " + staff.getName() + " ===");
            System.out.println("1. Approve Company Representatives");
            System.out.println("2. Approve Internships");
            System.out.println("3. View Approved Internships");
            System.out.println("4. Approve Student Withdrawals");
            System.out.println("5. View All Internships");
            System.out.println("6. Filter Internships");
            System.out.println("7. View Company Rep Statistics");
            System.out.println("8. View Internship Statistics");
            System.out.println("9. Change password");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");

            String choice = consoleUI.getInput("");

            switch (choice) {
                case "1":
                    staff.approveCompanyRepresentatives(companyReps);
                    break;
                case "2":
                    staff.approveInternships(companyReps);
                    break;
                case "3":
                    staff.viewApprovedInternships(companyReps);
                    break;
                case "4":
                    staff.processWithdrawals(students);
                    break;
                case "5":
                    staffViewInternships(staff,session);
                    break;
                case "6":
                    staffFilterInternships(staff,session);
                    break;
                case "7":
                    staff.viewCompanyRepStats(companyReps);
                    break;
                case "8":
                    staff.viewInternshipStats(companyReps);
                    break;
                case "9":
                    changePassword(staff);
                    break;
                case "10":
                    SessionManager.removeSession(staff.getUserID());
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            
            consoleUI.waitForEnter();
        }
    }

    private static void staffFilterInternships(CareerCenterStaff staff, UserSession session) {
        List<InternshipOpportunity> allInternships = internshipService.getAllInternships(companyReps);
    
        if (allInternships.isEmpty()) {
            System.out.println("No internships available in the system.");
            return;
        }
    
        FilterManager staffFilterManager = new FilterManagementService(null);
    
        staffFilterManager.filterInternships(allInternships, filterService, session.getCurrentFilter());
    
        System.out.println("\n=== Filtered Internships (Staff View) ===");
        staff.viewInternshipsWithFilter(allInternships, session.getCurrentFilter());
    }
    private static void staffViewInternships(CareerCenterStaff staff,UserSession session) {
        List<InternshipOpportunity> allInternships = internshipService.getAllInternships(companyReps);
        InternshipFilter filter = new InternshipFilter();
        
        if (allInternships.isEmpty()) {
            System.out.println("No internships in the system.");
            return;
        }
        
        System.out.println("All Internships in System:");
        staff.viewInternshipsWithFilter(allInternships, filter);
    }

    // =================== Helper Methods ===================
    private static void changePassword(User user) {
        String newPassword = consoleUI.getInput("Enter new password: ");
        if (newPassword.length() < 3) {
            System.out.println("Password must be at least 3 characters long.");
            return;
        }
        user.changePassword(newPassword);
        System.out.println("Password changed successfully!");
    }

    private static void showSystemStatistics() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Total Students: " + students.size());
        System.out.println("Total Staff: " + staffList.size());
        System.out.println("Total Company Representatives: " + companyReps.size());
        
        int totalInternships = internshipService.getAllInternships(companyReps).size();
        int pendingInternships = internshipService.getPendingInternships(companyReps).size();
        int approvedInternships = internshipService.getApprovedInternships(companyReps).size();
        
        System.out.println("Total Internships: " + totalInternships);
        System.out.println(" - Pending: " + pendingInternships);
        System.out.println(" - Approved: " + approvedInternships);
        
        SessionManager.printSessionStats();
        consoleUI.waitForEnter();
    }
}
