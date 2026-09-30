package com.prueba.crudpersonas.exception;

public class PersonaNotFoundException extends RuntimeException {
    public PersonaNotFoundException(Long id) {
        super("No existe una persona con id " + id);
    }
}
