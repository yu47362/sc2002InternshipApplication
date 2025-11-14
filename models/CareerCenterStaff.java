package models;

import interfaces.ApprovalProcessor;
import interfaces.InternshipViewer;
import services.ApprovalService;
import services.InternshipViewerService;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class CareerCenterStaff extends User implements ApprovalProcessor, InternshipViewer {
    private String department;
    private final ApprovalService approvalService;
    private final InternshipViewerService internshipViewer;

    public CareerCenterStaff(String userID, String name, String department,
                           InternshipViewerService internshipViewer) {
        super(userID, name);
        this.department = department;
        this.approvalService = new ApprovalService();
        this.internshipViewer = internshipViewer;
    }

    public String getDepartment() { return department; }

    // ApprovalProcessor implementation
    @Override
    public void approveCompanyRepresentatives(List<CompanyRepresentative> reps) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            List<CompanyRepresentative> pending = reps.stream()
                    .filter(r -> !r.isApproved())
                    .toList();

            if (pending.isEmpty()) {
                System.out.println("No pending company representatives.");
                break;
            }

            System.out.println("\n--- Pending Company Representatives ---");
            for (int i = 0; i < pending.size(); i++) {
                CompanyRepresentative r = pending.get(i);
                System.out.println((i + 1) + ". " + r.getName() + " | Company: " + r.getCompany());
            }

            System.out.print("Enter number to approve (0 to cancel): ");
            int choice;
            try { 
                choice = Integer.parseInt(sc.nextLine().trim()); 
            } catch (Exception e) { 
                System.out.println("Invalid input.");
                continue; 
            }
            if (choice == 0) break;

            if (choice > 0 && choice <= pending.size()) {
                approvalService.approveCompanyRepresentative(pending.get(choice - 1));
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    @Override
    public void approveInternships(List<CompanyRepresentative> reps) {
        Scanner sc = new Scanner(System.in);

        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity i : rep.getInternships()) {
                if (!i.getStatus().equalsIgnoreCase("Pending")) continue;

                System.out.println("\n--- Internship Approval ---");
                i.displayInfo();
                System.out.print("Approve this internship? (y/n): ");
                String ans = sc.nextLine().trim();
                if (ans.equalsIgnoreCase("y")) {
                    approvalService.approveInternship(i);
                }
            }
        }
    }

    @Override
    public void processWithdrawals(List<Student> students) {
        Scanner sc = new Scanner(System.in);
        List<InternshipApplication> withdrawalRequests = new ArrayList<>();
        for (Student student : students){
            for (InternshipApplication app : student.getAppliedInternships()) {
                if (app.isWithdrawRequested()) {
                    withdrawalRequests.add(app);
                }
            }
        }
    
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No withdrawal requests found.");
            return;
        }
    
        System.out.println("\n=== Withdrawal Requests ===");
        System.out.println("Found " + withdrawalRequests.size() + " withdrawal request(s):");
    
        while (!withdrawalRequests.isEmpty()) {
            // Display current withdrawal requests
            System.out.println("\n--- Pending Withdrawal Requests ---");
            for (int i = 0; i < withdrawalRequests.size(); i++) {
                InternshipApplication app = withdrawalRequests.get(i);
                Student student = app.getStudent();
                InternshipOpportunity internship = app.getInternship();
            
                System.out.println((i + 1) + ". Student: " + student.getName() + 
                                " (" + student.getUserID() + ")" +
                                " | Internship: " + internship.getTitle() +
                                " | Company: " + internship.getCompany() +
                                " | Current Status: " + app.getStatus());
            }
        
            System.out.println("\nOptions:");
            System.out.println("1. Approve a withdrawal request");
            System.out.println("2. Reject a withdrawal request");
            System.out.println("3. Approve all pending requests");
            System.out.println("4. Exit withdrawal processing");
            System.out.print("Choose an option: ");
        
            String option = sc.nextLine().trim();
        
            switch (option) {
                case "1":
                    approveSingleWithdrawal(withdrawalRequests, sc, true);
                    break;
                case "2":
                    approveSingleWithdrawal(withdrawalRequests, sc, false);
                    break;
                case "3":
                    approveAllWithdrawals(withdrawalRequests);
                    withdrawalRequests.clear();
                    System.out.println("All pending withdrawal requests have been approved.");
                    return;
                case "4":
                    System.out.println("Exiting withdrawal processing.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        

            for (int i = withdrawalRequests.size() - 1; i >= 0; i--) {
                if (!withdrawalRequests.get(i).isWithdrawRequested()) {
                    withdrawalRequests.remove(i);
                }
            }
        }
    
        System.out.println("All withdrawal requests processed.");
    }


    private void approveSingleWithdrawal(List<InternshipApplication> withdrawalRequests, Scanner sc, boolean approve) {
        if (withdrawalRequests.isEmpty()) {
            System.out.println("No withdrawal requests available.");
            return;
        }
    
        System.out.print("Enter the number of the request to " + (approve ? "approve" : "reject") + " (0 to cancel): ");
        try {
            int choice = Integer.parseInt(sc.nextLine().trim());
        
            if (choice == 0) {
                System.out.println("Cancelled.");
                return;
            }
        
            if (choice < 1 || choice > withdrawalRequests.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        
            InternshipApplication selectedApp = withdrawalRequests.get(choice - 1);
            Student student = selectedApp.getStudent();
            InternshipOpportunity internship = selectedApp.getInternship();
        
            System.out.println("\nSelected Request:");
            System.out.println("Student: " + student.getName() + " (" + student.getUserID() + ")");
            System.out.println("Internship: " + internship.getTitle() + " | Company: " + internship.getCompany());
            System.out.println("Current Status: " + selectedApp.getStatus());
        
            System.out.print("Are you sure you want to " + (approve ? "APPROVE" : "REJECT") + " this withdrawal? (y/n): ");
            String confirm = sc.nextLine().trim().toLowerCase();
        
            if (confirm.equals("y") || confirm.equals("yes")) {
                if (approve) {
                    approvalService.approveWithdrawal(selectedApp);
                    student.removeApplication(selectedApp);
                    System.out.println(" Withdrawal approved for " + student.getName());
                } else {
                    selectedApp.setWithdrawRequested(false);
                    selectedApp.setStatus("Pending"); // Reset to pending status
                    System.out.println(" Withdrawal rejected for " + student.getName() + ". Application reset to pending status.");
                }
            } else {
                System.out.println("Operation cancelled.");
            }
        
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }


    private void approveAllWithdrawals(List<InternshipApplication> withdrawalRequests) {
        for (InternshipApplication app : withdrawalRequests) {
            approvalService.approveWithdrawal(app);
            app.getStudent().removeApplication(app);
            System.out.println("Approved withdrawal for " + app.getStudent().getName() + 
                            " from " + app.getInternship().getTitle());
        }
    }

    @Override
    public void viewInternshipsWithFilter(List<InternshipOpportunity> internships, InternshipFilter filter) {
        internshipViewer.viewInternshipsWithFilter(internships, filter, getViewerType());
    }
    
    @Override
    public String getViewerType() {
        return "staff";
    }

    public void viewApprovedInternships(List<CompanyRepresentative> reps) {
        System.out.println("\n--- Approved Internships ---");
        boolean found = false;

        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity i : rep.getInternships()) {
                if ("Approved".equalsIgnoreCase(i.getStatus())) {
                    i.displayInfo();
                    found = true;
                }
            }
        }

        if (!found) System.out.println("No approved internships found.");
    }

    // View pending internships
    public void viewPendingInternships(List<CompanyRepresentative> reps) {
        System.out.println("\n--- Pending Internships ---");
        boolean found = false;

        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity i : rep.getInternships()) {
                if ("Pending".equalsIgnoreCase(i.getStatus())) {
                    i.displayInfo();
                    found = true;
                }
            }
        }

        if (!found) System.out.println("No pending internships found.");
    }

    public void viewAllInternships(List<CompanyRepresentative> reps) {
        System.out.println("\n--- All Internships (Staff View) ---");
        boolean found = false;

        for (CompanyRepresentative rep : reps) {
            if (rep.getInternships().isEmpty()) continue;
            
            System.out.println("\nCompany: " + rep.getCompany() + " | Rep: " + rep.getName());
            for (InternshipOpportunity i : rep.getInternships()) {
                i.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No internships found in the system.");
    }

    public void viewCompanyRepStats(List<CompanyRepresentative> reps) {
        System.out.println("\n=== Company Representatives Statistics ===");
    
        int totalReps = reps.size();
        int approvedReps = 0;
    
        for (CompanyRepresentative rep : reps) {
            if (rep.isApproved()) {
                approvedReps++;
            }
        }
    
        int pendingReps = totalReps - approvedReps;
    
        System.out.println("Total Representatives: " + totalReps);
        System.out.println("Approved: " + approvedReps);
        System.out.println("Pending: " + pendingReps);
    
        if (!reps.isEmpty()) {
            System.out.println("\nDetailed List:");
            for (CompanyRepresentative rep : reps) {
                String status = rep.isApproved() ? "Approved" : "Pending";
                System.out.println("- " + rep.getName() + " (" + rep.getCompany() + ") - " + status);
            }
        }
    }

    // View internship statistics
    public void viewInternshipStats(List<CompanyRepresentative> reps) {
        System.out.println("\n=== Internship Statistics ===");
        int totalInternships = 0;
        int pendingInternships = 0;
        int approvedInternships = 0;
        int visibleInternships = 0;

        for (CompanyRepresentative rep : reps) {
            totalInternships += rep.getInternships().size();
            for (InternshipOpportunity opp : rep.getInternships()) {
                if ("Pending".equalsIgnoreCase(opp.getStatus())) {
                    pendingInternships++;
                } else if ("Approved".equalsIgnoreCase(opp.getStatus())) {
                    approvedInternships++;
                    if (opp.isVisible()) {
                        visibleInternships++;
                    }
                }
            }
        }

        System.out.println("Total Internships: " + totalInternships);
        System.out.println("Pending Approval: " + pendingInternships);
        System.out.println("Approved: " + approvedInternships);
        System.out.println("Visible to Students: " + visibleInternships);
    }

    // Reject an internship (staff can reject pending internships)
    public void rejectInternship(List<CompanyRepresentative> reps) {
        Scanner sc = new Scanner(System.in);
        List<InternshipOpportunity> pendingInternships = new ArrayList<>();

        // Collect all pending internships
        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity opp : rep.getInternships()) {
                if ("Pending".equalsIgnoreCase(opp.getStatus())) {
                    pendingInternships.add(opp);
                }
            }
        }

        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to reject.");
            return;
        }

        System.out.println("\n--- Pending Internships for Rejection ---");
        for (int i = 0; i < pendingInternships.size(); i++) {
            InternshipOpportunity opp = pendingInternships.get(i);
            System.out.println((i + 1) + ". " + opp.getTitle() + " | Company: " + opp.getCompany());
        }

        System.out.print("Enter number to reject (0 to cancel): ");
        int choice;
        try { 
            choice = Integer.parseInt(sc.nextLine().trim()); 
        } catch (Exception e) { 
            System.out.println("Invalid input.");
            return; 
        }
        
        if (choice == 0) return;

        if (choice > 0 && choice <= pendingInternships.size()) {
            InternshipOpportunity selected = pendingInternships.get(choice - 1);
            System.out.print("Are you sure you want to reject '" + selected.getTitle() + "'? (y/n): ");
            String confirm = sc.nextLine().trim();
            if (confirm.equalsIgnoreCase("y")) {
                approvalService.rejectInternship(selected);
            } else {
                System.out.println("Rejection cancelled.");
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    // Display staff information
    public void displayStaffInfo() {
        System.out.println("\n=== Staff Information ===");
        System.out.println("Name: " + getName());
        System.out.println("Staff ID: " + getUserID());
        System.out.println("Department: " + department);
    }
}