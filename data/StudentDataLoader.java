package data;

import interfaces.DataLoader;
import models.Student;
import services.InternshipViewerService;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class StudentDataLoader implements DataLoader<Student> {
    private final InternshipViewerService internshipViewer;
    
    public StudentDataLoader(InternshipViewerService internshipViewer) {
        this.internshipViewer = internshipViewer;
    }
    
    @Override
    public List<Student> readFromCSV(String filename) {
        List<Student> students = new ArrayList<>();
        int successCount = 0;
        int errorCount = 0;
        
        System.out.println("Loading students from: " + filename);
        
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
                    String id = values[0].trim();
                    String name = values[1].trim();
                    String major = values[2].trim();
                    int year = Integer.parseInt(values[3].trim());
                    String email = values[4].trim();

                    // Validate data
                    if (id.isEmpty() || name.isEmpty() || major.isEmpty() || email.isEmpty()) {
                        System.out.println("Warning: Line " + lineNumber + " has empty fields: " + line);
                        errorCount++;
                        continue;
                    }
                    
                    if (year < 1 || year > 4) {
                        System.out.println("Warning: Line " + lineNumber + " has invalid year: " + year);
                        errorCount++;
                        continue;
                    }

                    Student student = new Student(id, name, year, major, email, internshipViewer);
                    students.add(student);
                    successCount++;
                    
                } catch (NumberFormatException e) {
                    System.out.println("Error: Line " + lineNumber + " has invalid number format: " + line);
                    errorCount++;
                } catch (Exception e) {
                    System.out.println("Error processing line " + lineNumber + ": " + e.getMessage());
                    errorCount++;
                }
            }
            
            System.out.println("Student loading completed: " + successCount + " successful, " + errorCount + " errors.");
            
        } catch (Exception e) {
            System.out.println("Error reading student file: " + e.getMessage());
            e.printStackTrace();
        }
        
        return students;
    }
    
    // Static method for backward compatibility
    public static List<Student> readStudentsFromCSV(String filename, InternshipViewerService internshipViewer) {
        return new StudentDataLoader(internshipViewer).readFromCSV(filename);
    }
}