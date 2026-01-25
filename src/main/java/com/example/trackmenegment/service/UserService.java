package com.example.trackmenegment.service;

import com.example.trackmenegment.component.UserFactory;
import com.example.trackmenegment.dto.req.UserRequestDto;
import com.example.trackmenegment.dto.res.UserResponse;
import com.example.trackmenegment.error.ByIdException;
import com.example.trackmenegment.error.ExistsNameException;
import com.example.trackmenegment.error.ValidationException;
import com.example.trackmenegment.model.User;
import com.example.trackmenegment.repository.UserRepository;
import com.example.trackmenegment.utils.ApiResponse;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse createDriver(UserRequestDto userRequest) {
        if (userRepository.existsByPnflAndDeletedFalse(userRequest.getPnfl()))
            throw new ExistsNameException("This Driver is already exists");
        validateUserRequestDto(userRequest);
        User user = userFactory.driver(userRequest);
        userRepository.save(user);
        return new ApiResponse("Driver saved successfully", true);
    }

    public ApiResponse createManager(UserRequestDto userRequest) {
        if (userRepository.existsByPnflAndDeletedFalse(userRequest.getPnfl()))
            throw new ExistsNameException("This Manager is already exists");
        validateUserRequestDto(userRequest);
        User user = userFactory.manager(userRequest);
        userRepository.save(user);
        return new ApiResponse("Manager saved successfully", true);

    }

    public ApiResponse createEconomist(UserRequestDto userRequest) {
        if (userRepository.existsByPnflAndDeletedFalse(userRequest.getPnfl()))
            throw new ExistsNameException("This Economist is already exists");
        validateUserRequestDto(userRequest);
        User user = userFactory.economist(userRequest);
        userRepository.save(user);
        return new ApiResponse("Economist saved successfully", true);
    }


    public ApiResponse getUser(Long userId) {

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() ->
                        new ByIdException("user not found by id"));

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .pnfl(user.getPnfl())
                .passportNumber(user.getPassportNumber())
                .address(user.getAddress())
                .brithDate(user.getBrithDate())
                .age(user.getAge())
                .role(user.getRoles().name())
                .hireDate(user.getHireDate())
                .build();

        return new ApiResponse("user by id", true, response);
    }

    public ApiResponse getDirectors() {
        List<User> userList = userRepository.findAllByDeletedFalse();
        List<UserResponse> list = userFactory.getDirectors(userList);
        return new ApiResponse("List of Directors", true, list);
    }

    public ApiResponse getListOfDriver() {
        List<User> userList = userRepository.findAllByDeletedFalse();
        List<UserResponse> list = userFactory.getDrivers(userList);
        return new ApiResponse("List of Drivers", true, list);
    }

    public ApiResponse getListOfManager() {
        List<User> userList = userRepository.findAllByDeletedFalse();
        List<UserResponse> list = userFactory.getManagers(userList);
        return new ApiResponse("list of managers", true, list);
    }

    public ApiResponse getListOfEconomist() {
        List<User> userList = userRepository.findAllByDeletedFalse();
        List<UserResponse> list = userFactory.getEconomists(userList);
        return new ApiResponse("list of economist", true, list);
    }

    private void validateUserRequestDto(UserRequestDto dto) {
        if (dto == null) {
            throw new ValidationException("Request data cannot be null");
        }
        if (StringUtils.isBlank(dto.getFullName())) {
            throw new ValidationException("Full name cannot be empty");
        }

        if (StringUtils.isBlank(dto.getPhoneNumber())) {
            throw new ValidationException("Phone number cannot be empty");
        }

        if (StringUtils.isBlank(dto.getPnfl()) || !dto.getPnfl().matches("\\d{14}")) {
            throw new ValidationException("PNFL is invalid (must be 14 digits)");
        }

        if (StringUtils.isBlank(dto.getPassportNumber()) || !dto.getPassportNumber().toUpperCase().matches("^[A-Z]{2}\\d{7}$")) {
            throw new ValidationException("Passport number is invalid (format: AA1234567)");
        }

        if (StringUtils.isBlank(dto.getPassword()) || dto.getPassword().length() < 6) {
            throw new ValidationException("Password is required and must be at least 6 characters");
        }

        if (StringUtils.isBlank(dto.getAddress())) {
            throw new ValidationException("Address is required and cannot be empty");
        }
        if (dto.getBrithDate() != null && dto.getBrithDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date cannot be in the future");
        }

        if (dto.getHireDate() != null && dto.getHireDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Hire date cannot be in the future");
        }
    }

    public ApiResponse update(UserRequestDto dto, Long id) {

        User user = userRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> new ByIdException("User not found"));

        if (!user.getPnfl().equals(dto.getPnfl()) &&
                userRepository.existsByPnflAndDeletedFalse(dto.getPnfl())) {
            throw new ByIdException("PNFL already exists");
        }
        validateUserRequestDtoForUpdate(dto);

        if (StringUtils.isNotBlank(dto.getFullName())) {
            user.setFullName(dto.getFullName());
        }
        if (StringUtils.isNotBlank(dto.getPhoneNumber())) {
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(dto.getPnfl())) {
            user.setPnfl(dto.getPnfl());
        }
        if (StringUtils.isNotBlank(dto.getPassportNumber())) {
            user.setPassportNumber(dto.getPassportNumber().toUpperCase());
        }
        if (StringUtils.isNotBlank(dto.getAddress())) {
            user.setAddress(dto.getAddress());
        }
        if (StringUtils.isNotBlank(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getBrithDate() != null) {
            user.setBrithDate(dto.getBrithDate());
            user.setAge(Period.between(dto.getBrithDate(), LocalDate.now()).getYears());
        }

        if (dto.getHireDate() != null) {
            user.setHireDate(dto.getHireDate());
        }

        userRepository.save(user);
        return new ApiResponse("updated", true);
    }

    private void validateUserRequestDtoForUpdate(UserRequestDto dto) {

        if (StringUtils.isNotBlank(dto.getPnfl()) && !dto.getPnfl().matches("\\d{14}")) {
            throw new ByIdException("PNFL is invalid (must be 14 digits)");
        }
        if (StringUtils.isNotBlank(dto.getPassportNumber()) &&
                !dto.getPassportNumber().matches("^[A-Z]{2}\\d{7}$")) {
            throw new ByIdException("Passport number is invalid (format: AA1234567)");
        }

    }


}



