package services;

import models.Student;
import models.InternshipOpportunity;
import models.InternshipApplication;
import java.util.ArrayList;

public class ApplicationService {
    private final Student student;
    
    public ApplicationService(Student student) {
        this.student = student;
    }
    
    public void applyForInternship(InternshipOpportunity opp) {
        if (!opp.isVisible() || !opp.getStatus().equalsIgnoreCase("Approved")) {
            System.out.println("Cannot apply: internship is not open for applications.");
            return;
        }
        if (!opp.isApplicationOpen()) {
            System.out.println("Cannot apply: application period closed.");
            return;
        }
        if (student.getAppliedInternships().size() >= 3) {
            System.out.println("Cannot apply for more than 3 internships at once.");
            return;
        }
        if ((student.getYear() == 1 || student.getYear() == 2) && !opp.getLevel().equalsIgnoreCase("Basic")) {
            System.out.println("Year 1 and 2 students may only apply for Basic-level internships.");
            return;
        }
        boolean already = student.getAppliedInternships().stream().anyMatch(a -> a.getInternship() == opp);
        if (already) {
            System.out.println("You already applied to this internship.");
            return;
        }

        InternshipApplication app = new InternshipApplication(student, opp);
        student.getAppliedInternships().add(app);
        opp.addApplication(app);
        System.out.println("Applied to internship: " + opp.getTitle());
    }
    
    public void acceptInternship(InternshipApplication app) {
        if (!student.getAppliedInternships().contains(app)) {
            System.out.println("You did not apply for this internship.");
            return;
        }
        if (!"Offered".equalsIgnoreCase(app.getStatus())) {
            System.out.println("Only 'Offered' applications can be accepted.");
            return;
        }

        student.setAcceptedInternship(app);
        app.setStatus("Accepted");
        System.out.println("You accepted: " + app.getInternship().getTitle());

        // Auto-reject other offers
        for (InternshipApplication other : new ArrayList<>(student.getAppliedInternships())) {
            if (other != app && "Offered".equalsIgnoreCase(other.getStatus())) {
                other.setStatus("Rejected");
                System.out.println("Auto-rejected: " + other.getInternship().getTitle());
            }
        }
    }
    
    public void requestWithdrawal(InternshipApplication app) {
        if (!student.getAppliedInternships().contains(app)) {
            System.out.println("Application not found.");
            return;
        }
        if ("Withdrawn".equalsIgnoreCase(app.getStatus())) {
            System.out.println("Already withdrawn.");
            return;
        }
        app.setWithdrawRequested(true);
        app.setStatus("Withdrawal Requested");
        System.out.println("Withdrawal request submitted for: " + app.getInternship().getTitle());
    }
}