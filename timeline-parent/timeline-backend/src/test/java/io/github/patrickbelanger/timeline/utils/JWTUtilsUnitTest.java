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

package io.github.patrickbelanger.timeline.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
public class JWTUtilsUnitTest {

    private JWTUtils jwtUtils;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String secretToken = Base64.getEncoder().encodeToString("this-is-a-very-secure-secret-key".getBytes());
        jwtUtils = new JWTUtils(secretToken);
    }

    @Test
    void generateToken_shouldGenerateTokenAndExtractUsername() {
        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
        Collection<? extends GrantedAuthority> authorities = List.of(authority);

        when(userDetails.getUsername()).thenReturn("testuser");
        when(userDetails.getAuthorities()).thenAnswer(invocation -> authorities);

        String token = jwtUtils.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("testuser", jwtUtils.extractUsername(token));
        assertEquals("USER", jwtUtils.extractAuthorities(token).iterator().next().toString());
    }

    @Test
    void isValidToken_shouldReturnTrue() {

    }

}
