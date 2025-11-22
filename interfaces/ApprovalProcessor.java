package interfaces;

import models.CompanyRepresentative;
import models.Student;
import java.util.List;
/**
 * This is to call method of approve company rep, approve internship or process withdrawal
 * @author Dai Jiayu
 * @version 1.0
 */
public interface ApprovalProcessor {
    void approveCompanyRepresentatives(List<CompanyRepresentative> reps);
    void approveInternships(List<CompanyRepresentative> reps);
    void processWithdrawals(List<Student> students);

}
