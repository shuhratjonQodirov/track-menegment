package com.example.trackmenegment.component;

import com.example.trackmenegment.dto.req.UserRequestDto;
import com.example.trackmenegment.dto.res.UserResponse;
import com.example.trackmenegment.enums.UserRoles;
import com.example.trackmenegment.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public User driver(UserRequestDto userRequest) {
        return create(userRequest, UserRoles.DRIVER);
    }

    public User manager(UserRequestDto userRequest) {
        return create(userRequest, UserRoles.MANAGER);
    }

    public User economist(UserRequestDto userRequest) {
        return create(userRequest, UserRoles.ECONOMIST);
    }


    public List<UserResponse> getDirectors(List<User> userList) {
        return listOfUsers(userList, UserRoles.DIRECTOR);
    }

    public List<UserResponse> getDrivers(List<User> userList) {
        return listOfUsers(userList, UserRoles.DRIVER);
    }

    public List<UserResponse> getManagers(List<User> userList) {
        return listOfUsers(userList, UserRoles.MANAGER);

    }

    public List<UserResponse> getEconomists(List<User> userList) {
        return listOfUsers(userList, UserRoles.ECONOMIST);

    }

    private User create(UserRequestDto requestDto, UserRoles userRoles) {
        return User.builder()
                .fullName(requestDto.getFullName())
                .pnfl(requestDto.getPnfl())
                .passportNumber(requestDto.getPassportNumber().toUpperCase())
                .address(requestDto.getAddress())
                .hireDate(requestDto.getHireDate())
                .brithDate(requestDto.getBrithDate())
                .phoneNumber(requestDto.getPhoneNumber())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .roles(userRoles)
                .build();
    }

    private List<UserResponse> listOfUsers(List<User> userList, UserRoles userRoles) {

        return userList.stream()
                .filter(user -> user.getRoles() == userRoles)
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phoneNumber(user.getPhoneNumber())
                        .passportNumber(user.getPassportNumber())
                        .pnfl(user.getPnfl())
                        .address(user.getAddress())
                        .age(user.getAge())
                        .brithDate(user.getBrithDate())
                        .role(user.getRoles().name())
                        .hireDate(user.getHireDate())
                        .fireDate(user.getFireDate())
                        .build()

                ).collect(Collectors.toList());
    }
}
