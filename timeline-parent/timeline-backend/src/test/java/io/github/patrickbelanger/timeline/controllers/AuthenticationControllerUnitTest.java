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

package io.github.patrickbelanger.timeline.controllers;

import io.github.patrickbelanger.timeline.filters.JWTAuthenticationFilter;
import io.github.patrickbelanger.timeline.interceptors.JWTTokenInterceptor;
import io.github.patrickbelanger.timeline.mocks.wrappers.ApiResponseMocks;
import io.github.patrickbelanger.timeline.services.RedisBlacklistTokenService;
import io.github.patrickbelanger.timeline.services.RedisRefreshTokenService;
import io.github.patrickbelanger.timeline.services.UserManagementService;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JWTTokenInterceptor jwtTokenInterceptor;

    @MockitoBean
    private UserManagementService userManagementService;

    @MockitoBean
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private RedisBlacklistTokenService redisBlacklistTokenService;

    @MockitoBean
    private RedisRefreshTokenService redisRefreshTokenService;

    @MockitoBean
    private JWTUtils jwtUtils;

    @Test
    void login_shouldBeAbleToLogin() throws Exception {
        /* Arrange */
        when(userManagementService.login(any())).thenReturn(ApiResponseMocks.getSuccessfulLoggedInUserMock());

        /* Act & Assert */
        mockMvc.perform(post("/api/v1/authenticate/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "emilie-jobin@test.com",
                                "password": "not-so-secured"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Emilie Jobin"))
                .andExpect(jsonPath("$.data.username").value("emilie-jobin@test.com"));
    }


}
