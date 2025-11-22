package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;
/**
 * This is call on filter method
 * @author Dai Jiayu
 * @version 1.0
 */
public interface FilterManager {
    void filterInternships(List<InternshipOpportunity> allInternships,
                                    InternshipFilterService filterService,
                                    InternshipFilter currentFilter);

}
