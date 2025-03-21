package com.microservice.estudiantes.service;


import com.microservice.estudiantes.model.Estudiante;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstudianteService{

    Flux<Estudiante> obtenerTodoslosEstudiantes();
    Mono<Estudiante> obtenerEstudianteporID(String id);
    Mono<Estudiante> registrarEstudiante(Estudiante estudiante);
    Mono<Estudiante> actualizarEstudiante(String id, Estudiante estudiante);
    Mono<Void> eliminarEstudiante(String id);
    Mono<Void> enviarMensajeAKafka(String topic, String key, Object mensaje);


}
