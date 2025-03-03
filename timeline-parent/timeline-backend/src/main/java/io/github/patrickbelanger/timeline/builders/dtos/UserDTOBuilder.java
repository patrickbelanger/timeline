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

package io.github.patrickbelanger.timeline.builders.dtos;

import io.github.patrickbelanger.timeline.dtos.UserDTO;

public class UserDTOBuilder {

    private final UserDTO userDTO;

    public UserDTOBuilder() {
        this.userDTO = new UserDTO();
    }

    public UserDTOBuilder setUuid(String uuid) {
        userDTO.setUuid(uuid);
        return this;
    }

    public UserDTOBuilder setUsername(String username) {
        userDTO.setUsername(username);
        return this;
    }

    public UserDTOBuilder setName(String name) {
        userDTO.setName(name);
        return this;
    }

    public UserDTOBuilder setPassword(String password) {
        userDTO.setPassword(password);
        return this;
    }

    public UserDTOBuilder setRole(String role) {
        userDTO.setRole(role);
        return this;
    }

    public UserDTO build() {
        return userDTO;
    }
}
