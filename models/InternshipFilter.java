package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class InternshipFilter {
    private String status;
    private String level;
    private LocalDate closingDateBefore;
    private LocalDate closingDateAfter;
    private String company;
    private String sortBy = "title"; 
    private boolean sortAscending = true;
    
    public InternshipFilter() {
        
    }
    
    // ===== Getters and Setters =====
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public LocalDate getClosingDateBefore() { return closingDateBefore; }
    public void setClosingDateBefore(LocalDate closingDateBefore) { this.closingDateBefore = closingDateBefore; }
    
    public LocalDate getClosingDateAfter() { return closingDateAfter; }
    public void setClosingDateAfter(LocalDate closingDateAfter) { this.closingDateAfter = closingDateAfter; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    
    public boolean isSortAscending() { return sortAscending; }
    public void setSortAscending(boolean sortAscending) { this.sortAscending = sortAscending; }
    

    public boolean hasActiveFilters() {
        return status != null || 
               level != null ||
               closingDateBefore != null ||
               closingDateAfter != null ||
               company != null;
    }
    

    public void clearFilters() {
        status = null;
        level = null;
        closingDateBefore = null;
        closingDateAfter = null;
        company = null;
        sortBy = "title";
        sortAscending = true;
    }
    

    public InternshipFilter copy() {
        InternshipFilter copy = new InternshipFilter();
        copy.setStatus(this.status);
        copy.setLevel(this.level);
        copy.setClosingDateBefore(this.closingDateBefore);
        copy.setClosingDateAfter(this.closingDateAfter);
        copy.setCompany(this.company);
        copy.setSortBy(this.sortBy);
        copy.setSortAscending(this.sortAscending);
        return copy;
    }
    

    public String getFilterSummary() {
        List<String> activeFilters = new ArrayList<>();
        
        if (status != null) activeFilters.add("Status: " + status);
        if (level != null) activeFilters.add("Level: " + level);
        if (closingDateAfter != null || closingDateBefore != null) {
            String dateRange = (closingDateAfter != null ? closingDateAfter.toString() : "Any") +
                             " to " +
                             (closingDateBefore != null ? closingDateBefore.toString() : "Any");
            activeFilters.add("Closing Date: " + dateRange);
        }
        if (company != null) activeFilters.add("Company: " + company);
        
        return activeFilters.isEmpty() ? "No active filters" : String.join(" | ", activeFilters);
    }
    
    @Override
    public String toString() {
        return "InternshipFilter{" +
                "status='" + status + '\'' +
                ", level='" + level + '\'' +
                ", closingDateBefore=" + closingDateBefore +
                ", closingDateAfter=" + closingDateAfter +
                ", company='" + company + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", sortAscending=" + sortAscending +
                '}';
    }
}
