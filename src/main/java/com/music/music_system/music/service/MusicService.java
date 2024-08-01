package com.music.music_system.music.service;

import com.music.music_system.common.service.LikeService;
import com.music.music_system.member.domain.Member;
import com.music.music_system.member.repository.MemberRepository;
import com.music.music_system.member.service.MemberService;
import com.music.music_system.music.domain.Music;
import com.music.music_system.music.domain.MusicSaveDto;
import com.music.music_system.music.dto.MusicListResDto;
import com.music.music_system.music.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MusicService {
    private final MusicRepository musicRepository;
    private final S3Client s3Client;
    private final MemberService memberService;
    private final LikeService likeService;
    public MusicService(MusicRepository musicRepository, S3Client s3Client, MemberService memberService, LikeService likeService) {
        this.musicRepository = musicRepository;
        this.s3Client = s3Client;
        this.memberService = memberService;
        this.likeService = likeService;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Music musicRegister(MusicSaveDto musicSaveDto) {
        MultipartFile image = musicSaveDto.getMusicFile();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findByEmailAndDelYn(email, "N").orElseThrow(()->new EntityNotFoundException("member not found"));
        Member member = memberService.memberFindByEmail(email, "N");
        Music music = null;
        try{
            music = musicRepository.save(musicSaveDto.toEntity(member));

            likeService.musicRedisRegister(music.getId(), 0);
            //이미지 파일 저장시 byte로
            byte[] bytes = image.getBytes();

            String fileName = music.getId()+"_"+image.getOriginalFilename();
            Path path = Paths.get("C:/Users/Playdata/Desktop/tmp/", fileName);

            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));
            String s3Path = s3Client.utilities().getUrl(a->a.bucket(bucket).key(fileName)).toExternalForm();

            music.updateFilePath(s3Path);

            member.getMusics().add(music);
        }catch (IOException e){
            throw new RuntimeException("음악 파일 저장 실패");
        }

        return music;
    }







}
