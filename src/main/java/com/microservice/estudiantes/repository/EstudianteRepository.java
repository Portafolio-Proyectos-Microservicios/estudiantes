package com.microservice.estudiantes.repository;

import com.microservice.estudiantes.model.Estudiante;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends ReactiveMongoRepository<Estudiante, String> {
}
