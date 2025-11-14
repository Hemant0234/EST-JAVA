import java.sql.*;

// -------- Parent Class ----------
class Employee {
    int id;
    String name;
    double salary;

    Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Runtime Polymorphism (Overridden)
    void showDetails() {
        System.out.println("Employee: " + name + ", Salary: " + salary);
    }
}

// -------- Child Class 1 ----------
class Manager extends Employee {
    Manager(int id, String name, double salary) {
        super(id, name, salary);
    }

    @Override
    void showDetails() {
        System.out.println("Manager -> Name: " + name + ", Salary: " + salary);
    }
}

// -------- Child Class 2 ----------
class Developer extends Employee {
    Developer(int id, String name, double salary) {
        super(id, name, salary);
    }

    @Override
    void showDetails() {
        System.out.println("Developer -> Name: " + name + ", Salary: " + salary);
    }
}


// -------- Main Class with JDBC ----------
public class PolymorphismJDBC {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/company";
        String user = "root";
        String pass = "8586";

        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect with DB
            Connection con = DriverManager.getConnection(url, user, pass);

            String query = "SELECT id, name, salary, role FROM employee";
            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                String role = rs.getString("role");

                // --- Runtime Polymorphism based on role fetched from DB ---
                Employee emp;

                if (role.equalsIgnoreCase("Manager")) {
                    emp = new Manager(id, name, salary);
                } else if (role.equalsIgnoreCase("Developer")) {
                    emp = new Developer(id, name, salary);
                } else {
                    emp = new Employee(id, name, salary);
                }

                // Dynamic method dispatch
                emp.showDetails();
            }

            con.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
