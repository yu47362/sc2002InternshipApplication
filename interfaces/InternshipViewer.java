package interfaces;

import models.InternshipOpportunity;
import models.InternshipFilter;
import java.util.List;
/**
 * This is to view the filtered internship information
 * @author Dai Jiayu
 * @version 1.0
 */
public interface InternshipViewer {
    void viewInternshipsWithFilter(List<InternshipOpportunity> internships, InternshipFilter filter);
    String getViewerType();

}
