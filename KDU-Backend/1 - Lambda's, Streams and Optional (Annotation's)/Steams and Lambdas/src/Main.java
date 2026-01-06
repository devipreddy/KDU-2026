//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Main {
    public static void main(String[] args) {

        List<Employee> employees = Employee.getSampleData();
        // Activity 1 - Question 1

        List<Employee> highPaidEngineers =
                Employee.filterEmployees(
                        employees,
                        e -> e.getSalary() > 70000 &&
                                e.getDepartment().equals("ENGINEERING")
                );

        // Activity 1 - Question 2

        List<String> standardizedNames =
                employees.stream()
                        .map(Employee::getName)
                        .map(String::toUpperCase)
                        .collect(Collectors.toList());

        // Activity 1 - Question 3

        double totalBudget =
                employees.stream()
                        .mapToDouble(Employee::getSalary)
                        .sum();
        System.out.println("Q-1");
        highPaidEngineers.forEach(System.out::println);
        System.out.println();
        System.out.println("Q-2");
        System.out.println(standardizedNames);
        System.out.println();
        System.out.println("Q-3");
        System.out.println("Total Annual Salary Budget = $" + totalBudget);

        System.out.println();

        System.out.println("Activity 2");
        System.out.println("Q-1");
        System.out.println(SafeLookup.safeFind("A100"));

        System.out.println();
        System.out.println("Search Fails:");
        System.out.println(SafeLookup.safeFind("XYZ"));

        Inventory inventory = Inventory.findItem("A100");

        Set<String> uniqueIds = InventoryUtils.extractUniqueIds(inventory);
        System.out.println();
        System.out.println("Q-2");
        System.out.println("Unique Item IDs: " + uniqueIds);

        System.out.println("Activity 3");
        System.out.println("Q-1&2");

        System.out.println("\n=== VALID CONFIG ===");
        SystemConfig valid = new SystemConfig(8, 2500);
        ConfigValidator.validate(valid);

        System.out.println("\n=== INVALID CONFIG ===");
        SystemConfig invalid = new SystemConfig(50, 10);

        try {
            ConfigValidator.validate(invalid);
        } catch (ConfigValidationException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}