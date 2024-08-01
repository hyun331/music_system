package com.music.music_system.member.dto;

import com.music.music_system.member.domain.Member;
import com.music.music_system.member.domain.Role;
import lombok.*;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSaveDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    @Builder.Default
    private Role role = Role.USER;

    public Member toEntity(String encodedPassword) {
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .phone(this.phone)
                .password(encodedPassword)
                .role(this.role)
                .musics(new ArrayList<>())
                .build();
    }
}
