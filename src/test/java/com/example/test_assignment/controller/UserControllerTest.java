package com.example.test_assignment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;
import com.example.test_assignment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.webjars.NotFoundException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddUser() throws Exception {
    // test data
    UserDto userDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    // Mocking the userService.addUser method
    when(userService.addUser(any(UserDto.class))).thenReturn(User.builder().build());

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testUpdateUserField() throws Exception {
    // Test data
    UserDto userDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    String email = "test@example.com";
    Map<String, Object> update = new HashMap<>();
    update.put("First name", "NewFirstName");

    // Mocking the userService.updateUserFields method
    when(userService.updateUserFields(eq(email), any(Map.class))).thenReturn(
        User.builder().build());

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/v1/users")
            .param("email", email)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(update)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testUpdateUser() throws Exception {
    // Test data
    UserDto userDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    User user = User.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    // Mocking the userService.updateUser method
    when(userService.updateUser(any(UserDto.class))).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());

    mockMvc.perform(MockMvcRequestBuilders
            .put("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(user)))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testDeleteUser() throws Exception {
    // Test data
    String email = "test@example.com";

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/v1/users")
            .param("email", email))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void testSearchUsersByBirthDateRange() throws Exception {
    // Test data
    UserDto userDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    LocalDate from = LocalDate.now().minusDays(30);
    LocalDate to = LocalDate.now();

    // Mocking the userService.searchUsersByBirthDateRange method
    when(userService.searchUsersByBirthDateRange(eq(from), eq(to))).thenReturn(
        List.of(User.builder().build()));

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isOk());

    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/users")
            .param("from", from.toString())
            .param("to", to.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Utility method to convert object to JSON string
  private static String asJsonString(final Object obj) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testAddUser_InvalidInput() throws Exception {
    // Invalid user data
    UserDto userDto = UserDto.builder().build(); // Missing required fields

    mockMvc.perform(MockMvcRequestBuilders
            .post("/api/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDto)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testUpdateUserField_UserNotFound() throws Exception {
    // User with the specified email does not exist
    String email = "nonexistent@example.com";
    Map<String, Object> update = new HashMap<>();
    update.put("First name", "NewFirstName");

    // Mocking the userService.updateUserFields method to throw an exception
    when(userService.updateUserFields(eq(email), any(Map.class)))
        .thenThrow(new NotFoundException("User not found."));

    mockMvc.perform(MockMvcRequestBuilders
            .patch("/api/v1/users")
            .param("email", email)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(update)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testDeleteUser_UserNotFound() throws Exception {
    // User with the specified email does not exist
    String email = "nonexistent@example.com";

    // Mocking the userService.deleteUser method to throw an exception
    doThrow(new NotFoundException("User not found."))
        .when(userService).deleteUser(email);

    mockMvc.perform(MockMvcRequestBuilders
            .delete("/api/v1/users")
            .param("email", email))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  void testSearchUsersByBirthDateRange_NoUsersFound() throws Exception {
    // No users found in the given date range
    LocalDate from = LocalDate.now().minusDays(30);
    LocalDate to = LocalDate.now();

    // Mocking the userService.searchUsersByBirthDateRange method to return an empty list
    when(userService.searchUsersByBirthDateRange(eq(from), eq(to))).thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/v1/users")
            .param("from", from.toString())
            .param("to", to.toString()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));
  }

}
