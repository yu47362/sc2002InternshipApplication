package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;

public interface InternshipFilterService {
    List<InternshipOpportunity> filterInternships(List<InternshipOpportunity> internships, InternshipFilter filter);
    List<String> getAvailableLevels(List<InternshipOpportunity> internships);
    List<String> getAvailableCompanies(List<InternshipOpportunity> internships);
    List<String> getAvailableStatuses(List<InternshipOpportunity> internships);
}