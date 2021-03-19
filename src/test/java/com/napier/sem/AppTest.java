package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void displaySalariesTestNull()
    {
        app.displaySalaries(null);
    }

    @Test
    void displaySalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        app.displaySalaries(employees);
    }

    @Test
    void displaySalariesTestContainsNull()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        employees.add(null);
        app.displaySalaries(employees);
    }

    @Test
    void displaySalaries()
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.displaySalaries(employees);
    }
}