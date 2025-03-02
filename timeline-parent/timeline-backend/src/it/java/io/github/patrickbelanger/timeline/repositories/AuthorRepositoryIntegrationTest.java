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

package io.github.patrickbelanger.timeline.repositories;

import io.github.patrickbelanger.timeline.builders.dtos.AuthorDTOBuilder;
import io.github.patrickbelanger.timeline.configurations.ModelMapperConfig;
import io.github.patrickbelanger.timeline.dtos.AuthorDTO;
import io.github.patrickbelanger.timeline.entities.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ModelMapperConfig.class)
public class AuthorRepositoryIntegrationTest {

    @Autowired
    private AuthorRepository sut;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Rollback
    void save_shouldSaveAuthor() {
        /* Arrange */
        AuthorDTO authorDTO = new AuthorDTOBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setFirstName("Marc")
                .setLastName("Thomas")
                .setPseudonym("Marcus Divine")
                .setEmail("marc-thomas@test.com")
                .setBio("Screenwriter - Bachelor at Concordia - Won the FFM twice")
                .build();

        /* Act */
        AuthorEntity authorEntity = sut.save(modelMapper.map(authorDTO, AuthorEntity.class));

        /* Assert */
        assertNotNull(authorEntity);
        assertNotNull(authorEntity.getUuid());
        assertEquals("Marc", authorEntity.getFirstName());
        assertEquals("Thomas", authorEntity.getLastName());
        assertEquals("marc-thomas@test.com", authorEntity.getEmail());
        assertEquals("Marcus Divine", authorEntity.getPseudonym());
        assertEquals("Screenwriter - Bachelor at Concordia - Won the FFM twice", authorEntity.getBio());
    }

    @Test
    void findByUuid_shouldFindAuthor() {
        /* Arrange */
        AuthorDTO authorDTO = new AuthorDTOBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setFirstName("Marc")
                .setLastName("Thomas")
                .setPseudonym("Marcus Divine")
                .setEmail("marc-thomas@test.com")
                .setBio("Screenwriter - Bachelor at Concordia - Won the FFM twice")
                .build();

        /* Act */
        AuthorEntity authorEntity = sut.save(modelMapper.map(authorDTO, AuthorEntity.class));
        Optional<AuthorEntity> findAuthor = sut.findByUuid(authorEntity.getUuid());

        /* Assert */
        assertNotNull(findAuthor);
        assertTrue(findAuthor.isPresent());
        assertNotNull(findAuthor.get().getUuid());
        assertEquals("Marc", findAuthor.get().getFirstName());
        assertEquals("Thomas", findAuthor.get().getLastName());
        assertEquals("marc-thomas@test.com", findAuthor.get().getEmail());
        assertEquals("Marcus Divine", findAuthor.get().getPseudonym());
        assertEquals("Screenwriter - Bachelor at Concordia - Won the FFM twice", findAuthor.get().getBio());
    }

    @Test
    void dateByUuid_shouldDeleteAuthor() {
        /* Arrange */
        AuthorDTO authorDTO = new AuthorDTOBuilder()
                .setUuid(UUID.randomUUID().toString())
                .setFirstName("Marc")
                .setLastName("Thomas")
                .setPseudonym("Marcus Divine")
                .setEmail("marc-thomas@test.com")
                .setBio("Screenwriter - Bachelor at Concordia - Won the FFM twice")
                .build();

        /* Act */
        AuthorEntity authorEntity = sut.save(modelMapper.map(authorDTO, AuthorEntity.class));
        int deleteEntries = sut.deleteByUuid(authorEntity.getUuid());

        /* Assert */
        assertEquals(1, deleteEntries);
    }
}
