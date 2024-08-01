package com.music.music_system.listening.dto;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.listening.domain.Listening;
import com.music.music_system.member.domain.Member;
import com.music.music_system.music.domain.Music;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListeningSaveDto {
    private Member member;
    private Music music;
    private int counting;
    private String isLike;

    public Listening toEntity(){
        return Listening.builder()
                .member(member)
                .music(music)
                .counting(this.counting)
                .isLike(this.isLike)
                .build();

    }

}
