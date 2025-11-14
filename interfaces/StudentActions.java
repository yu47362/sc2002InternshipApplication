package interfaces;

import models.InternshipOpportunity;
import models.InternshipApplication;

public interface StudentActions {
    void applyForInternship(InternshipOpportunity opp);
    void acceptInternship(InternshipApplication app);
    void requestWithdrawal(InternshipApplication app);
    void viewApplicationStatus();
}