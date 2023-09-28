package com.example.test_assignment.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserMapperTest {

  @Test
  void testDtoToUser() {
    UserDto userDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    User user = UserMapper.dtoToUser(userDto);

    // Assert
    assertEquals(userDto.getEmail(), user.getEmail());
    assertEquals(userDto.getFirstName(), user.getFirstName());
    assertEquals(userDto.getLastName(), user.getLastName());
    assertEquals(userDto.getBirthDate(), user.getBirthDate());
    assertEquals(userDto.getAddress(), user.getAddress());
    assertEquals(userDto.getPhoneNumber(), user.getPhoneNumber());
  }

  @Test
  void testUserToDto() {

    User user = User.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    UserDto userDto = UserMapper.userToDto(user);

    // Assert
    assertEquals(user.getEmail(), userDto.getEmail());
    assertEquals(user.getFirstName(), userDto.getFirstName());
    assertEquals(user.getLastName(), userDto.getLastName());
    assertEquals(user.getBirthDate(), userDto.getBirthDate());
    assertEquals(user.getAddress(), userDto.getAddress());
    assertEquals(user.getPhoneNumber(), userDto.getPhoneNumber());
  }

  @Test
  void testDtoToUserAndUserToDtoConsistency() {

    UserDto originalDto = UserDto.builder()
        .email("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.parse("1990-01-01"))
        .address("123 Main St")
        .phoneNumber("123-456-7890")
        .build();

    User user = UserMapper.dtoToUser(originalDto);
    UserDto userDto = UserMapper.userToDto(user);

    // Assert
    assertEquals(originalDto, userDto);
  }

 }
