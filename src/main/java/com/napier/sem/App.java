package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App
{
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect("db:3306");

        // Extract employee salary information
        ArrayList<Employee> employees = a.getAllSalaries();

        // Test the size of the returned data - should be 240124
        System.out.println(employees.size());

        // Print message saying whether the data is the correct size
        System.out.println((employees.size() == 240124) ? "Data is correct size" : "Data is not correct size");

        // Display employees
        a.displaySalaries(employees);

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 100;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(5000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" +  location + "/employees?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException e)
            {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(e.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Fetch an employee with a given ID from the database
     * @param ID employee's ID
     * @return The employee object
     */
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT e.emp_no, first_name, last_name, title, salary, dept_name, (" +
                            "SELECT CONCAT(first_name, ' ', last_name) " +
                            "FROM employees " +
                            "WHERE emp_no = m.emp_no) AS manager " +
                            "FROM employees e " +
                            "JOIN titles t ON e.emp_no = t.emp_no " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "JOIN departments d ON de.dept_no = d.dept_no " +
                            "JOIN dept_manager m ON d.dept_no = m.dept_no " +
                            "WHERE e.emp_no = " + ID + " " +
                            "AND t.to_date = '9999-01-01' " +
                            "AND s.to_date = '9999-01-01' " +
                            "AND de.to_date = '9999-01-01' " +
                            "AND m.to_date = '9999-01-01'";
            // Execute SQL statement
            ResultSet resultSet = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (resultSet.next())
            {
                Employee emp = new Employee();
                emp.emp_no = resultSet.getInt("e.emp_no");
                emp.first_name = resultSet.getString("first_name");
                emp.last_name = resultSet.getString("last_name");
                emp.title = resultSet.getString("title");
                emp.salary = resultSet.getInt("salary");
                emp.dept_name = resultSet.getString("dept_name");
                emp.manager = resultSet.getString("manager");
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /**
     * Fetches all employee records with current salary details from
     * employees database
     * @return An array of the employee records
     */
    public ArrayList<Employee> getAllSalaries()
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect = "SELECT e.emp_no, first_name, last_name, salary\n" +
                    "FROM employees e\n" +
                    "JOIN salaries s ON e.emp_no = s.emp_no\n" +
                    "WHERE s.to_date = '9999-01-01'\n" +
                    "ORDER BY e.emp_no";
            // Execute SQL statement
            ResultSet resultSet = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<>();
            while (resultSet.next())
            {
                Employee emp = new Employee();
                emp.emp_no = resultSet.getInt("e.emp_no");
                emp.first_name = resultSet.getString("first_name");
                emp.last_name = resultSet.getString("last_name");
                emp.salary = resultSet.getInt("salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }

    /**
     * Display an employee in the console
     * @param employee An employee object
     */
    public void displayEmployee(Employee employee)
    {
        if (employee != null)
        {
            System.out.println(
                    employee.first_name + " "
                            + employee.last_name + "\n"
                            + "Employee number: "
                            + employee.emp_no + "\n"
                            + "Job title: "+ employee.title + "\n"
                            + "Salary: £" + employee.salary + "\n"
                            + "Department: "
                            + employee.dept_name + "\n"
                            + "Manager: " + employee.manager + "\n");
        }
    }

    /**
     * Displays a list of employees and their salaries.
     * @param employees The list of employees to print
     */
    public void displaySalaries(ArrayList<Employee> employees)
    {
        // Check employees is not null
        if (employees == null)
        {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.printf("%-10s %-15s %-20s %-8s%n", "Emp No", "First Name", "Last Name", "Salary");
        // Loop over all employees in the list
        for (Employee emp : employees)
        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }
}
