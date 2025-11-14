package data;

import interfaces.DataLoader;
import models.CareerCenterStaff;
import services.InternshipViewerService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StaffDataLoader implements DataLoader<CareerCenterStaff> {
    private final InternshipViewerService internshipViewer;
    
    public StaffDataLoader(InternshipViewerService internshipViewer) {
        this.internshipViewer = internshipViewer;
    }
    
    @Override
    public List<CareerCenterStaff> readFromCSV(String filename) {
        List<CareerCenterStaff> staffList = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        System.out.println("Loading staff from: " + filename);
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean first = true;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                
                if (first) { 
                    first = false; 
                    continue; 
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length < 5) {
                    System.out.println("Warning: Line " + lineNumber + " has insufficient data: " + line);
                    errorCount++;
                    continue;
                }

                try {
                    String staffID = values[0].trim();
                    String name = values[1].trim();
                    String department = values[3].trim();

                    // Validate data
                    if (staffID.isEmpty() || name.isEmpty() || department.isEmpty()) {
                        System.out.println("Warning: Line " + lineNumber + " has empty fields: " + line);
                        errorCount++;
                        continue;
                    }

                    CareerCenterStaff staff = new CareerCenterStaff(staffID, name, department, internshipViewer);
                    staffList.add(staff);
                    successCount++;
                    
                } catch (Exception e) {
                    System.out.println("Error processing line " + lineNumber + ": " + e.getMessage());
                    errorCount++;
                }
            }
            
            System.out.println("Staff loading completed: " + successCount + " successful, " + errorCount + " errors.");
            
        } catch (IOException e) {
            System.out.println("Error reading staff file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return staffList;
    }
    
    // Static method for backward compatibility
    public static List<CareerCenterStaff> readStaffFromCSV(String filename, InternshipViewerService internshipViewer) {
        return new StaffDataLoader(internshipViewer).readFromCSV(filename);
    }
}