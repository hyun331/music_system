package com.music.music_system.listening.repository;

import com.music.music_system.listening.domain.Listening;
import com.music.music_system.member.domain.Member;
import com.music.music_system.music.domain.Music;
import com.music.music_system.music.dto.MusicListResDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ListeningRepository extends JpaRepository<Listening, Long> {

    Optional<Listening> findByMemberAndMusic(Member member, Music music);

    @Query("select l, l.music  from Listening l left join fetch l.music")
    List<Object> findAllFetch();

    @Query("select m.id, m.title from Listening l left join Music m on l.music.id = m.id where l.member.id =:memberId order by l.counting desc")
    List<MusicListResDto> findManyListening(@Param("memberId") Long memberId);


}
