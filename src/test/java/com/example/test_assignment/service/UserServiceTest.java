package com.example.test_assignment.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.webjars.NotFoundException;

@SpringBootTest
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Value("${minimum-age}")
  private int minimumAge;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddUser() {
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

    // Call the service method
    User addedUser = userService.addUser(userDto);

    // Assertions
    assertNotNull(addedUser);
    assertEquals(user, addedUser);
  }

  @Test
  void testUpdateUserFields() {
    // Create a user and add them to the service's user list
    User user = User.builder()
        .email("test@example.com")
        .build();
    userService.users.add(user);

    // Define the update map
    Map<String, Object> update = new HashMap<>();
    update.put("First name", "John");
    update.put("Last name", "Doe");

    // Call the service method
    User updatedUser = userService.updateUserFields("test@example.com", update);

    // Assertions
    assertNotNull(updatedUser);
    assertEquals("John", updatedUser.getFirstName());
    assertEquals("Doe", updatedUser.getLastName());
  }

  @Test
  void testDeleteUser() {
    // Create a user and add them to the service's user list
    User user = User.builder()
        .email("test@example.com")
        .build();
    userService.users.add(user);

    // Call the service method
    userService.deleteUser("test@example.com");

    // Assertions
    assertTrue(userService.users.isEmpty());
  }

  @Test
  void testSearchUsersByBirthDateRange() {
    // Create a list of users with different birthdates
    List<User> userList = Arrays.asList(
        createUserWithBirthDate("user1@example.com", LocalDate.of(1990, 1, 1)),
        createUserWithBirthDate("user2@example.com", LocalDate.of(1995, 3, 15)),
        createUserWithBirthDate("user3@example.com", LocalDate.of(2000, 5, 30))
    );
    userService.users.addAll(userList);

    // Call the service method with a date range
    LocalDate fromDate = LocalDate.of(1994, 1, 1);
    LocalDate toDate = LocalDate.of(2000, 12, 31);
    List<User> result = userService.searchUsersByBirthDateRange(fromDate, toDate);

    // Assertions
    assertEquals(2, result.size());
  }

  private User createUserWithBirthDate(String email, LocalDate birthDate) {
    User user = User.builder()
        .email(email)
        .build();
    user.setBirthDate(birthDate);
    return user;
  }

  @Test
  void testUpdateUser() {
    UserDto userDto = UserDto.builder().build();

    // Set up an existing user with different information
    User existingUser = User.builder().build();
    existingUser.setEmail("old@example.com");
    existingUser.setFirstName("OldFirstName");
    existingUser.setLastName("OldLastName");
    existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
    existingUser.setAddress("OldAddress");
    existingUser.setPhoneNumber("OldPhoneNumber");

    // Add the existing user to the service's user list
    userService.users.add(existingUser);

    // Call the service method to update the user
    userDto.setEmail("old@example.com");
    userDto.setFirstName("NewFirstName");
    userDto.setLastName("NewLastName");
    userDto.setBirthDate(LocalDate.of(1995, 5, 5));
    userDto.setAddress("NewAddress");
    userDto.setPhoneNumber("NewPhoneNumber");

    User updatedUser = userService.updateUser(userDto);

    // Assertions
    assertNotNull(updatedUser);
    assertEquals("old@example.com", updatedUser.getEmail());
    assertEquals("NewFirstName", updatedUser.getFirstName());
    assertEquals("NewLastName", updatedUser.getLastName());
    assertEquals(LocalDate.of(1995, 5, 5), updatedUser.getBirthDate());
    assertEquals("NewAddress", updatedUser.getAddress());
    assertEquals("NewPhoneNumber", updatedUser.getPhoneNumber());
  }

  @Test
  void testUpdateNonExistentUser() {
    // Define the update map for a non-existent email address
    Map<String, Object> update = new HashMap<>();
    update.put("First name", "John");

    // Try to update the user and expect a NotFoundException
    assertThrows(NotFoundException.class,
        () -> userService.updateUserFields("nonexistent@example.com", update));
  }

  @Test
  void testSearchUsersWithInvalidDateRange() {
    // Define an invalid date range where "from" is not less than "to"
    LocalDate fromDate = LocalDate.of(2020, 6, 1);
    LocalDate toDate = LocalDate.of(2020, 5, 31);

    // Try to search users with the invalid date range and expect a ValidationException
    assertThrows(ValidationException.class,
        () -> userService.searchUsersByBirthDateRange(fromDate, toDate));
  }

  @Test
  void testAddUserWithValidAge() {
    UserDto userDto = UserDto.builder().build();

    // Set the user's birthdate to make their age valid (greater than or equal to the minimum age)
    userDto.setBirthDate(LocalDate.now().minusYears(minimumAge));

    // Try to add the user with a valid age and expect no exceptions
    assertDoesNotThrow(() -> userService.addUser(userDto));
  }

  @Test
  void testDeleteNonExistentUser() {
    // Try to delete a user with a non-existent email address and expect a NotFoundException
    assertThrows(NotFoundException.class, () -> userService.deleteUser("nonexistent@example.com"));
  }

  @Test
  void testSearchUsersByBirthDateRangeWithNoMatchingUsers() {
    // Set up a list of users with birthdates outside the specified range
    List<User> userList = Arrays.asList(
        createUserWithBirthDate("user1@example.com", LocalDate.of(1990, 1, 1)),
        createUserWithBirthDate("user2@example.com", LocalDate.of(1995, 3, 15)),
        createUserWithBirthDate("user3@example.com", LocalDate.of(2000, 5, 30))
    );
    userService.users.addAll(userList);

    // Search for users within a date range where there are no matching users and expect an empty list
    LocalDate fromDate = LocalDate.of(2022, 1, 1);
    LocalDate toDate = LocalDate.of(2022, 12, 31);
    List<User> result = userService.searchUsersByBirthDateRange(fromDate, toDate);

    assertTrue(result.isEmpty());
  }

  @Test
  void testUpdateUserFieldsWithInvalidField() {
    // Set up an existing user
    User existingUser = User.builder().build();
    existingUser.setEmail("existing@example.com");
    existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
    userService.users.add(existingUser);

    // Attempt to update the user with an invalid field and expect a ValidationException
    Map<String, Object> update = new HashMap<>();
    update.put("Invalid Field", "NewValue");
    assertThrows(ValidationException.class,
        () -> userService.updateUserFields("existing@example.com", update));
  }

  @Test
  void testAddUserAndGetByEmail() {
    UserDto userDto = UserDto.builder().build();

    // Set up the user
    userDto.setEmail("test@example.com");
    userDto.setBirthDate(LocalDate.now().minusYears(minimumAge));
    userService.addUser(userDto);

    // Retrieve the user by email
    User retrievedUser = userService.findUserByEmail("test@example.com");

    // Assertions
    assertNotNull(retrievedUser);
    assertEquals("test@example.com", retrievedUser.getEmail());
  }

  @Test
  void testUpdateUserFieldsWithMultipleFields() {
    // Set up an existing user
    User existingUser = User.builder().build();
    existingUser.setEmail("existing@example.com");
    userService.users.add(existingUser);

    // Define an update map with multiple fields
    Map<String, Object> update = new HashMap<>();
    update.put("First name", "NewFirstName");
    update.put("Last name", "NewLastName");
    update.put("Address", "NewAddress");

    // Update the user's fields
    User updatedUser = userService.updateUserFields("existing@example.com", update);

    // Assertions
    assertNotNull(updatedUser);
    assertEquals("NewFirstName", updatedUser.getFirstName());
    assertEquals("NewLastName", updatedUser.getLastName());
    assertEquals("NewAddress", updatedUser.getAddress());
  }

  @Test
  void testDeleteUserByEmail() {
    // Set up an existing user
    User existingUser = User.builder().build();
    existingUser.setEmail("delete@example.com");
    userService.users.add(existingUser);

    // Delete the user by email
    userService.deleteUser("delete@example.com");

    // Verify that the user has been removed
    assertFalse(userService.users.contains(existingUser));
  }

  @Test
  void testSearchUsersByBirthDateRangeWithSingleUser() {
    // Create a single user with a birthdate within the specified range
    User user = createUserWithBirthDate("user@example.com", LocalDate.of(1990, 5, 15));
    userService.users.add(user);

    // Search for users within the date range
    LocalDate fromDate = LocalDate.of(1980, 1, 1);
    LocalDate toDate = LocalDate.of(2000, 12, 31);
    List<User> result = userService.searchUsersByBirthDateRange(fromDate, toDate);

    // Assertions
    assertEquals(1, result.size());
    assertEquals(user, result.get(0));
  }

}
