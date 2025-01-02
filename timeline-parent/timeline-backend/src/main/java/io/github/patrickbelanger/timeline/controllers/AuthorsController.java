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
import io.github.patrickbelanger.timeline.services.AuthorsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorsController {

    private final AuthorsService authorsService;

    public AuthorsController(AuthorsService authorsService) {
        this.authorsService = authorsService;
    }

    @GetMapping
    public ResponseEntity<Page<AuthorDTO>> getAuthors(Pageable pageable) {
        return new ResponseEntity<>(authorsService.getAuthors(pageable), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable String uuid) {
        return new ResponseEntity<>(this.authorsService.getAuthorByUuid(uuid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authorsService.createAuthor(authorDTO));
    }
}
