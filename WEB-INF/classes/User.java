public class User {
    public int id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private boolean isAdmin;

    public User(int id, String name, String phone, String email, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    @Override
    public String toString() {
        return "User{" +
            "id=" + id + // No null check needed; primitives cannot be null
            ", name='" + (name != null ? name : "null") + '\'' +
            ", phone='" + (phone != null ? phone : "null") + '\'' +
            ", email='" + (email != null ? email : "null") + '\'' +
            ", isAdmin=" + isAdmin + // No null check needed; primitives cannot be null
            '}';
    }



}