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

package io.github.patrickbelanger.timeline.dtos;

import java.util.Objects;

public class AuthorDTO {
    private String uuid;
    private String firstName;
    private String lastName;
    private String pseudonym;
    private String email;
    private String bio;
    private String picture;

    public AuthorDTO() { }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return Objects.equals(uuid, authorDTO.uuid) &&
                Objects.equals(firstName, authorDTO.firstName) &&
                Objects.equals(lastName, authorDTO.lastName) &&
                Objects.equals(pseudonym, authorDTO.pseudonym) &&
                Objects.equals(email, authorDTO.email) &&
                Objects.equals(bio, authorDTO.bio) &&
                Objects.equals(picture, authorDTO.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, firstName, lastName, pseudonym, email, bio, picture);
    }

    @Override
    public String toString() {
        return String.format(
            "AuthorDTO{uuid='%s', firstName='%s', lastName='%s', pseudonym='%s', email='%s', bio='%s', picture='%s'}",
            uuid, firstName, lastName, pseudonym, email, bio, picture
        );
    }
}

