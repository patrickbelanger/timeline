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

package io.github.patrickbelanger.timeline.mocks.dtos;

import io.github.patrickbelanger.timeline.builders.dtos.UserDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.UUID;

public class UserDTOMocks {
    public static UserDTO getMock() {
        return new UserDTOBuilder()
            .setUuid(UUID.randomUUID().toString())
            .setName("Emilie Jobin")
            .setUsername("emilie-jobin@test.com")
            .setPassword(UUID.randomUUID().toString())
            .setRole("USER")
            .build();
    }

    public static Page<UserDTO> getMockPage() {
        return new PageImpl<>(Collections.singletonList(getMock()));
    }
}
