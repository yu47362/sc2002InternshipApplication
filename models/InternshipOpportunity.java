package models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * This is Internship opportunity with its attributes
 * @author Dai Jiayu
 * @version 1.0
 */
public class InternshipOpportunity {
    private String title;
    private String description;
    private String level;
    private String preferredMajor;
    private LocalDate openDate;
    private LocalDate closeDate;
    private int slots;
    private String company;
    private boolean visible = false; // visible only after staff approval
    private String status = "Pending"; // Pending, Approved, Filled, Closed
    private List<InternshipApplication> applications = new ArrayList<>();

    public InternshipOpportunity(String title, String description, String level,
                                 String preferredMajor, LocalDate openDate, LocalDate closeDate,
                                 int slots, String company) {
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.slots = Math.min(slots, 10);
        this.company = company;
    }

    // ===== Getters =====
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLevel() { return level; }
    public String getPreferredMajor() { return preferredMajor; }
    public LocalDate getOpenDate() { return openDate; }
    public LocalDate getCloseDate() { return closeDate; }
    public int getSlots() { return slots; }
    public String getCompany() { return company; }
    public boolean isVisible() { return visible; }
    public String getStatus() { return status; }
    public List<InternshipApplication> getApplications() { return applications; }

    // ===== Setters =====
    public void setVisible(boolean visible) { this.visible = visible; }
    public void setStatus(String status) { this.status = status; }

    // ===== Application Management =====
    public void addApplication(InternshipApplication app) {
        applications.add(app);
    }

    public int getAcceptedCount() {
        int count = 0;
        for (InternshipApplication a : applications) {
            if ("Accepted".equalsIgnoreCase(a.getStatus())) count++;
        }
        return count;
    }

    public int getSlotsLeft() {
        return Math.max(0, slots - getAcceptedCount());
    }

    public boolean isApplicationOpen() {
        LocalDate today = LocalDate.now();
        return (!today.isBefore(openDate)) && (!today.isAfter(closeDate));
    }

    public void checkAndSetFilled() {
        if (getSlotsLeft() <= 0 && !"Filled".equalsIgnoreCase(status)) {
            setStatus("Filled");
            setVisible(false);
            System.out.println("Internship '" + title + "' is now filled.");
        }
    }

    // ===== Editing (Only before approval) =====
    public boolean editDetails(String newTitle, String newDesc, String newLevel,
                               String newMajor, LocalDate newOpen, LocalDate newClose, int newSlots) {
        if ("Pending".equalsIgnoreCase(status)) {
            this.title = newTitle;
            this.description = newDesc;
            this.level = newLevel;
            this.preferredMajor = newMajor;
            this.openDate = newOpen;
            this.closeDate = newClose;
            this.slots = Math.min(newSlots, 10);
            System.out.println("Internship details updated successfully!");
            return true;
        } else {
            System.out.println("Cannot edit internship after approval.");
            return false;
        }
    }

    // ===== Toggle Visibility =====
    public void toggleVisibility() {
        if ("Approved".equalsIgnoreCase(status)) {
            this.visible = !this.visible;
            System.out.println("Internship '" + title + "' visibility set to: " + (visible ? "Visible" : "Hidden"));
        } else {
            System.out.println("Visibility can only be toggled after staff approval.");
        }
    }

    // ===== Display Info =====
    public void displayInfo() {
        System.out.println("\nTitle: " + title);
        System.out.println("Description: " + description);
        System.out.println("Level: " + level);
        System.out.println("Preferred Major: " + preferredMajor);
        System.out.println("Open Date: " + openDate + " | Close Date: " + closeDate);
        System.out.println("Slots: " + slots + " | Company: " + company);
        System.out.println("Status: " + status + " | Visible: " + visible);
        System.out.println("Applications: " + applications.size());
    }
}


