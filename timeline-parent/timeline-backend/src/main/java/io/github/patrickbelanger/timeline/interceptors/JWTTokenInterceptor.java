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

package io.github.patrickbelanger.timeline.interceptors;

import io.github.patrickbelanger.timeline.builders.dtos.UserDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.wrappers.ApiResponse;
import io.github.patrickbelanger.timeline.services.RedisBlacklistTokenService;
import io.github.patrickbelanger.timeline.services.RedisRefreshTokenService;
import io.github.patrickbelanger.timeline.services.UserManagementService;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;

@Component
public class JWTTokenInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(JWTTokenInterceptor.class);
    private final JWTUtils jwtUtils;
    private final RedisBlacklistTokenService redisBlacklistTokenService;
    private final RedisRefreshTokenService redisRefreshTokenService;
    private final UserManagementService userManagementService;

    public JWTTokenInterceptor(JWTUtils jwtUtils,
                               RedisBlacklistTokenService redisBlacklistTokenService,
                               RedisRefreshTokenService redisRefreshTokenService,
                               UserManagementService userManagementService) {
        this.jwtUtils = jwtUtils;
        this.redisBlacklistTokenService = redisBlacklistTokenService;
        this.redisRefreshTokenService = redisRefreshTokenService;
        this.userManagementService  = userManagementService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) throws IOException {
        Optional<ApiResponse<UserDTO>> currentUser = Optional.empty();
        String token = jwtUtils.extractToken(httpServletRequest);
        String username = jwtUtils.extractUsername(token);
        String refreshToken = redisRefreshTokenService.getToken(username);
        boolean isExpired = jwtUtils.isTokenExpired(token);

        if (redisBlacklistTokenService.getToken(token) != null) {
            logger.info("Expired token for user {}", username.replaceAll("(?<=.{2}).(?=.*@.{2})", "*"));
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        if (isExpired && refreshToken != null && !jwtUtils.isTokenExpired(refreshToken)) {
            logger.info("Refresh expired token for user {}", username.replaceAll("(?<=.{2}).(?=.*@.{2})", "*"));
            currentUser = Optional.of(userManagementService
                    .refresh(new UserDTOBuilder().setUsername(username).build(), token, refreshToken)
            );
            redisBlacklistTokenService.setToken(token);
            redisRefreshTokenService.setToken(currentUser.map(ApiResponse::getRefreshToken).orElseThrow(), username);
        }
        httpServletResponse.setHeader("X-Token", currentUser.map(ApiResponse::getToken).orElse(token));
        httpServletResponse.setHeader("X-Refresh-Token", currentUser.map(ApiResponse::getRefreshToken).orElse(refreshToken));

        return true;
    }
}
