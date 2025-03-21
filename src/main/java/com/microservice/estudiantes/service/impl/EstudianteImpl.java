package com.microservice.estudiantes.service.impl;

import com.microservice.estudiantes.model.Estudiante;
import com.microservice.estudiantes.repository.EstudianteRepository;
import com.microservice.estudiantes.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EstudianteImpl implements EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    private final WebClient.Builder webClientBuilder;

    public EstudianteImpl(WebClient.Builder weClientBuilder) {
        this.webClientBuilder = weClientBuilder;
    }

    @Override
    public Flux<Estudiante> obtenerTodoslosEstudiantes(){
        return estudianteRepository.findAll();
    }

    @Override
    public Mono<Estudiante> obtenerEstudianteporID(String id){
        return estudianteRepository.findById(id);
    }

    @Override
    public Mono<Estudiante> registrarEstudiante(Estudiante estudiante){
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Mono<Estudiante> actualizarEstudiante(String id, Estudiante estudiante){
        return estudianteRepository.findById(id).flatMap(existingEstudiante -> {
            existingEstudiante.setNombre(estudiante.getNombre() != null ? estudiante.getNombre() : existingEstudiante.getNombre());
            existingEstudiante.setEdad(estudiante.getEdad() != 0 ? estudiante.getEdad() : existingEstudiante.getEdad());
            existingEstudiante.setCurso(estudiante.getCurso() != null ? estudiante.getCurso() : existingEstudiante.getCurso());
            return estudianteRepository.save(existingEstudiante);
        });
    }

    @Override
    public Mono<Void> eliminarEstudiante(String id){
        return estudianteRepository.deleteById(id);
    }

    @Override
    public Mono<Void> enviarMensajeAKafka(String topic, String key, Object mensaje) {
        return webClientBuilder.build()
                .post()
                .uri("/api/kafka/producer/{topic}/key/{key}", topic, key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mensaje)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
