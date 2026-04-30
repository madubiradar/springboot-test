package com.sprigboot.test.integration;

import com.sprigboot.test.model.Employee;
import com.sprigboot.test.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@ActiveProfiles("test")
@Testcontainers
public class ContainerIntgTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        // Force UTC timezone to avoid "Asia/Calcutta" being sent to PostgreSQL
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        postgresqlContainer.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    void test() {
        assertThat(postgresqlContainer.isRunning()).isTrue();
    }

    @Test
    public void testCreateEmployee_returnEmployee_OnSuccess() throws Exception {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();


        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@gmail.com"));

    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();

        Employee employee1 = Employee.builder()
                .firstName("Jacob")
                .lastName("Creek")
                .email("jacob.creek@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);



        ResultActions  resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Jacob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Creek"));

    }

}
