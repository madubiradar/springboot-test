package com.sprigboot.test.repository;

import com.sprigboot.test.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();

    }


    @DisplayName("Test for save employee operation")
    @Test
    public void testSaveEmployee() {

        //given
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllEmployees() {
        Employee firstEmployee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        Employee secondEmployee = Employee.builder()
                .firstName("Nikita")
                .lastName("Das")
                .email("Nikita.das@gmail.com")
                .build();

        employeeRepository.saveAll(List.of(firstEmployee, secondEmployee));

        Iterable<Employee> employees = employeeRepository.findAll();
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(2);
        assertThat(employees).contains(firstEmployee, secondEmployee);
    }

    @Test
    public void testEmployeeById(){
        Employee saveEmployee = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(saveEmployee);

        Employee existingEmployee = employeeRepository.findById(saveEmployee.getId()).orElse(null);
        assertThat(existingEmployee).isNotNull();
        assertThat(existingEmployee.getId()).isEqualTo(saveEmployee.getId());
        assertThat(existingEmployee.getFirstName()).isEqualTo(saveEmployee.getFirstName());
        assertThat(existingEmployee.getLastName()).isEqualTo(saveEmployee.getLastName());
        assertThat(existingEmployee.getEmail()).isEqualTo(saveEmployee.getEmail());
    }

    @Test
    public void testGetEmployeeByEmail(){
        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        Employee employee = employeeRepository.save(save);

        Employee employee1 = employeeRepository.findByEmail(save.getEmail()).orElse(null);

        assertThat(employee1).isNotNull();
        assertThat(employee1.getId()).isEqualTo(employee.getId());
        assertThat(employee1.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(employee1.getLastName()).isEqualTo(employee.getLastName());
        assertThat(employee1.getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    public void testUpdateEmployee(){
        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(save);

        Employee existingEmployee = employeeRepository.findById(save.getId()).orElse(null);
        assertThat(existingEmployee).isNotNull();
        assertThat(existingEmployee.getId()).isEqualTo(save.getId());

        existingEmployee.setLastName("right");
        existingEmployee.setEmail("john.right@gmail.com");

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        assertThat(updatedEmployee.getFirstName()).isEqualTo("john");
        assertThat(updatedEmployee.getLastName()).isEqualTo("right");
        assertThat(updatedEmployee.getEmail()).isEqualTo("john.right@gmail.com");
    }

    @Test
    public void testDeleteEmployee(){
        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        Employee employee = employeeRepository.save(save);

        employeeRepository.delete(save);

        Employee existingEmployee = employeeRepository.findById(save.getId()).orElse(null);
        assertThat(existingEmployee).isNull();
    }

    @DisplayName("Test for custom query using JPQL with index parameters")
    @Test
    public void testFindByJPQL() {
        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(save);

        String firstName = "john";
        String lastName = "doe";

        Employee employee = employeeRepository.findByJPQL(firstName, lastName);
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo(firstName);
        assertThat(employee.getLastName()).isEqualTo(lastName);

    }

    @Test
    public void testFindByJPQLNamedParameters(){

        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(save);

        String firstName = "john";
        String lastName = "doe";

        Employee employee = employeeRepository.findByJPQLNamedParameters(firstName, lastName);
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo(firstName);
        assertThat(employee.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testFindByNativeSQL(){

        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(save);

        String firstName = "john";
        String lastName = "doe";
        Employee employee = employeeRepository.findByNativeSQLQuery(firstName, lastName);
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo(firstName);
        assertThat(employee.getLastName()).isEqualTo(lastName);

    }

    @Test
    public void testFindByNativeSQLNamedParameters(){

        Employee save = Employee.builder()
                .firstName("john")
                .lastName("doe")
                .email("john.doe@gmail.com")
                .build();

        employeeRepository.save(save);

        String firstName = "john";
        String lastName = "doe";
        Employee employee = employeeRepository.findByNativeSQLNamedParameters(firstName, lastName);
        assertThat(employee).isNotNull();
        assertThat(employee.getFirstName()).isEqualTo(firstName);
        assertThat(employee.getLastName()).isEqualTo(lastName);

    }

}
