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

package io.github.patrickbelanger.timeline.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final SecretKey secretKey;
    public static final long EXPIRATION_TIME = 3600 * 1000;

    public JWTUtils(@Value("${jwt.secret.token}") String secretToken) {
        byte[] keyBytes = Base64.getDecoder().decode(secretToken);
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        List<Map<String, String>> roles = extractClaims(token, claims -> claims.get("role", List.class));

        return roles.stream()
                .map(roleMap -> new SimpleGrantedAuthority(roleMap.get("authority")))
                .toList();
    }

    public Date extractExpirationDate(String token) {
        return extractClaims(token, claims -> claims.get("exp", Date.class));
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractToken(HttpServletRequest httpServletRequest) {
        final String BEARER_PREFIX = "Bearer ";
        String token = httpServletRequest.getHeader("Authorization");

        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("Invalid or missing Authorization header");
        }

        return token.substring(BEARER_PREFIX.length());
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .issuer("TIMELINE")
                .subject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME / 2)))
                .signWith(secretKey)
                .compact();
    }

    public String refreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .issuer("TIMELINE")
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(Date expirationDate) {
        return expirationDate.before(new Date());
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
