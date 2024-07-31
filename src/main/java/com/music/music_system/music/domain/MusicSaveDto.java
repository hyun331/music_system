package com.music.music_system.music.domain;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicSaveDto extends BaseEntity {
    private String title;
    private String contents;
    private String filePath;
    private Member member;

    public Music toEntity(){
        return Music.builder()
                .title(this.title)
                .contents(this.contents)
                .filePath(filePath)
                .build();
    }
}
