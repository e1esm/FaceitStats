package com.esm.faceitstats;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.esm.faceitstats.service.FaceitService;
import com.esm.faceitstats.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private FaceitService faceitService;

    @Test
    @WithMockUser(username = "elesm", roles = {"USER", "ADMIN"})
    public void testGetUserID_Success() throws Exception {
        String username = "testUser";
        UserResponse mockResponse = new UserResponse();
        mockResponse.setId("12345");
        mockResponse.setNickname(username);

        when(faceitService.getIDByUsername(username)).thenReturn(mockResponse);

        mockMvc.perform(get("/api/users/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("player_id").value("12345"))
                        .andExpect(jsonPath("nickname").value(mockResponse.getNickname()));

        verify(faceitService, times(1)).getIDByUsername(username);
    }

    @Test
    public void testGetUserID_NotFound() throws Exception {
        String username = "some_user";
        when(faceitService.getIDByUsername(username)).thenReturn(null);

        mockMvc.perform(get("/api/users/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(faceitService, times(0)).getIDByUsername(username);
    }

}

