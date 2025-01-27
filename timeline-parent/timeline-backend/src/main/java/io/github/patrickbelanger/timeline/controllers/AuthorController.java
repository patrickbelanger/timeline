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

import io.github.patrickbelanger.timeline.dtos.AuthorDTO;
import io.github.patrickbelanger.timeline.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorsService) {
        this.authorService = authorsService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> getAuthors(Pageable pageable) {
        return new ResponseEntity<>(authorService.getAuthors(pageable), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable String uuid) {
        return new ResponseEntity<>(this.authorService.getAuthorByUuid(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authorService.createAuthor(authorDTO));
    }
}
