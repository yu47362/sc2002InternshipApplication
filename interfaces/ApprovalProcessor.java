package interfaces;

import models.CompanyRepresentative;
import models.Student;
import java.util.List;

public interface ApprovalProcessor {
    void approveCompanyRepresentatives(List<CompanyRepresentative> reps);
    void approveInternships(List<CompanyRepresentative> reps);
    void processWithdrawals(List<Student> students);
}