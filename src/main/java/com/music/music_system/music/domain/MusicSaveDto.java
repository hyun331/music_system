package com.music.music_system.music.domain;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.member.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicSaveDto{
    private String title;
    private String contents;
    private MultipartFile musicFile;

    public Music toEntity(Member member){
        return Music.builder()
                .title(this.title)
                .contents(this.contents)
                .member(member)
                .build();
    }
}
