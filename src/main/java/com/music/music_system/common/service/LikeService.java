package com.music.music_system.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Qualifier("3")
    private final RedisTemplate<String, Object> redisTemplate;


    public LikeService(@Qualifier("3") RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void musicRedisRegister(Long musicId, int likeNum){
        redisTemplate.opsForValue().set(String.valueOf(musicId), likeNum);
    }

    public Long musicLikeIncrease(Long musicId){
        return redisTemplate.opsForValue().increment(String.valueOf(musicId), 1);
    }

//    public Long increase
}
