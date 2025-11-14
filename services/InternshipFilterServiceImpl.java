package services;

import interfaces.InternshipFilterService;
import models.InternshipOpportunity;
import models.InternshipFilter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;

public class InternshipFilterServiceImpl implements InternshipFilterService {
    
    @Override
    public List<InternshipOpportunity> filterInternships(List<InternshipOpportunity> internships, InternshipFilter filter) {
        if (internships == null || internships.isEmpty()) {
            return new ArrayList<>(); 
        }
    
        List<InternshipOpportunity> filtered = new ArrayList<>();
        for (InternshipOpportunity opp : internships) {
            if (filterByStatus(opp, filter) && 
                filterByLevel(opp, filter) &&
                filterByClosingDate(opp, filter) &&
                filterByCompany(opp, filter)) {
                filtered.add(opp);
            }
        }
    
        filtered.sort((a, b) -> {
            if ("title".equals(filter.getSortBy())) {
                return a.getTitle().compareTo(b.getTitle());
            } else if ("company".equals(filter.getSortBy())) {
                return a.getCompany().compareTo(b.getCompany());
            }
            return 0;
        });
    
        return filtered;
    }
    
    private boolean filterByStatus(InternshipOpportunity opp, InternshipFilter filter) {
        return filter.getStatus() == null || 
               filter.getStatus().equalsIgnoreCase(opp.getStatus());
    }
    
    
    private boolean filterByLevel(InternshipOpportunity opp, InternshipFilter filter) {
        return filter.getLevel() == null || 
               filter.getLevel().equalsIgnoreCase(opp.getLevel());
    }
    
    private boolean filterByClosingDate(InternshipOpportunity opp, InternshipFilter filter) {
        LocalDate closeDate = opp.getCloseDate();
        boolean afterCondition = filter.getClosingDateAfter() == null || 
                               !closeDate.isBefore(filter.getClosingDateAfter());
        boolean beforeCondition = filter.getClosingDateBefore() == null || 
                                !closeDate.isAfter(filter.getClosingDateBefore());
        return afterCondition && beforeCondition;
    }
    
    private boolean filterByCompany(InternshipOpportunity opp, InternshipFilter filter) {
        return filter.getCompany() == null || 
               filter.getCompany().equalsIgnoreCase(opp.getCompany());
    }
    
    private Comparator<InternshipOpportunity> getComparator(InternshipFilter filter) {
        Comparator<InternshipOpportunity> comparator;
        
        switch (filter.getSortBy().toLowerCase()) {
            case "company":
                comparator = Comparator.comparing(InternshipOpportunity::getCompany);
                break;
            case "closingdate":
                comparator = Comparator.comparing(InternshipOpportunity::getCloseDate);
                break;
            case "level":
                comparator = Comparator.comparing(InternshipOpportunity::getLevel);
                break;
            case "status":
                comparator = Comparator.comparing(InternshipOpportunity::getStatus);
                break;
            case "title":
            default:
                comparator = Comparator.comparing(InternshipOpportunity::getTitle);
                break;
        }
        
        if (!filter.isSortAscending()) {
            comparator = comparator.reversed();
        }
        
        return comparator;
    }
    
    
    @Override
    public List<String> getAvailableLevels(List<InternshipOpportunity> internships) {
        List<String> result = new ArrayList<>();
    
        if (internships == null) return result;
    
        for (int i = 0; i < internships.size(); i++) {
            InternshipOpportunity opp = internships.get(i);
            String lvl = opp.getLevel();
        

            if (lvl == null || lvl.isEmpty()) continue;
        

            boolean duplicate = false;
            for (String item : result) {
                if (item.equals(lvl)) {
                    duplicate = true;
                    break;
                }
            }
        
            if (!duplicate) {
                result.add(lvl);
            }
        }
    
        if (result.size() > 0) {
            result.sort(String::compareTo);  
        }
    
        return result;
    }
    
    @Override
    public List<String> getAvailableCompanies(List<InternshipOpportunity> internships) {
        List<String> companies = new ArrayList<>();
    
        if (internships == null) {
            return companies;
        }
    
        for (InternshipOpportunity opp : internships) {
            String company = opp.getCompany();
            if (company != null && !company.trim().isEmpty()) {
                if (!companies.contains(company)) {
                    companies.add(company);
                }
            }
        }
    
        Collections.sort(companies);
        return companies;
    }
    @Override
    public List<String> getAvailableStatuses(List<InternshipOpportunity> internships) {
        List<String> result = new ArrayList<>();
    
        if (internships == null) {
            return result;  
        }
    
        for (InternshipOpportunity opp : internships) {
            String status = opp.getStatus();

            if (status == null || status.trim().length() == 0) {
                continue;
            }
            if (!result.contains(status)) {
                result.add(status);
            }
        }
    
        result.sort(String::compareTo);
        return result;
    }
}