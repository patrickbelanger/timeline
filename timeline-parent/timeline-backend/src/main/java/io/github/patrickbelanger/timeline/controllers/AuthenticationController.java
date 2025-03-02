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

import io.github.patrickbelanger.timeline.dtos.UserDTO;
import io.github.patrickbelanger.timeline.wrappers.ApiResponse;
import io.github.patrickbelanger.timeline.services.UserManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthenticationController {

    private final UserManagementService userManagementService;

    public AuthenticationController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    // TODO: Temporary method
    @GetMapping
    public ResponseEntity<ApiResponse<?>> isAuthenticated(HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> currentUserDTO = userManagementService.login(userDTO);
        return new ResponseEntity<>(currentUserDTO, currentUserDTO.getStatusCode());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<UserDTO>> logout(HttpServletRequest request, HttpServletResponse response) {
        ApiResponse<UserDTO> currentUserDTO = userManagementService.logout(request, response);
        return new ResponseEntity<>(currentUserDTO, currentUserDTO.getStatusCode());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userManagementService.register(userDTO), HttpStatus.OK);
    }
}
