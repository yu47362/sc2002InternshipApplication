package interfaces;

import models.InternshipOpportunity;

public interface UserInterface {
    void displayMessage(String message);
    String getInput(String prompt);
    int getChoice(String prompt, int maxChoice);
    void displayInternship(InternshipOpportunity internship);
}