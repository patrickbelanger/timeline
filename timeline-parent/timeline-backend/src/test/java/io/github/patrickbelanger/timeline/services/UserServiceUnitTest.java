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

import io.github.patrickbelanger.timeline.builders.dtos.UserDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.entities.UserEntity;
import io.github.patrickbelanger.timeline.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserDTO userDTO;

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
    void loadUserByUsername_validUser_shouldGetUserDetails() {
        /* Arrange */
        when(usersRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));

        /* Act */
        UserDetails result = userService.loadUserByUsername(userDTO.getUsername());

        /* Assert */
        assertEquals("emilie-jobin@test.com", result.getUsername());
        assertEquals(userDTO.getPassword(), result.getPassword());
        verify(usersRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void loadUserByUsername_invalidUuid_shouldThrowAnException() {
        /* Arrange & Act */
        when(usersRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        /* Assert */
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("invalid@user.com"));
        verify(usersRepository, times(1)).findByUsername("invalid@user.com");
    }
}
