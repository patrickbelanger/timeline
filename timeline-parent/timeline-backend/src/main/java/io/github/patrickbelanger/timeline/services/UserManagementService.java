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

import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.entities.UserEntity;
import io.github.patrickbelanger.timeline.wrappers.ApiResponse;
import io.github.patrickbelanger.timeline.repositories.UsersRepository;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserManagementService {

        private final AuthenticationManager authenticationManager;
        private final JWTUtils jwtUtils;
        private final ModelMapper modelMapper;
        private final PasswordEncoder passwordEncoder;
        private final RedisBlacklistTokenService redisBlacklistTokenService;
        private final RedisRefreshTokenService redisRefreshTokenService;
        private final UsersRepository usersRepository;

        public UserManagementService(AuthenticationManager authenticationManager,
                                     JWTUtils jwtUtils,
                                     ModelMapper modelMapper,
                                     PasswordEncoder passwordEncoder,
                                     RedisBlacklistTokenService redisBlacklistTokenService,
                                     RedisRefreshTokenService redisRefreshTokenService,
                                     UsersRepository usersRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
                this.modelMapper = modelMapper;
                this.passwordEncoder = passwordEncoder;
                this.redisBlacklistTokenService = redisBlacklistTokenService;
                this.redisRefreshTokenService = redisRefreshTokenService;
                this.usersRepository = usersRepository;
        }

        public Page<UserDTO> getUsers(Pageable pageable) {
                return usersRepository.findAll(pageable).map(u -> modelMapper.map(u, UserDTO.class));
        }

        public ApiResponse<UserDTO> login(UserDTO userDTO) {
                UserEntity currentUser = usersRepository.findByUsername(userDTO.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(userDTO.getUsername()));

                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDTO loggedUser = modelMapper.map(currentUser, UserDTO.class);
                String refreshToken = jwtUtils.refreshToken(new HashMap<>(), currentUser);

                redisRefreshTokenService.setToken(refreshToken, currentUser.getUsername());

            return new ApiResponse<>(
                HttpStatus.OK,
                "Authentication successful",
                jwtUtils.generateToken(currentUser),
                refreshToken,
                loggedUser
            );
        }

        public ApiResponse<UserDTO> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
                String token = jwtUtils.extractToken(httpServletRequest);
                if (jwtUtils.isTokenExpired(token) || Objects.equals(redisBlacklistTokenService.getToken(token), token)) {
                        return new ApiResponse<>(
                                HttpStatus.BAD_REQUEST,
                                "Token is expired"
                        );
                }

                redisBlacklistTokenService.setToken(token);

                new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse,
                        SecurityContextHolder.getContext().getAuthentication());

                return new ApiResponse<>(
                        HttpStatus.OK,
                        "Logout successful"
                );
        }

        public ApiResponse<UserDTO> refresh(UserDTO userDTO, String currentToken, String refreshToken) {
                if (jwtUtils.isTokenExpired(refreshToken)) {
                        return new ApiResponse<>(
                                HttpStatus.BAD_REQUEST,
                                "Refresh token is expired"
                        );
                }

                UserEntity currentUser = usersRepository.findByUsername(userDTO.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(userDTO.getUsername()));

                redisBlacklistTokenService.setToken(currentToken);
                redisBlacklistTokenService.setToken(refreshToken);

                refreshToken = jwtUtils.refreshToken(new HashMap<>(), currentUser);

                redisRefreshTokenService.setToken(refreshToken, currentUser.getUsername());

                return new ApiResponse<>(
                        HttpStatus.OK,
                        "Token refreshed",
                        jwtUtils.generateToken(currentUser),
                        refreshToken,
                        userDTO
                );
        }

        public ApiResponse<UserDTO> register(UserDTO userDTO) {
                userDTO.setUuid(UUID.randomUUID().toString());
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                UserEntity currentUser = usersRepository.save(modelMapper.map(userDTO, UserEntity.class));
                return new ApiResponse<>(
                        HttpStatus.OK,
                        "User registration successful",
                        modelMapper.map(currentUser, UserDTO.class)
                );
        }
}
