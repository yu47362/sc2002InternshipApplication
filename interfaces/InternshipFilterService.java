package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;
/**
 * This is to get the internship with matching characteristic
 * @author Dai Jiayu
 * @version 1.0
 */
public interface InternshipFilterService {
    List<InternshipOpportunity> filterInternships(List<InternshipOpportunity> internships, InternshipFilter filter);
    List<String> getAvailableLevels(List<InternshipOpportunity> internships);
    List<String> getAvailableCompanies(List<InternshipOpportunity> internships);
    List<String> getAvailableStatuses(List<InternshipOpportunity> internships);
    List<String> getAvailablePreferredMajors(List<InternshipOpportunity> internships);
}

