package com.music.music_system.member.controller;


import com.music.music_system.common.auth.JwtTokenProvider;
import com.music.music_system.common.dto.CommonErrorResDto;
import com.music.music_system.common.dto.CommonResDto;
import com.music.music_system.member.domain.Member;
import com.music.music_system.member.dto.MemberListResDto;
import com.music.music_system.member.dto.MemberLoginDto;
import com.music.music_system.member.dto.MemberRefreshDto;
import com.music.music_system.member.dto.MemberSaveDto;
import com.music.music_system.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Qualifier("2")
    private final RedisTemplate<String, Object> redisTemplate;
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }


    @PostMapping("/register")
    public ResponseEntity<?> memberRegister(@RequestBody MemberSaveDto memberSaveDto){
        String memberEmail = memberService.memberRegister(memberSaveDto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, memberEmail+"님이 회원가입되었습니다.", memberEmail), HttpStatus.CREATED);
    }


    @PostMapping("/doLogin")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberLoginDto){
        Member member = memberService.login(memberLoginDto);
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());

        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail(), member.getRole().toString());

        redisTemplate.opsForValue().set(member.getEmail(), refreshToken, 240, TimeUnit.HOURS);
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("refreshToken", refreshToken);


        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, memberLoginDto.getEmail()+"님 로그인 완료", loginInfo);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);

    }

    @Value("${jwt.secretKeyRt}")
    String secretKeyRt;

    @PostMapping("/refresh-token")
    //at 만료시 refresh token를 body로 받아와서 다시 at 발급. access token은 헤더에 있었음
    public ResponseEntity<?> generateNewAccessToken(@RequestBody MemberRefreshDto dto){
        Claims claims = null;
        String rt = dto.getRefreshToken();
        try{
            //코드를 통해 rt 검증
            claims = Jwts.parser().setSigningKey(secretKeyRt).parseClaimsJws(rt).getBody();
        }catch (Exception e){
            return new ResponseEntity<>(new CommonErrorResDto(HttpStatus.UNAUTHORIZED, "invalid refresh token1"), HttpStatus.UNAUTHORIZED);
        }

        //access token 새로 발급해야함
        String email = claims.getSubject();
        String role = claims.get("role").toString();

        //redis를 조회하여 rt추가검증
        Object obj = redisTemplate.opsForValue().get(email);

        if(obj == null || !obj.toString().equals(rt)){
            return new ResponseEntity<>(new CommonErrorResDto(HttpStatus.UNAUTHORIZED, "invalid refresh token2"), HttpStatus.UNAUTHORIZED);

        }
        String newAt = jwtTokenProvider.createToken(email, role);
        Map<String, Object> info = new HashMap<>();
        info.put("token", newAt);

        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "at is renewed", info), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listAll")
    public ResponseEntity<?> showMemberListAll(Pageable pageable){
        Page<MemberListResDto> memberListResDtos = memberService.showMemberAllList(pageable);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "ADMIN : 회원 리스트", memberListResDtos), HttpStatus.OK);

    }



}
