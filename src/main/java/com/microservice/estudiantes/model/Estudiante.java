package com.microservice.estudiantes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document (collection = "Estudiante")
public class Estudiante {

    @Id
    private String id;
    private String nombre;
    private int edad;
    private String curso;

}
