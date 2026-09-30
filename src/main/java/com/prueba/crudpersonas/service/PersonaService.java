package com.prueba.crudpersonas.service;

import com.prueba.crudpersonas.exception.PersonaNotFoundException;
import com.prueba.crudpersonas.model.Persona;
import com.prueba.crudpersonas.repositori.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    public List<Persona> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return listar();
        }
        return personaRepository.findByNombreContainingIgnoreCase(nombre.trim());
    }

    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new PersonaNotFoundException(id));
    }

    @Transactional
    public Persona crear(Persona persona) {
        persona.setId(null);
        return personaRepository.save(persona);
    }

    @Transactional
    public Persona actualizar(Long id, Persona datos) {
        Persona persona = obtenerPorId(id);
        persona.setNombre(datos.getNombre());
        persona.setEdad(datos.getEdad());
        persona.setDireccion(datos.getDireccion());
        return personaRepository.save(persona);
    }

    @Transactional
    public void eliminar(Long id) {
        Persona persona = obtenerPorId(id);
        personaRepository.delete(persona);
    }
}
