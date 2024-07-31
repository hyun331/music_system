package com.music.music_system.member.domain;

import com.music.music_system.common.domain.BaseEntity;
import com.music.music_system.member.dto.MemberListResDto;
import com.music.music_system.music.domain.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;


    @OneToMany(mappedBy = "member")
    private List<Music> musics;


    public MemberListResDto listFromEntity(){
        return MemberListResDto.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .delYn(this.getDelYn())
                .build();
    }




}
