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

package io.github.patrickbelanger.timeline.mocks;

import io.github.patrickbelanger.timeline.builders.AuthorDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthorDTOMocks {
    public static AuthorDTO getMock() {
        return new AuthorDTOBuilder()
            .setUuid(UUID.randomUUID().toString())
            .setFirstName("Marc")
            .setLastName("Thomas")
            .setPseudonym("Marcus Divine")
            .setEmail("marc-thomas@test.com")
            .setBio("Screenwriter - Bachelor at Concordia - Won the FFM twice")
            .setPicture("path/to/marc-thomas-picture.png")
            .build();
    }

    public static AuthorDTO getAltMock() {
        return new AuthorDTOBuilder()
            .setUuid(UUID.randomUUID().toString())
            .setFirstName("Emilie")
            .setLastName("Jobin")
            .setPseudonym("Emilie Jolie")
            .setEmail("emilie-jobin@test.com")
            .setBio("Author - Bachelor at UQAM - CEO Interactive Platform")
            .setPicture("path/to/emilie-jobin-picture.png")
            .build();
    }

    public static Page<AuthorDTO> getMocks() {
        List<AuthorDTO> authorsDTO = new ArrayList<>();
        authorsDTO.add(getMock());
        authorsDTO.add(getAltMock());

        return new PageImpl<>(authorsDTO, new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }

            @Override
            public int getPageSize() {
                return 0;
            }

            @Override
            public long getOffset() {
                return 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public Pageable next() {
                return null;
            }

            @Override
            public Pageable previousOrFirst() {
                return null;
            }

            @Override
            public Pageable first() {
                return null;
            }

            @Override
            public Pageable withPage(int pageNumber) {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }
        }, authorsDTO.size());
    }
}
