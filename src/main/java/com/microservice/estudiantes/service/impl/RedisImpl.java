package com.microservice.estudiantes.service.impl;

import com.microservice.estudiantes.service.RedisService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisImpl implements RedisService {

    private final ReactiveRedisTemplate<String,String> reactiveRedisTemplate;

    public RedisImpl(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public Mono<Boolean> saveData(String key, String value){
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Mono<String> getData(String key){
        return reactiveRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Mono<Boolean> deleteData(String key){
        return reactiveRedisTemplate.delete(key)
                .map(delete -> delete > 0);
    }

}
