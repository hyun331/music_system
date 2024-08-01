package com.music.music_system.music.controller;

import com.music.music_system.common.dto.CommonResDto;
import com.music.music_system.music.domain.Music;
import com.music.music_system.music.domain.MusicSaveDto;
import com.music.music_system.music.service.MusicService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/music")
public class MusicController {
    private final MusicService musicService;

    @Autowired
    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }


    //노래 등록
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/register")
    public ResponseEntity<?> musicRegister(MusicSaveDto musicSaveDto){
        Music music = musicService.musicRegister(musicSaveDto);
        //순환참조 안빠지려면 music이 아닌 music.getId이런걸 리턴해줘야함
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "music registration", music.getId()), HttpStatus.CREATED);
    }

//    //노래 조회
//    //자신이 가장 많이 들은 노래 순서
//    @GetMapping("/list/manyListening")
//    public ResponseEntity<?> musicListManyListening(){
//        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "user가 많이 들은 순서 리스트", musicService.musicListManyListening());
//        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
//    }

//    //노래 수정
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @PatchMapping("/update/{id}")
//    public ResponseEntity<?> musicUpdate(MusicUpdteDto musicUpdteDto){
//        Music
//    }


    //노래 삭제


}
