package io.github.patrickbelanger.timeline.controllers;

import io.github.patrickbelanger.timeline.interceptors.JWTTokenInterceptor;
import io.github.patrickbelanger.timeline.mocks.dtos.UserDTOMocks;
import io.github.patrickbelanger.timeline.services.RedisBlacklistTokenService;
import io.github.patrickbelanger.timeline.services.RedisRefreshTokenService;
import io.github.patrickbelanger.timeline.services.UserManagementService;
import io.github.patrickbelanger.timeline.services.UserService;
import io.github.patrickbelanger.timeline.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserManagementControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JWTTokenInterceptor jwtTokenInterceptor;

    @MockitoBean
    private JWTUtils jwtUtils;

    @MockitoBean
    private RedisBlacklistTokenService redisBlacklistTokenService;

    @MockitoBean
    private RedisRefreshTokenService redisRefreshTokenService;

    @MockitoBean
    private UserManagementService userManagementService;

    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_shouldGetPageUsers() throws Exception {
        /* Arrange */
        when(userManagementService.getUsers(any())).thenReturn(UserDTOMocks.getMockPage());
        /* Act & Assert */
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Emilie Jobin"))
                .andExpect(jsonPath("$.content[0].username").value("emilie-jobin@test.com"))
                .andExpect(jsonPath("$.content[0].role").value("USER"))
                .andExpect(jsonPath("$.content[0].revoked").value("false"));
    }
}
