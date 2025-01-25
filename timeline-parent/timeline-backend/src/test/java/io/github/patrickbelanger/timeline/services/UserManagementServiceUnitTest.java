// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package io.github.patrickbelanger.timeline.services;

import io.github.patrickbelanger.timeline.builders.UserDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.entities.UserEntity;
import io.github.patrickbelanger.timeline.mocks.UserDTOMocks;
import io.github.patrickbelanger.timeline.models.ApiResponse;
import io.github.patrickbelanger.timeline.repositories.UsersRepository;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserManagementServiceUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RedisBlacklistTokenService redisBlacklistTokenService;
    @Mock
    private RedisRefreshTokenService redisRefreshTokenService;
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserManagementService userManagementService;

    private UserEntity userEntity;
    private UserDTO userDTO;

    private static final String VALID_TOKEN = "valid-token";
    private static final String EXPIRED_TOKEN = "expired-token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setName("Emilie Jobin");
        userEntity.setUsername("emilie-jobin@test.com");
        userEntity.setPassword(UUID.randomUUID().toString());
        userEntity.setRole("USER");

        userDTO = new UserDTOBuilder()
                .setUuid(userEntity.getUuid())
                .setUsername("emilie-jobin@test.com")
                .setPassword(userEntity.getPassword())
                .build();
    }

    @Test
    void login_shouldBeAbleToLogin() {
        /* Arrange */
        when(jwtUtils.generateToken(any())).thenReturn("dummy-token");
        when(modelMapper.map(any(), any())).thenReturn(UserDTOMocks.getMock());
        when(usersRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        when(redisRefreshTokenService.setToken(any(), any())).thenReturn(EXPIRED_TOKEN);

        /* Act */
        ApiResponse<UserDTO> response = userManagementService.login(userDTO);

        /* Assert */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dummy-token", response.getToken());
        assertEquals("USER", response.getData().getRole());
        assertEquals("Authentication successful", response.getMessage());
        verify(usersRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void logout_shouldReturnBadRequest_whenTokenIsExpired() {
        /* Arrange */
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + EXPIRED_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(EXPIRED_TOKEN);
        when(jwtUtils.isTokenExpired(EXPIRED_TOKEN)).thenReturn(true);

        /* Act */
        ApiResponse<UserDTO> response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token is expired", response.getError());
        verify(jwtUtils, times(1)).isTokenExpired(EXPIRED_TOKEN);
    }

    @Test
    void logout_shouldReturnBadRequest_whenTokenIsBlacklisted() {
        /* Arrange */
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(VALID_TOKEN);

        /* Act */
        /* Simulating user calling the service twice */
        userManagementService.logout(httpServletRequest, httpServletResponse);
        when(redisBlacklistTokenService.getToken(VALID_TOKEN)).thenReturn(VALID_TOKEN);
        ApiResponse<UserDTO> response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token is expired", response.getError());
        verify(jwtUtils, times(2)).isTokenExpired(VALID_TOKEN);
        verify(jwtUtils, times(2)).isTokenExpired(VALID_TOKEN);
        verify(redisBlacklistTokenService, times(1)).setToken(VALID_TOKEN);
        verify(redisBlacklistTokenService, times(2)).getToken(VALID_TOKEN);
    }

    @Test
    void logout_shouldLogoutSuccessfully_whenTokenIsValid() {
        /* Arrange */
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(VALID_TOKEN);
        when(jwtUtils.isTokenExpired(VALID_TOKEN)).thenReturn(false);

        /* Act */
        ApiResponse<UserDTO> response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout successful", response.getMessage());
        verify(jwtUtils, times(1)).isTokenExpired(VALID_TOKEN);
        verify(redisBlacklistTokenService, times(1)).setToken(VALID_TOKEN);
    }
}
