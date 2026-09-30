package com.prueba.crudpersonas;

import com.prueba.crudpersonas.exception.PersonaNotFoundException;
import com.prueba.crudpersonas.model.Persona;
import com.prueba.crudpersonas.repositori.PersonaRepository;
import com.prueba.crudpersonas.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {
    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    @Test
    void obtenerPorId() {
        Persona persona = new Persona("Kathy", 27, "Tajibos");
        persona.setId(1L);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        Persona resultado = personaService.obtenerPorId(1L);

        assertThat(resultado.getNombre()).isEqualTo("Kathy");
    }

    @Test
    void obtenerPorIdFallo() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personaService.obtenerPorId(99L))
                .isInstanceOf(PersonaNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void ignorarIdRecibido() {
        Persona persona = new Persona("Mari", 39, "Tadeo");
        persona.setId(2L);
        when(personaRepository.save(any(Persona.class))).thenAnswer(inv -> inv.getArgument(0));

        Persona resultado = personaService.crear(persona);

        assertThat(resultado.getId()).isNull();
        verify(personaRepository).save(persona);
    }

    @Test
    void editarLosDatos() {
        Persona persona = new Persona("Kathy", 27, "Tajibos");
        persona.setId(1L);
        Persona datos = new Persona("Kathy Polo", 28, "Final Tajibos");
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(personaRepository.save(any(Persona.class))).thenAnswer(inv -> inv.getArgument(0));

        Persona resultado = personaService.actualizar(1L, datos);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Kathy Polo");
        assertThat(resultado.getEdad()).isEqualTo(28);
        assertThat(resultado.getDireccion()).isEqualTo("Final Tajibos");
    }

    @Test
    void eliminarFallo() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personaService.eliminar(99L))
                .isInstanceOf(PersonaNotFoundException.class);
        verify(personaRepository, never()).deleteById(any());
    }

    @Test
    void eliminarExito() {
        Persona persona = new Persona("Kathy", 27, "Tajibos");
        persona.setId(1L);
        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        personaService.eliminar(1L);

        verify(personaRepository).deleteById(1L);
    }
}
