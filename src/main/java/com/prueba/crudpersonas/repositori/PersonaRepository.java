package com.prueba.crudpersonas.repositori;

import com.prueba.crudpersonas.model.Persona;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository {
    List<Persona> findAll();

    List<Persona> findByNombreContaining(String texto);

    Optional<Persona> findById(Long id);

    Persona save(Persona persona);

    void deleteById(Long id);
}
