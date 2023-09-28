package com.example.test_assignment.service;

import com.example.test_assignment.mapper.UserMapper;
import com.example.test_assignment.model.User;
import com.example.test_assignment.model.UserDto;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Slf4j
@Service
public class UserService {

  @Value("${minimum-age}")
  private int minimumAge;

  protected final List<User> users = new ArrayList<>();

  protected User findUserByEmail(String email) {

    log.debug("findUserByEmail");
    return users.stream()
        .filter(u -> u.getEmail().equals(email))
        .findFirst()
        .orElseThrow(() -> new NotFoundException(
            String.format("User with email %s not found", email)));
  }

  public User addUser(UserDto userDto) {

    log.debug("addUser");
    checkAge(userDto.getBirthDate());

    User user = UserMapper.dtoToUser(userDto);
    users.add(user);
    return user;

  }

  private void checkAge(LocalDate birthDate) {

    log.debug("checkAge");
    int age = LocalDate.now().getYear() - birthDate.getYear();
    if (age < minimumAge) {
      throw new ValidationException(
          String.format("allows to register users who are more than %d years old", minimumAge));
    }

  }

  public User updateUserFields(String email, Map<String, Object> update) {

    log.debug("updateUserFields");
    User user = findUserByEmail(email);
    for (Entry<String, Object> entry : update.entrySet()) {
      String fieldName = entry.getKey();
      Object value = entry.getValue();
      switch (fieldName) {
        case "Email" -> user.setEmail((String) value);
        case "First name" -> user.setFirstName((String) value);
        case "Last name" -> user.setLastName((String) value);
        case "Birth date" -> user.setBirthDate(LocalDate.parse((CharSequence) value));
        case "Address" -> user.setAddress((String) value);
        case "Phone number" -> user.setPhoneNumber((String) value);
        default -> throw new ValidationException(String.format("Field %s not found", fieldName));
      }
    }
    return user;

  }

  public User updateUser(UserDto userDto) {

    log.debug("updateUser");
    User user = findUserByEmail(userDto.getEmail());
    checkAge(userDto.getBirthDate());
    user.setEmail(userDto.getEmail());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setBirthDate(userDto.getBirthDate());
    user.setAddress(userDto.getAddress());
    user.setPhoneNumber(userDto.getPhoneNumber());
    return user;

  }

  public void deleteUser(String email) {

    log.debug("deleteUser");
    User user = findUserByEmail(email);
    users.remove(user);

  }

  public List<User> searchUsersByBirthDateRange(LocalDate from, LocalDate to) {

    if (!from.isBefore(to)) {
      throw new ValidationException("'From' is not less than 'To'");
    }
    log.debug("searchUsersByBirthDateRange");
    return users.stream()
        .filter(u -> u.getBirthDate().isAfter(from.minusDays(1))
            && u.getBirthDate().isBefore(to.plusDays(1)))
        .toList();

  }

}
