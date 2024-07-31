package com.music.music_system.common.service;

import com.music.music_system.member.domain.Role;
import com.music.music_system.member.dto.MemberRefreshDto;
import com.music.music_system.member.dto.MemberSaveDto;
import com.music.music_system.member.repository.MemberRepository;
import com.music.music_system.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminRegistrationService implements CommandLineRunner {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {
        memberService.memberRegister(MemberSaveDto.builder()
                        .email("admin@naver.com")
                        .name("admin")
                        .password("12341234")
                        .role(Role.ADMIN)
                .build());

    }
}
