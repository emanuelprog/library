package com.challenge.library.service.impl;

import com.challenge.library.dto.request.UserRequestDTO;
import com.challenge.library.dto.response.UserResponseDTO;
import com.challenge.library.entity.User;
import com.challenge.library.enums.Role;
import com.challenge.library.exception.AlreadyExistException;
import com.challenge.library.exception.BadRequestException;
import com.challenge.library.exception.NotFoundException;
import com.challenge.library.mapper.UserMapper;
import com.challenge.library.repository.UserRepository;
import com.challenge.library.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRequestDTO request;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        request = new UserRequestDTO();
        request.setUsername("user");
        request.setPassword("pass");

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setPassword("encoded");
        user.setRole(Role.ROLE_READER);
    }

    @Test
    void register_ShouldThrow_WhenUserExists() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        assertThrows(AlreadyExistException.class, () -> authService.register(request));
    }

    @Test
    void register_ShouldReturnToken_WhenUserNew() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(jwtService.generateToken(anyMap(), eq("user"))).thenReturn("token");

        String result = authService.register(request);

        assertThat(result).isEqualTo("token");
    }

    @Test
    void login_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> authService.login(request));
    }

    @Test
    void login_ShouldThrow_WhenPasswordInvalid() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> authService.login(request));
    }

    @Test
    void login_ShouldReturnUserResponseDTOWithToken_WhenValid() {
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("user");
        request.setPassword("pass");

        User user = new User();
        user.setUsername("user");
        user.setPassword("encoded");

        UserResponseDTO mappedResponse = new UserResponseDTO();
        mappedResponse.setUsername("user");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(userMapper.toResponse(user)).thenReturn(mappedResponse);
        when(jwtService.generateToken(anyMap(), eq("user"))).thenReturn("token");

        UserResponseDTO result = authService.login(request);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("user");
        assertThat(result.getAccessToken()).isEqualTo("token");
    }
}