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
import io.github.patrickbelanger.timeline.repositories.UsersRepository;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserManagementService {

        private final AuthenticationManager authenticationManager;
        private final JWTUtils jwtUtils;
        private final ModelMapper modelMapper;
        private final PasswordEncoder passwordEncoder;
        private final UsersRepository usersRepository;
        private final Map<String, Date> tokenBlacklist = new ConcurrentHashMap<>(); // Should replace this with Redis?

        private UserManagementService(AuthenticationManager authenticationManager,
                                      JWTUtils jwtUtils,
                                      ModelMapper modelMapper,
                                      PasswordEncoder passwordEncoder,
                                      UsersRepository usersRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtUtils = jwtUtils;
                this.modelMapper = modelMapper;
                this.passwordEncoder = passwordEncoder;
                this.usersRepository = usersRepository;
        }

        public UserDTO login(UserDTO userDTO) {
                UserDTO response = new UserDTO();
                UserEntity currentUser = usersRepository.findByUsername(userDTO.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(userDTO.getUsername()));

                Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                response.setStatusCode(HttpStatus.OK);
                response.setToken(jwtUtils.generateToken(currentUser));
                response.setRole(currentUser.getRole());
                response.setRefreshToken(jwtUtils.refreshToken(new HashMap<>(), currentUser));
                response.setMessage("Authenticated - Token created");

                return response;
        }

        public UserDTO logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
                UserDTO response = new UserDTO();
                String token = jwtUtils.extractToken(httpServletRequest);
                if (jwtUtils.isTokenExpired(token) || tokenBlacklist.containsKey(token)) {
                        response.setStatusCode(HttpStatus.BAD_REQUEST);
                        response.setMessage("Token is expired");
                        return response;
                }

                tokenBlacklist.put(token, jwtUtils.extractExpirationDate(token));

                new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse,
                        SecurityContextHolder.getContext().getAuthentication());

                response.setStatusCode(HttpStatus.OK);
                response.setMessage("Logout successful");
                return response;
        }

        public boolean isTokenBlacklisted(String token) {
                Date expirationDate = tokenBlacklist.get(token);
                return expirationDate != null && !jwtUtils.isTokenExpired(expirationDate);
        }

        public UserDTO refresh(UserDTO userDTO) {
                UserDTO response = new UserDTO();
                String currentEmail = jwtUtils.extractUsername(userDTO.getUsername());
                UserEntity currentUser = usersRepository.findByUsername(userDTO.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException(userDTO.getUsername()));

                if (!jwtUtils.isValidToken(userDTO.getRefreshToken(), currentUser)) {
                        response.setStatusCode(HttpStatus.BAD_REQUEST);
                        return response;
                }

                response.setStatusCode(HttpStatus.OK);
                response.setToken(jwtUtils.generateToken(currentUser));
                response.setRole(currentUser.getRole());
                response.setRefreshToken(userDTO.getRefreshToken());
                response.setMessage("Token refreshed");

                return response;
        }

        public UserDTO register(UserDTO userDTO) {
                userDTO.setUuid(UUID.randomUUID().toString());
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                UserEntity currentUser = usersRepository.save(modelMapper.map(userDTO, UserEntity.class));
                return modelMapper.map(currentUser, UserDTO.class);
        }
}
