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

import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.services.RedisBlacklistTokenService;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JWTTokenInterceptor implements HandlerInterceptor {

    private JWTUtils jwtUtils;
    private UserDTO userDTO;
    private RedisBlacklistTokenService redisBlacklistTokenService;

    public JWTTokenInterceptor(JWTUtils jwtUtils,
                               UserDTO userDTO,
                               RedisBlacklistTokenService redisBlacklistTokenService) {
        this.jwtUtils = jwtUtils;
        this.userDTO = userDTO;
        this.redisBlacklistTokenService = redisBlacklistTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object handler) throws IOException {
        String token = jwtUtils.extractToken(httpServletRequest);
        String username = jwtUtils.extractUsername(token);
        boolean isExpired = jwtUtils.isTokenExpired(token);

        if (redisBlacklistTokenService.getToken(token) != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        if (isExpired) {

        }

        return true;
    }
}
