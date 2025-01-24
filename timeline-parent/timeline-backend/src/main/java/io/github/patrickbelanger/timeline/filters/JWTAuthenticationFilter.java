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

package io.github.patrickbelanger.timeline.filters;

import io.github.patrickbelanger.timeline.services.UserService;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final JWTUtils jwtUtils;
    private final UserService userService;

    public JWTAuthenticationFilter(JWTUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = httpServletRequest.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        jwtToken = authorizationHeader.substring(7);
        userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UserDetails userDetails = userService.loadUserByUsername(userEmail);

        if (!jwtUtils.isValidToken(jwtToken, userDetails)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        logger.info("Set security context");
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
