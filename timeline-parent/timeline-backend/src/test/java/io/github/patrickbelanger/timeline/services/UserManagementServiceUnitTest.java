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
import io.github.patrickbelanger.timeline.repositories.UsersRepository;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Date;
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
        when(usersRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));

        /* Act */
        UserDTO result = userManagementService.login(userDTO);

        /* Assert */
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("dummy-token", result.getToken());
        assertEquals("USER", result.getRole());
        assertEquals("Authenticated - Token created", result.getMessage());
        verify(usersRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void logout_shouldReturnBadRequest_whenTokenIsExpired() {
        /* Arrange */
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + EXPIRED_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(EXPIRED_TOKEN);
        when(jwtUtils.isTokenExpired(EXPIRED_TOKEN)).thenReturn(true);

        /* Act */
        UserDTO response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token is expired", response.getMessage());
        verify(jwtUtils, times(1)).isTokenExpired(EXPIRED_TOKEN);
    }

    @Test
    void logout_shouldReturnBadRequest_whenTokenIsBlacklisted() {
        /* Arrange */
        Date expirationDate = new Date(System.currentTimeMillis() + 10000);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(VALID_TOKEN);
        when(jwtUtils.extractExpirationDate(VALID_TOKEN)).thenReturn(expirationDate);

        /* Act */
        /* Simulating user calling the service twice */
        userManagementService.logout(httpServletRequest, httpServletResponse);
        UserDTO response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Token is expired", response.getMessage());
        verify(jwtUtils, times(2)).isTokenExpired(VALID_TOKEN);
        verify(jwtUtils, times(2)).isTokenExpired(VALID_TOKEN);
        verify(jwtUtils, times(1)).extractExpirationDate(VALID_TOKEN);
    }

    @Test
    void logout_shouldLogoutSuccessfully_whenTokenIsValid() {
        /* Arrange */
        Date expirationDate = new Date(System.currentTimeMillis() + 10000);

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(jwtUtils.extractToken(httpServletRequest)).thenReturn(VALID_TOKEN);
        when(jwtUtils.isTokenExpired(VALID_TOKEN)).thenReturn(false);
        when(jwtUtils.extractExpirationDate(VALID_TOKEN)).thenReturn(expirationDate);

        /* Act */
        UserDTO response = userManagementService.logout(httpServletRequest, httpServletResponse);

        /* Assert */
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout successful", response.getMessage());
        assertTrue(userManagementService.isTokenBlacklisted(VALID_TOKEN));
        verify(jwtUtils, times(1)).extractExpirationDate(VALID_TOKEN);
        verify(jwtUtils, times(1)).isTokenExpired(VALID_TOKEN);
    }
}
