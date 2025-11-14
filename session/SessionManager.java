package session;

import models.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {
    private static final Map<String, UserSession> activeSessions = new HashMap<>();
    private static final long SESSION_TIMEOUT_MINUTES = 30; 
    private static ScheduledExecutorService cleanupScheduler;
    
    static {
        startSessionCleanup();
    }
    
    public static UserSession createSession(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        UserSession session = new UserSession(user);
        activeSessions.put(user.getUserID(), session);
        System.out.println("Session created for user: " + user.getName());
        return session;
    }
    
    public static UserSession getSession(String userID) {
        UserSession session = activeSessions.get(userID);
        if (session != null) {
            session.updateLastActivity();
        }
        return session;
    }
    
    public static void removeSession(String userID) {
        UserSession session = activeSessions.remove(userID);
        if (session != null) {
            System.out.println("Session removed for user: " + session.getCurrentUser().getName());
        }
    }
    
    public static boolean hasActiveSession(String userID) {
        return activeSessions.containsKey(userID);
    }
    
    public static void updateSessionActivity(String userID) {
        UserSession session = getSession(userID);
        if (session != null) {
            session.updateLastActivity();
        }
    }
    
    public static int getActiveSessionCount() {
        return activeSessions.size();
    }
    
    public static void cleanupExpiredSessions() {
        int initialCount = activeSessions.size();
        activeSessions.entrySet().removeIf(entry -> 
            entry.getValue().isSessionExpired(SESSION_TIMEOUT_MINUTES)
        );
        int removedCount = initialCount - activeSessions.size();
        if (removedCount > 0) {
            System.out.println("Cleaned up " + removedCount + " expired sessions.");
        }
    }
    
    private static void startSessionCleanup() {
        cleanupScheduler = Executors.newScheduledThreadPool(1);
        cleanupScheduler.scheduleAtFixedRate(
            SessionManager::cleanupExpiredSessions,
            5, 5, TimeUnit.MINUTES 
        );
    }
    
    public static void shutdown() {
        if (cleanupScheduler != null) {
            cleanupScheduler.shutdown();
            try {
                if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    cleanupScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                cleanupScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        activeSessions.clear();
        System.out.println("Session manager shut down.");
    }
    
    public static void printSessionStats() {
        System.out.println("\n=== Session Statistics ===");
        System.out.println("Active sessions: " + getActiveSessionCount());
        activeSessions.forEach((userId, session) -> {
            System.out.println(" - " + session.getCurrentUser().getName() + 
                             " (ID: " + userId + ")" +
                             " | Duration: " + session.getSessionDurationMinutes() + "m" +
                             " | Inactive: " + session.getInactivityMinutes() + "m" +
                             " | Filters: " + (session.getCurrentFilter().hasActiveFilters() ? "Yes" : "No"));
        });
    }
}