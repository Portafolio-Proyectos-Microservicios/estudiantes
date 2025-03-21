package com.microservice.estudiantes.controller;

import com.microservice.estudiantes.model.Estudiante;
import com.microservice.estudiantes.service.EstudianteService;
import com.microservice.estudiantes.service.RedisService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private  final EstudianteService estudianteService;
    private final RedisService redisService;

    public EstudianteController(EstudianteService estudianteService, RedisService redisService) {
        this.estudianteService = estudianteService;
        this.redisService = redisService;
    }

    @GetMapping
    public Flux<Estudiante> obtenerTodoslosEstudiantes(){
        return estudianteService.obtenerTodoslosEstudiantes();
    }

    @GetMapping("/{id}")
    public Mono<Estudiante> obtenerEstudianteporID(@PathVariable String id){
        return estudianteService.obtenerEstudianteporID(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Estudiante no encontrado")));
    }

    @PostMapping("/registrar-estudiante")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Estudiante> registrarEstudiante(@Valid @RequestBody Estudiante estudiante){
        return estudianteService.registrarEstudiante(estudiante)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos Invalidos")))
                .flatMap(estudianteRegistrado ->
                    estudianteService.enviarMensajeAKafka("Registro-Estudiante", estudianteRegistrado.getId(), estudianteRegistrado)
                            .thenReturn(estudianteRegistrado)
                );
    }

    @PutMapping("/actualizar-estudiante/{id}")
    public Mono<Estudiante> actualizarEstudiante(@PathVariable String id, @RequestBody Estudiante estudiante){
        return estudianteService.actualizarEstudiante(id, estudiante).
                switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Estudiante no encontrado")));
    }

    @DeleteMapping("/eliminar-estudiante/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> eliminarEstudiante(@PathVariable String id){
        return estudianteService.eliminarEstudiante(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,"Estudiante no encontrado")));
    }


    @PostMapping("/redis-save")
    public Mono<String> saveData(@RequestParam String key, @RequestParam String value){
        return redisService.saveData(key,value).map(
                sucess -> sucess ? "Guardado correctamente" : "Error al guardar");
    }

    @GetMapping("/redis-get")
    public Mono<String> getData(@RequestParam String key){
        return redisService.getData(key).defaultIfEmpty("No se encontro el valor");
    }

    @DeleteMapping("redis-delete")
    public Mono<String > deleteData(@RequestParam String key){
        return redisService.deleteData(key)
                .map(sucess -> sucess ? "Se elimino correctamente" : "No se pudo eliminar");
    }



}
