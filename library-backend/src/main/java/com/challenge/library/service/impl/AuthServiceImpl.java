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
import com.challenge.library.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public String register(UserRequestDTO request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AlreadyExistException("Usuário já existe.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getUsername().equals("admin") ? Role.ROLE_WRITER : Role.ROLE_READER);

        userRepository.save(user);

        return jwtService.generateToken(new HashMap<>(), user.getUsername());
    }

    public UserResponseDTO login(UserRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Senha inválida.");
        }

        UserResponseDTO userResponseDTO = userMapper.toResponse(user);
        userResponseDTO.setAccessToken(jwtService.generateToken(new HashMap<>(), user.getUsername()));

        return userResponseDTO;
    }
}
