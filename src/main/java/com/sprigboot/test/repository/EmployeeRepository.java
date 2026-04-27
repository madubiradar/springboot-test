package com.sprigboot.test.repository;

import com.sprigboot.test.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL with index parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    //define custom query using JPQL with named parameters
    @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
    Employee findByJPQLNamedParameters(String firstName, String lastName);

    //define custom query using native SQL with index parameters

    @Query(nativeQuery = true,
            value = "SELECT * FROM employees e WHERE e.first_name = ?1 AND e.last_name = ?2")
    Employee findByNativeSQLQuery(String firstName, String lastName);

    @Query(nativeQuery = true,
            value = "SELECT * FROM employees e WHERE e.first_name = :firstName AND e.last_name = :lastName")
    Employee findByNativeSQLNamedParameters(String firstName, String lastName);

}
