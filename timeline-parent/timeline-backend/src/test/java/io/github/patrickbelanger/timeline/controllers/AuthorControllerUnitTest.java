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

import io.github.patrickbelanger.timeline.mocks.AuthorDTOMocks;
import io.github.patrickbelanger.timeline.services.AuthorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
public class AuthorControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @Test
    void getAuthors_shouldGetPageAuthors() throws Exception {
        /* Arrange */
        when(authorService.getAuthors(any())).thenReturn(AuthorDTOMocks.getMocks());
        /* Act & Assert */
        mockMvc.perform(get("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Marc"))
                .andExpect(jsonPath("$.content[0].email").value("marc-thomas@test.com"))
                .andExpect(jsonPath("$.content[1].firstName").value("Emilie"))
                .andExpect(jsonPath("$.content[1].email").value("emilie-jobin@test.com"));
    }

    @Test
    void getAuthorByUuid_shouldGetAuthor() throws Exception {
        /* Arrange */
        when(authorService.getAuthorByUuid(any())).thenReturn(AuthorDTOMocks.getMock());
        /* Act & Assert */
        mockMvc.perform(get("/api/v1/authors/{uuid}", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Marc"))
                .andExpect(jsonPath("$.email").value("marc-thomas@test.com"));
    }

    @Test
    void createAuthor_shouldCreateAuthor() throws Exception {
        /* Arrange */
        when(authorService.createAuthor(any())).thenReturn(AuthorDTOMocks.getMock());

        /* Act & Assert */
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "firstName": "Marc",
                        "lastName": "Thomas",
                        "pseudonym": "Marcus Divine",
                        "email": "marc-thomas@example.com",
                        "bio": "Screenwriter - Bachelor at Concordia - Won the FFM twice",
                        "picture": ""
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Marc"))
                .andExpect(jsonPath("$.email").value("marc-thomas@test.com"));
    }
}
