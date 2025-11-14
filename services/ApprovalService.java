package services;

import models.CompanyRepresentative;
import models.InternshipOpportunity;
import models.InternshipApplication;

public class ApprovalService {
    
    public void approveCompanyRepresentative(CompanyRepresentative rep) {
        if (rep.isApproved()) {
            System.out.println("Company representative " + rep.getName() + " is already approved.");
            return;
        }
        rep.setApproved(true);
        System.out.println("Approved company representative: " + rep.getName() + " (" + rep.getUserID() + ")");
    }
    
    public void approveInternship(InternshipOpportunity opp) {
        if ("Approved".equalsIgnoreCase(opp.getStatus())) {
            System.out.println("Internship '" + opp.getTitle() + "' is already approved.");
            return;
        }
        opp.setStatus("Approved");
        opp.setVisible(true);
        System.out.println("Approved internship: " + opp.getTitle() + " | Company: " + opp.getCompany());
    }
    
    public void rejectInternship(InternshipOpportunity opp) {
        opp.setStatus("Rejected");
        opp.setVisible(false);
        System.out.println("Rejected internship: " + opp.getTitle() + " | Company: " + opp.getCompany());
    }
    
    public void approveWithdrawal(InternshipApplication app) {
        if ("Withdrawn".equalsIgnoreCase(app.getStatus())) {
            System.out.println("Application is already withdrawn.");
            return;
        }
        app.withdraw();
        System.out.println("Withdrawal approved for student: " + app.getStudent().getName()
                + " | Internship: " + app.getInternship().getTitle());
    }
    
    public void toggleInternshipVisibility(InternshipOpportunity opp) {
        if (!"Approved".equalsIgnoreCase(opp.getStatus())) {
            System.out.println("Cannot toggle visibility for unapproved internship.");
            return;
        }
        opp.setVisible(!opp.isVisible());
        System.out.println("Internship '" + opp.getTitle() + "' visibility set to: " + 
                          (opp.isVisible() ? "Visible" : "Hidden"));
    }
}