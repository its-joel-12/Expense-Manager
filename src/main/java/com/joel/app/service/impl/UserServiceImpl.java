package com.joel.app.service.impl;

import com.joel.app.dto.UserRequestDto;
import com.joel.app.dto.UserResponseDto;
import com.joel.app.exception.ResourceNotFoundException;
import com.joel.app.model.User;
import com.joel.app.repository.UserRepository;
import com.joel.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();

        List<UserResponseDto> userResponseDtoLists = allUsers
                .stream()
                .map(e -> modelMapper.map(e, UserResponseDto.class))
                .toList();

        return userResponseDtoLists;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setCreatedBy(user.getFirstName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );
        userRepository.delete(user);
    }
}
