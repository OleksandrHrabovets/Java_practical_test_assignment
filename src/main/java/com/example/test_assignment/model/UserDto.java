package com.example.test_assignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

  @NotBlank
  @Email
  @JsonProperty("Email")
  private String email;

  @NotBlank
  @JsonProperty("First name")
  private String firstName;

  @NotBlank
  @JsonProperty("Last name")
  private String lastName;

  @NotNull
  @JsonProperty("Birth date")
  @Past
  private LocalDate birthDate;

  @JsonProperty("Address")
  private String address;

  @JsonProperty("Phone number")
  private String phoneNumber;

}
