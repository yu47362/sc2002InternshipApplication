package session;

import models.User;
import models.InternshipFilter;
import java.time.LocalDateTime;

public class UserSession {
    private User currentUser;
    private InternshipFilter currentFilter;
    private LocalDateTime loginTime;
    private LocalDateTime lastActivity;
    
    public UserSession(User user) {
        this.currentUser = user;
        this.currentFilter = new InternshipFilter();
        this.loginTime = LocalDateTime.now();
        this.lastActivity = LocalDateTime.now();
    }
    
    public User getCurrentUser() { 
        updateLastActivity();
        return currentUser; 
    }
    
    public InternshipFilter getCurrentFilter() { 
        updateLastActivity();
        return currentFilter; 
    }
    
    public void setCurrentFilter(InternshipFilter filter) { 
        this.currentFilter = filter;
        updateLastActivity();
    }
    
    public LocalDateTime getLoginTime() { 
        return loginTime; 
    }
    
    public LocalDateTime getLastActivity() { 
        return lastActivity; 
    }
    
    public void updateLastActivity() {
        this.lastActivity = LocalDateTime.now();
    }
    
    public long getSessionDurationMinutes() {
        return java.time.Duration.between(loginTime, LocalDateTime.now()).toMinutes();
    }
    
    public long getInactivityMinutes() {
        return java.time.Duration.between(lastActivity, LocalDateTime.now()).toMinutes();
    }
    
    public boolean isSessionExpired(long timeoutMinutes) {
        return getInactivityMinutes() > timeoutMinutes;
    }
    
    @Override
    public String toString() {
        return "UserSession{" +
                "user=" + currentUser.getName() +
                ", loginTime=" + loginTime +
                ", lastActivity=" + lastActivity +
                ", activeFilters=" + currentFilter.hasActiveFilters() +
                '}';
    }
}