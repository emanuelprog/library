package com.challenge.library.service;

import com.challenge.library.dto.request.UserRequestDTO;
import com.challenge.library.dto.response.UserResponseDTO;

public interface AuthService {

    String register(UserRequestDTO request);

    UserResponseDTO login(UserRequestDTO request);
}
