package com.music.music_system.listening.domain;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.member.domain.Member;
import com.music.music_system.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Listening extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "int(11) default 0")
    private int counting = 0;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "char(1) default 'N'")
    private String isLike = "N";

    public void updateCounting() {
        this.counting += 1;
    }

    public void updateIsLike(String isLike) {
        this.isLike = isLike;
    }
}
