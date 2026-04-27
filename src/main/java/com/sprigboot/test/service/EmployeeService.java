package com.sprigboot.test.service;

import com.sprigboot.test.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {

    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long Id);
    Employee updateEmployee(Employee updateEmployee);
    void deleteEmployee(Long id);
}
