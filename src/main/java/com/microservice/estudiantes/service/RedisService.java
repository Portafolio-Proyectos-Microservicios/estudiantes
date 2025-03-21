package com.microservice.estudiantes.service;

import reactor.core.publisher.Mono;

public interface RedisService {

    Mono<Boolean> saveData(String key, String value);
    Mono<String> getData(String key);
    Mono<Boolean> deleteData(String key);


}
