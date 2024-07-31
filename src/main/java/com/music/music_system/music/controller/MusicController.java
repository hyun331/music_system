package com.music.music_system.music.controller;

import com.music.music_system.common.dto.CommonResDto;
import com.music.music_system.music.domain.Music;
import com.music.music_system.music.domain.MusicSaveDto;
import com.music.music_system.music.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/music")
public class MusicController {
    private final MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }




//    @PostMapping("/register")
//    public ResponseEntity<?> musicRegister(@RequestBody MusicSaveDto musicSaveDto){
////        Music music = musicService.musicRegister(musicSaveDto);
////        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "music regist", music), HttpStatus.CREATED);
//
//    }


}
