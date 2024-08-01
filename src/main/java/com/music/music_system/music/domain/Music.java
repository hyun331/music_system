package com.music.music_system.music.domain;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.member.domain.Member;
import com.music.music_system.music.dto.MusicListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Music extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public void updateFilePath(String url){
        this.filePath = url;
    }


    public MusicListResDto listFromEntity(){
        return MusicListResDto.builder()
                .id(this.id)
                .title(this.title)
                .build();
    }
}
