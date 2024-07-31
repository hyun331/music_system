package com.music.music_system.music.service;

import com.music.music_system.music.domain.Music;
import com.music.music_system.music.domain.MusicSaveDto;
import com.music.music_system.music.repository.MusicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MusicService {
    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

//    public Music musicRegister(MusicSaveDto musicSaveDto) {
//
//    }
}
