package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;

public interface FilterManager {
    void filterInternships(List<InternshipOpportunity> allInternships,
                                    InternshipFilterService filterService,
                                    InternshipFilter currentFilter);
}