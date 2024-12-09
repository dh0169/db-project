import java.util.List;

public interface DAO<T> {
    T get(int id); // Retrieve by ID
    List<T> getAll(); // Retrieve all records
    void save(T t); // Insert a new record
    void update(T t); // Update an existing record
    void delete(int id); // Delete by ID
}
