

import java.util.List;
import java.util.ArrayList;


public class Employee {
    private String name;
    private int age;
    private double salary;
    private String department;

    public Employee(String name, int age, double salary, String department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }

    // Getters (needed for compliance/reporting)
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("Employee{Name='%s', Dept='%s', Salary=$%.2f, Age=%d}", name, department, salary, age);
    }

    // Test data method
    public static List<Employee> getSampleData() {
        return List.of(
                new Employee("Alice Johnson", 30, 85000.0, "ENGINEERING"),
                new Employee("Bob Smith", 45, 62000.0, "HR"),
                new Employee("Charlie Brown", 22, 91000.0, "ENGINEERING"),
                new Employee("David Lee", 58, 75000.0, "HR"),
                new Employee("Eve Wilson", 33, 55000.0, "SALES"),
                new Employee("Frank Miller", 55, 120000.0, "ENGINEERING")
        );

    }

    public static List<Employee> filterEmployees(List<Employee> employees,
                                                 EmployeeSelector selector) {

        List<Employee> result = new ArrayList<>();

        for (Employee e : employees) {
            if (selector.select(e)) {
                result.add(e);
            }
        }

        return result;
    }
}




