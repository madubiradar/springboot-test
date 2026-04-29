package com.sprigboot.test.contract;

import com.sprigboot.test.model.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EmployeeApi {
    @PostMapping("/api/v1/employees")
    Employee createEmployee(@RequestBody Employee employee);

    @GetMapping("/api/v1/employees")
    List<Employee> getAllEmployees();
}