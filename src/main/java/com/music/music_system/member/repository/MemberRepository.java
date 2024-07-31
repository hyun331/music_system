package com.music.music_system.member.repository;

import com.music.music_system.member.domain.Member;
import com.music.music_system.member.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDelYn(String email, String delYn);

    Page<Member> findByRole(Pageable pageable, Role role);
}
