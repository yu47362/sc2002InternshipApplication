package models;

import interfaces.StudentActions;
import interfaces.InternshipViewer;
import interfaces.InternshipFilterService;
import services.ApplicationService;
import services.InternshipViewerService;
import services.FilterManagementService;
import java.util.ArrayList;
import java.util.List;
/**
 * This is student class which will apply for internship
 * @author Dai Jiayu
 * @version 1.0
 */
public class Student extends User implements StudentActions, InternshipViewer {
    private int year;
    private String major;
    private String email;
    private List<InternshipApplication> applications;
    private InternshipApplication acceptedInternship;
    
    private final ApplicationService applicationService;
    private final InternshipViewerService internshipViewer;
    private final FilterManagementService filterManager;

    public Student(String userID, String name, int year, String major, String email,
                  InternshipViewerService internshipViewer) {
        super(userID, name);
        this.year = year;
        this.major = major;
        this.email = email;
        this.applications = new ArrayList<>();
        this.acceptedInternship = null;
        
        this.applicationService = new ApplicationService(this);
        this.internshipViewer = internshipViewer;
        this.filterManager = new FilterManagementService(this);
    }

    // ======== GETTERS ========
    public int getYear() { return year; }
    public String getMajor() { return major; }
    public String getEmail(){ return email;}
    public List<InternshipApplication> getAppliedInternships() { return applications; }
    public InternshipApplication getAcceptedInternship() { return acceptedInternship; }
    public void setAcceptedInternship(InternshipApplication acceptedInternship) { 
        this.acceptedInternship = acceptedInternship; 
    }

    // ======== APPLICATION MANAGEMENT ========
    @Override
    public void applyForInternship(InternshipOpportunity opp) {
        applicationService.applyForInternship(opp);
    }
/**
 * for accepting offer from a company
 */
    @Override
    public void acceptInternship(InternshipApplication app) {
        if (app == null) {
            System.out.println("No application selected.");
            return;
        }
        applicationService.acceptInternship(app);
    }

    @Override
    public void requestWithdrawal(InternshipApplication app) {
        applicationService.requestWithdrawal(app);
    }

    @Override
    public void viewApplicationStatus() {
        if (applications.isEmpty()) {
            System.out.println("No applications.");
            return;
        }
        System.out.println("\nApplications for " + getName() + ":");
        for (InternshipApplication a : applications) {
            System.out.println("- " + a.getInternship().getTitle() + " | Status: " + a.getStatus());
        }
    }

    // ======== INTERNSHIP VIEWING ========
    public void viewAvailableInternships(List<InternshipOpportunity> internships) {
        // Show all available internships
        System.out.println("\n=== Available Internships ===");
        boolean found = false;
        for (InternshipOpportunity i : internships) {
            if (i.isVisible() && i.getStatus().equalsIgnoreCase("Approved")) {
                i.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No available internships at the moment.");
    }
    
    public void viewAvailableInternshipsWithFilter(List<InternshipOpportunity> allInternships, 
                                                  InternshipFilterService filterService,
                                                  InternshipFilter filter) {
        internshipViewer.viewInternshipsWithFilter(allInternships, filter, getViewerType());
    }
    
    public void viewMyApplicationsDetailed() {
        internshipViewer.viewMyApplicationsDetailed(this);  // FIXED: Added 'this' parameter
    }

    // ======== FILTER MANAGEMENT ========
    public void filterInternships(List<InternshipOpportunity> allInternships,
                                           InternshipFilterService filterService,
                                           InternshipFilter currentFilter) {
        filterManager.filterInternships(allInternships, filterService, currentFilter);
    }

    // ======== INTERNSHIP VIEWER INTERFACE ========
    @Override
    public void viewInternshipsWithFilter(List<InternshipOpportunity> internships, InternshipFilter filter) {
        internshipViewer.viewInternshipsWithFilter(internships, filter, getViewerType());
    }
    
    @Override
    public String getViewerType() {
        return "student";
    }

    // ======== REMOVE APPLICATION ========
    public void removeApplication(InternshipApplication app) {
        applications.remove(app);
        if (acceptedInternship == app) acceptedInternship = null;
        System.out.println("Application removed for internship: " + app.getInternship().getTitle());
    }

}


