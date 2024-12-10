package com.esm.faceitstats.controller;


import com.esm.faceitstats.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maps")
public class MapPickerController {

    @Autowired
    private LobbyService lobbyService;

    @GetMapping("/pick")
    public ResponseEntity<?> pickMap(@RequestParam String matchLink) {
        var lobby = this.lobbyService.getLobby(matchLink);
        return ResponseEntity.ok(lobby);
    }


}
