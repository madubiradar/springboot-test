package com.sprigboot.test.service;

import com.sprigboot.test.exception.ResourceFoundException;
import com.sprigboot.test.model.Employee;
import com.sprigboot.test.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    Employee employee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();
    }
    @Test
    public void testSaveEmployee() {

        //given
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(savedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());

    }

    @Test
    public void testThrowsException_whenSaveWithExistingEmail(){

        //given
        Mockito.when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        org.junit.jupiter.api.Assertions.assertThrows(ResourceFoundException.class, ()->{
            employeeService.saveEmployee(employee);
        });
        Mockito.verify(employeeRepository, Mockito.never()).save(employee);
    }

    @Test
    public void testAllEmployees(){

        Employee employee1 = Employee.builder()
                .firstName("Pamela")
                .lastName("Jacob")
                .email("Pamela.Jacob@gmail.com")
                .build();

        Mockito.when(employeeRepository.findAll()).thenReturn(List.of(employee, employee1));

        List<Employee> employees = employeeService.getAllEmployees();
        Assertions.assertThat(employees).isNotNull();
        Assertions.assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    public void testGetAllEmployeesEmpty(){

        Mockito.when(employeeRepository.findAll()).thenReturn(List.of());

        List<Employee> employeeList = employeeService.getAllEmployees();

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    @Test
    public void testGetEmployeeById(){

        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Employee existingEmployee = employeeService.getEmployeeById(employee.getId()).orElse(null);

        assertThat(existingEmployee).isNotNull();
        assertThat(existingEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(existingEmployee.getLastName()).isEqualTo(employee.getLastName());

    }

    @Test
    public void testUpdateEmployee(){

        Mockito.when(employeeRepository.save(employee)).thenReturn(employee);

        Employee savedEmployee = employeeService.updateEmployee(employee);

        Assertions.assertThat(employee.getId()).isEqualTo(savedEmployee.getId());
        Assertions.assertThat(employee.getFirstName()).isEqualTo(savedEmployee.getFirstName());
        Assertions.assertThat(employee.getLastName()).isEqualTo(savedEmployee.getLastName());

    }

    @Test
    public void testDeleteEmployee(){
        Mockito.doNothing().when(employeeRepository).deleteById(employee.getId());

        employeeService.deleteEmployee(employee.getId());

        Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(employee.getId());

    }
}
