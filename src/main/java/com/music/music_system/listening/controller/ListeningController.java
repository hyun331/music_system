package com.music.music_system.listening.controller;

import com.music.music_system.common.dto.CommonResDto;
import com.music.music_system.listening.service.ListeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ListeningController {
    private final ListeningService listeningService;

    @Autowired
    public ListeningController(ListeningService listeningService) {
        this.listeningService = listeningService;
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/listening/{id}")
    public ResponseEntity<?> listeningMusic(@PathVariable Long id){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "listening music successfully finished", listeningService.listeningOrLike(id, "listening").getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/like/{id}")
    public ResponseEntity<?> likeMusic(@PathVariable(value = "id") Long musicId){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "user like this music", listeningService.listeningOrLike(musicId, "like").getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }


    //노래 조회
    //자신이 가장 많이 들은 노래 순서
    @GetMapping("/music/list/manyListening")
    public ResponseEntity<?> musicListManyListening(){
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "user가 많이 들은 순서 리스트", listeningService.musicListManyListening());
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

}
