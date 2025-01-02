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

import io.github.patrickbelanger.timeline.dtos.AuthorDTO;
import io.github.patrickbelanger.timeline.entities.AuthorEntity;
import io.github.patrickbelanger.timeline.repositories.AuthorRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AuthorsService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    public AuthorsService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    public Page<AuthorDTO> getAuthors(Pageable pageable) {
        Page<AuthorEntity> authors = authorRepository.findAll(pageable);
        List<AuthorDTO> authorDTO = authors.getContent().stream()
                .map(author -> modelMapper.map(author, AuthorDTO.class))
                .toList();
        return new PageImpl<>(authorDTO, pageable, authors.getTotalElements());
    }

    public AuthorDTO getAuthorByUuid(String uuid) {
        AuthorEntity author = authorRepository.findByUuid(uuid)
            .orElseThrow(() -> new NoSuchElementException("Author not found"));
        return modelMapper.map(author, AuthorDTO.class);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        authorDTO.setUuid(UUID.randomUUID().toString());
        AuthorEntity currentAuthor = authorRepository.save(modelMapper.map(authorDTO, AuthorEntity.class));
        return modelMapper.map(currentAuthor, AuthorDTO.class);
    }
}
