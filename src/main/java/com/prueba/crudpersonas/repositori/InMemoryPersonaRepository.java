package com.prueba.crudpersonas.repository;

import com.prueba.crudpersonas.model.Persona;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryPersonaRepository implements PersonaRepository {

    private final Map<Long, Persona> personas = new ConcurrentSkipListMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    @Override
    public List<Persona> findAll() {
        return new ArrayList<>(personas.values());
    }

    @Override
    public List<Persona> findByNombreContaining(String texto) {
        String filtro = texto.toLowerCase();
        return findAll().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return Optional.ofNullable(personas.get(id));
    }

    @Override
    public Persona save(Persona persona) {
        if (persona.getId() == null) {
            persona.setId(secuencia.incrementAndGet());
        }
        personas.put(persona.getId(), persona);
        return persona;
    }

    @Override
    public void deleteById(Long id) {
        personas.remove(id);
    }
}
