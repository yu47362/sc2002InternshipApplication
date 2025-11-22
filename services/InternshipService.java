package services;

import models.Student;
import models.CompanyRepresentative;
import models.InternshipOpportunity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * This is to check if the internship posted matches required student attribute
 * @author Dai Jiayu
 * @version 1.0
 */
public class InternshipService {
    
    public List<InternshipOpportunity> getAvailableInternshipsForStudent(Student student, List<CompanyRepresentative> reps) {
        List<InternshipOpportunity> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (CompanyRepresentative r : reps) {
            for (InternshipOpportunity i : r.getInternships()) {
                if(!r.isApproved()){
                    continue;
                }
                if (i.isVisible() && "Approved".equalsIgnoreCase(i.getStatus()) &&
                    !today.isBefore(i.getOpenDate()) && !today.isAfter(i.getCloseDate()) &&
                    isMajorMatch(i, student)&&
                    isLevelEligible(i,student)) {
                    list.add(i);
                }
            }
        }
        return list;
    }
    
    private boolean isMajorMatch(InternshipOpportunity internship, Student student) {
        String preferredMajor = internship.getPreferredMajor();
        return preferredMajor == null || preferredMajor.isEmpty() ||
               preferredMajor.equalsIgnoreCase(student.getMajor());
    }
    
    private boolean isLevelEligible(InternshipOpportunity internship, Student student) {
        String level = internship.getLevel();
        int studentYear = student.getYear();
    
        if (level == null) {
            return true; // If no level specified, assume eligible
        }
    
        // Level eligibility rules:
        if ("Basic".equalsIgnoreCase(level)) {
            // Basic level: Open to all years (1-4)
            return true;
        } else if ("Intermediate".equalsIgnoreCase(level)) {
            // Intermediate level: Year 3-4 only
            return studentYear >= 3;
        } else if ("Advanced".equalsIgnoreCase(level)) {
            // Advanced level: Year 4 only
            return studentYear == 4;
        }
    
        return true;
    }
    public List<InternshipOpportunity> getAllInternships(List<CompanyRepresentative> reps) {
        List<InternshipOpportunity> allInternships = new ArrayList<>();
        for (CompanyRepresentative rep : reps) {
            allInternships.addAll(rep.getInternships());
        }
        return allInternships;
    }
    
    public List<InternshipOpportunity> getPendingInternships(List<CompanyRepresentative> reps) {
        List<InternshipOpportunity> pending = new ArrayList<>();
        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity opp : rep.getInternships()) {
                if ("Pending".equalsIgnoreCase(opp.getStatus())) {
                    pending.add(opp);
                }  
            }
        }
        return pending;
    }
    
    public List<InternshipOpportunity> getApprovedInternships(List<CompanyRepresentative> reps) {
        List<InternshipOpportunity> approved = new ArrayList<>();
        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity opp : rep.getInternships()) {
                if ("Approved".equalsIgnoreCase(opp.getStatus())) {
                    approved.add(opp);
                }
            }
        }
        return approved;
    }
    
    public InternshipOpportunity findInternshipByTitle(List<CompanyRepresentative> reps, String title) {
        for (CompanyRepresentative rep : reps) {
            for (InternshipOpportunity opp : rep.getInternships()) {
                if (opp.getTitle().equalsIgnoreCase(title)) {
                    return opp;  
                }
            }
        }
        return null;
    }

}
