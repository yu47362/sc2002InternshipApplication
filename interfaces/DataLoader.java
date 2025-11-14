package interfaces;

import java.util.List;

public interface DataLoader<T> {
    List<T> readFromCSV(String filename);
}