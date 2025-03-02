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

import io.github.patrickbelanger.timeline.builders.dtos.AuthorDTOBuilder;
import io.github.patrickbelanger.timeline.dtos.AuthorDTO;
import io.github.patrickbelanger.timeline.entities.AuthorEntity;
import io.github.patrickbelanger.timeline.repositories.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceUnitTest {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthorService authorService;

    private AuthorEntity authorEntity;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        authorEntity = new AuthorEntity();
        authorEntity.setId(1L);
        authorEntity.setUuid(UUID.randomUUID().toString());
        authorEntity.setFirstName("Emilie");
        authorEntity.setLastName("Jobin");
        authorEntity.setPseudonym("Emilie Jolie");
        authorEntity.setEmail("emilie-jobin@test.com");
        authorEntity.setBio("Author - Bachelor at UQAM - Interactive Platform");
        authorEntity.setPicture("path/to/emilie-jobin-picture.png");

        authorDTO = new AuthorDTOBuilder()
                .setUuid(authorEntity.getUuid())
                .setFirstName("Emilie")
                .setLastName("Jobin")
                .setPseudonym("Emilie Jolie")
                .setEmail("emilie-jobin@test.com")
                .setBio("Author - Bachelor at UQAM - Interactive Platform")
                .setPicture("path/to/emilie-jobin-picture.png")
                .build();
    }

    @Test
    void getAuthors_shouldGetPageableWithAuthorDto() {
        /* Arrange */
        Pageable pageable = PageRequest.of(0, 10);
        Page<AuthorEntity> authorPage = new PageImpl<>(Collections.singletonList(authorEntity));
        when(authorRepository.findAll(pageable)).thenReturn(authorPage);
        when(modelMapper.map(authorEntity, AuthorDTO.class)).thenReturn(authorDTO);

        /* Act */
        Page<AuthorDTO> result = authorService.getAuthors(pageable);

        /* Assert */
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Emilie", result.getContent().getFirst().getFirstName());
        assertEquals("Jobin", result.getContent().getFirst().getLastName());
        assertEquals("Emilie Jolie", result.getContent().getFirst().getPseudonym());
        assertEquals("emilie-jobin@test.com", result.getContent().getFirst().getEmail());
        assertEquals("Author - Bachelor at UQAM - Interactive Platform", result.getContent().getFirst().getBio());
        assertEquals("path/to/emilie-jobin-picture.png", result.getContent().getFirst().getPicture());
        verify(authorRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(1)).map(authorEntity, AuthorDTO.class);
    }

    @Test
    void getAuthorByUuid_validUuid_shouldGetAuthorDto() {
        /* Arrange */
        when(authorRepository.findByUuid(authorEntity.getUuid())).thenReturn(Optional.of(authorEntity));
        when(modelMapper.map(authorEntity, AuthorDTO.class)).thenReturn(authorDTO);

        /* Act */
        AuthorDTO result = authorService.getAuthorByUuid(authorEntity.getUuid());

        /* Assert */
        assertNotNull(result);
        assertEquals("Emilie", result.getFirstName());
        verify(authorRepository, times(1)).findByUuid(authorEntity.getUuid());
        verify(modelMapper, times(1)).map(authorEntity, AuthorDTO.class);
    }

    @Test
    void getAuthorByUuid_invalidUuid_shouldThrowAnException() {
        /* Arrange & Act */
        when(authorRepository.findByUuid(anyString())).thenReturn(Optional.empty());

        /* Assert */
        assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByUuid("invalid-uuid"));
        verify(authorRepository, times(1)).findByUuid("invalid-uuid");
    }

    @Test
    void createAuthor_shouldAbleToCreateAndGetAuthorDto() {
        /* Arrange */
        when(modelMapper.map(authorDTO, AuthorEntity.class)).thenReturn(authorEntity);
        when(authorRepository.save(authorEntity)).thenReturn(authorEntity);
        when(modelMapper.map(authorEntity, AuthorDTO.class)).thenReturn(authorDTO);

        /* Act */
        AuthorDTO result = authorService.createAuthor(authorDTO);

        /* Assert */
        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertEquals("Emilie", result.getFirstName());
        verify(authorRepository, times(1)).save(authorEntity);
        verify(modelMapper, times(2)).map(any(), any());
    }
}
