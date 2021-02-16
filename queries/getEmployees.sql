SELECT e.emp_no, first_name, last_name, title, salary, dept_name,
       (
        SELECT CONCAT(first_name, " ", last_name)
        FROM employees
        WHERE emp_no = m.emp_no
       ) AS manager
    FROM employees e
        JOIN titles t ON e.emp_no = t.emp_no
        JOIN salaries s ON e.emp_no = s.emp_no
        JOIN dept_emp de ON e.emp_no = de.emp_no
        JOIN departments d ON de.dept_no = d.dept_no
        JOIN dept_manager m ON d.dept_no = m.dept_no
    WHERE e.emp_no = 255530
        AND t.to_date = "9999-01-01"
        AND s.to_date = "9999-01-01"
        AND de.to_date = "9999-01-01"
        AND m.to_date = "9999-01-01";