package services;

import interfaces.InternshipFilterService;
import models.InternshipOpportunity;
import models.InternshipFilter;
import models.Student;
import models.InternshipApplication;
import java.util.List;
/**
 * This is to display internship information
 * @author Dai Jiayu
 * @version 1.0
 */
public class InternshipViewerService {
    private final InternshipFilterService filterService;
    
    public InternshipViewerService(InternshipFilterService filterService) {
        this.filterService = filterService;
    }
    
    public void viewInternshipsWithFilter(List<InternshipOpportunity> internships, 
                                        InternshipFilter filter,
                                        String viewerType) {
        List<InternshipOpportunity> filtered = filterService.filterInternships(internships, filter);
        
        System.out.println("\n=== Available Internships (" + viewerType + " View) ===");
        System.out.println("Active Filters: " + (filter.hasActiveFilters() ? "Yes" : "No"));
        System.out.println("Sort By: " + filter.getSortBy() + " (" + 
                          (filter.isSortAscending() ? "Ascending" : "Descending") + ")");
        System.out.println("Results: " + filtered.size() + " internships found\n");
        
        if (filtered.isEmpty()) {
            System.out.println("No internships match your current filters.");
            return;
        }
        
        for (InternshipOpportunity i : filtered) {
            displayInternshipForViewer(i, viewerType);
        }
    }
    
    private void displayInternshipForViewer(InternshipOpportunity internship, String viewerType) {
        switch (viewerType.toLowerCase()) {
            case "student":
                displayForStudent(internship);
                break;
            case "company":
                displayForCompany(internship);
                break;
            case "staff":
                displayForStaff(internship);
                break;
            default:
                internship.displayInfo();
        }
    }
    
    private void displayForStudent(InternshipOpportunity internship) {
        System.out.println("\nTitle: " + internship.getTitle());
        System.out.println("Company: " + internship.getCompany());
        System.out.println("Description: " + internship.getDescription());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Preferred Major: " + internship.getPreferredMajor());
        System.out.println("Open Date: " + internship.getOpenDate() + " | Close Date: " + internship.getCloseDate());
        System.out.println("Slots Left: " + internship.getSlotsLeft() + " / " + internship.getSlots());
        System.out.println("Status: " + internship.getStatus());
    }
    
    private void displayForCompany(InternshipOpportunity internship) {
        System.out.println("\nTitle: " + internship.getTitle());
        System.out.println("Status: " + internship.getStatus());
        System.out.println("Visible: " + internship.isVisible());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Preferred Major: " + internship.getPreferredMajor());
        System.out.println("Applications: " + internship.getApplications().size());
        System.out.println("Slots: " + internship.getSlots() + " | Filled: " + 
                          (internship.getSlots() - internship.getSlotsLeft()));
        System.out.println("Close Date: " + internship.getCloseDate());
    }
    
    private void displayForStaff(InternshipOpportunity internship) {
        System.out.println("\nTitle: " + internship.getTitle());
        System.out.println("Company: " + internship.getCompany());
        System.out.println("Status: " + internship.getStatus());
        System.out.println("Visible: " + internship.isVisible());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Applications: " + internship.getApplications().size());
        System.out.println("Slots: " + internship.getSlots() + " | Filled: " + 
                          (internship.getSlots() - internship.getSlotsLeft()));
        System.out.println("Close Date: " + internship.getCloseDate());
    }
    
    public void viewMyApplicationsDetailed(Student student) {
        System.out.println("\n=== My Internship Applications ===");
        if (student.getAppliedInternships().isEmpty()) {
            System.out.println("No applications submitted.");
            return;
        }
        
        for (InternshipApplication app : student.getAppliedInternships()) {
            InternshipOpportunity opp = app.getInternship();
            System.out.println("- " + opp.getTitle() + " (" + opp.getCompany() +
                    ") | Status: " + app.getStatus() +
                    " | Withdrawal Requested: " + app.isWithdrawRequested());
        }
    }

}
