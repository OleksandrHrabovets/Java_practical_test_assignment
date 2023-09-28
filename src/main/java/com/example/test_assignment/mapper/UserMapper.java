package com.example.test_assignment.mapper;

import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;

public class UserMapper {

  public static User dtoToUser(UserDto userDto) {
    return User.builder()
        .email(userDto.getEmail())
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .birthDate(userDto.getBirthDate())
        .address(userDto.getAddress())
        .phoneNumber(userDto.getPhoneNumber())
        .build();
  }

  public static UserDto userToDto(User user) {
    return UserDto.builder()
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .birthDate(user.getBirthDate())
        .address(user.getAddress())
        .phoneNumber(user.getPhoneNumber())
        .build();
  }

}
