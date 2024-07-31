package com.music.music_system.member.service;

import com.music.music_system.member.domain.Member;
import com.music.music_system.member.domain.Role;
import com.music.music_system.member.dto.MemberListResDto;
import com.music.music_system.member.dto.MemberLoginDto;
import com.music.music_system.member.dto.MemberSaveDto;
import com.music.music_system.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //admin이 보는 멤버리스트
    public Page<MemberListResDto> showMemberAllList(Pageable pageable) {
        Page<Member> memberPage = memberRepository.findByRole(pageable, Role.USER);
        return memberPage.map(Member::listFromEntity);
    }


    //로그인
    public Member login(MemberLoginDto memberLoginDto) {
        Member member = memberRepository.findByEmailAndDelYn(memberLoginDto.getEmail(), "N").orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
        if(!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    //회원가입
    public String memberRegister(MemberSaveDto memberSaveDto) {
        if(memberRepository.findByEmailAndDelYn(memberSaveDto.getEmail(), "N").isPresent()){
            throw new IllegalArgumentException("해당 email은 존재하는 이메일입니다.");
        }
        Member member = memberSaveDto.toEntity(passwordEncoder.encode(memberSaveDto.getPassword()));
        return memberRepository.save(member).getEmail();
    }
}
