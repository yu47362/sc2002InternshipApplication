package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;

public interface InternshipViewer {
    void viewInternshipsWithFilter(List<InternshipOpportunity> internships, InternshipFilter filter);
    String getViewerType();
}