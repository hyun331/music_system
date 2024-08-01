package com.music.music_system.listening.service;

import com.music.music_system.common.service.LikeService;
import com.music.music_system.listening.domain.Listening;
import com.music.music_system.listening.dto.ListeningSaveDto;
import com.music.music_system.listening.repository.ListeningRepository;
import com.music.music_system.member.domain.Member;
import com.music.music_system.member.repository.MemberRepository;
import com.music.music_system.music.domain.Music;
import com.music.music_system.music.dto.MusicListResDto;
import com.music.music_system.music.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ListeningService {
    private final MemberRepository memberRepository;
    private final MusicRepository musicRepository;
    private final ListeningRepository listeningRepository;
    private final LikeService likeService;

    @Autowired
    public ListeningService(MemberRepository memberRepository, MusicRepository musicRepository, ListeningRepository listeningRepository, LikeService likeService) {
        this.memberRepository = memberRepository;
        this.musicRepository = musicRepository;
        this.listeningRepository = listeningRepository;
        this.likeService = likeService;
    }

    public Listening listeningOrLike(Long musicId, String menu) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYn(email, "N").orElseThrow(() -> new EntityNotFoundException("member not found"));

        Music music = musicRepository.findById(musicId).orElseThrow(() -> new EntityNotFoundException("music not found"));
        Listening listening = listeningRepository.findByMemberAndMusic(member, music).orElseGet(() -> {
                    ListeningSaveDto listeningSaveDto = ListeningSaveDto.builder()
                            .member(member)
                            .music(music)
                            .counting(0)
                            .isLike("N").build();
                    return listeningRepository.save(listeningSaveDto.toEntity());
                }
        );
        if (menu.equals("listening")) {
            return this.listeningMusic(listening);
        } else if (menu.equals("like")) {
            return this.likeMusic(listening, musicId);
        }
        return null;

    }

    //노래 듣기
    public Listening listeningMusic(Listening listening) {
        listening.updateCounting();
        return listening;
    }


    //노래 좋아요
    public Listening likeMusic(Listening listening, Long musicId) {
        if(listening.getIsLike().equals("N")){
            listening.updateIsLike("Y");
            likeService.musicLikeIncrease(musicId);
        }else{
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }
        return listening;
    }


    public List<MusicListResDto> musicListManyListening() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmailAndDelYn(email, "N").orElseThrow(() -> new EntityNotFoundException("member not found"));

        List<MusicListResDto> listenings = listeningRepository.findManyListening(member.getId());
        List<MusicListResDto> musicListResDtoPage = new ArrayList<>();
        for(MusicListResDto m : listenings){
            System.out.println(m.getTitle());
//            musicListResDtoPage.add(music.listFromEntity());
        }
        return musicListResDtoPage;
    }
}
