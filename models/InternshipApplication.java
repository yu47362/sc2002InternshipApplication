package models;
/**
 * This is InternshipApplication that have important methods related to application such as apply
 * @author Dai Jiayu
 * @version 1.0
 */
public class InternshipApplication {
    private Student student;
    private InternshipOpportunity internship;
    private String status; // Pending, Offered, Accepted, Rejected, Withdrawn, Withdrawal Requested
    private boolean withdrawRequested;

    public InternshipApplication(Student student, InternshipOpportunity internship) {
        this.student = student;
        this.internship = internship;
        this.status = "Pending";
        this.withdrawRequested = false;
    }

    public Student getStudent() { return student; }
    public InternshipOpportunity getInternship() { return internship; }
    public String getStatus() { return status; }
    public boolean isWithdrawRequested() { return withdrawRequested; }

    // --- Application Management ---
    public void approve() {
        this.status = "Offered";
        System.out.println("Application approved for " + student.getName() +
                " | Internship: " + internship.getTitle());
    }

    public void reject() {
        this.status = "Rejected";
        System.out.println("Application rejected for " + student.getName() +
                " | Internship: " + internship.getTitle());
    }

    public void withdraw() {
        this.status = "Withdrawn";
        this.withdrawRequested = false;
        System.out.println(student.getName() + " has withdrawn from " + internship.getTitle());
    }

    public void setWithdrawRequested(boolean requested) {
        this.withdrawRequested = requested;
        if (requested) {
            System.out.println(student.getName() + " requested withdrawal from " + internship.getTitle());
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        // Students should still see active applications even if internship is invisible
        return !status.equalsIgnoreCase("Withdrawn")
                && !status.equalsIgnoreCase("Rejected");
    }
}

