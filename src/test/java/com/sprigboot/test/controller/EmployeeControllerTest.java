package com.sprigboot.test.controller;

import com.sprigboot.test.model.Employee;
import com.sprigboot.test.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@gmail.com")
                .build();
    }

    @Test
    public void testCreateEmployee_returnEmployee_OnSuccess() throws Exception {

        Mockito.when(employeeServiceImpl.saveEmployee(employee)).thenReturn(employee);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@gmail.com"));

    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee employee1 = Employee.builder()
                .firstName("Jacob")
                .lastName("Creek")
                .email("jacob.creek@gmail.com")
                .build();
        Mockito.when(employeeServiceImpl.getAllEmployees()).thenReturn(List.of(employee, employee1));

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
