SELECT e.emp_no, first_name, last_name, salary
FROM employees e
    JOIN salaries s ON e.emp_no = s.emp_no
WHERE s.to_date = '9999-01-01'
ORDER BY e.emp_no