package com.esm.faceitstats;

import com.esm.faceitstats.dto.PredictionResponse;
import com.esm.faceitstats.service.LobbyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MapPickerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private LobbyService lobbyService;


    @Test
    public void testPickMapForbidden() throws Exception {
        String lobby = "";
        PredictionResponse predictionResponse = new PredictionResponse();
        when(lobbyService.getLobby(lobby)).thenReturn(predictionResponse);

        mockMvc.perform(get("/api/maps/pick")
                        .requestAttr("matchLink", lobby)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());


        verify(lobbyService, times(0)).getLobby(lobby);
    }

    @Test
    @WithMockUser(username = "elesm", roles = {"USER", "ADMIN"})
    public void testPickMapBadRequest() throws Exception {
        String lobby = null;
        PredictionResponse predictionResponse = new PredictionResponse();
        when(lobbyService.getLobby(lobby)).thenReturn(predictionResponse);

        mockMvc.perform(get("/api/maps/pick")
                        .param("matchLink", lobby)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


        verify(lobbyService, times(0)).getLobby(lobby);
    }

    @Test
    @WithMockUser(username = "elesm", roles = {"USER", "ADMIN"})
    public void testPickMapOk() throws Exception {
        String lobby = "lobby1";
        PredictionResponse predictionResponse = new PredictionResponse();
        when(lobbyService.getLobby(lobby)).thenReturn(predictionResponse);

        mockMvc.perform(get("/api/maps/pick")
                        .param("matchLink", lobby)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(lobbyService, times(1)).getLobby(lobby);
    }
}
