package com.prueba.crudpersonas;

import com.prueba.crudpersonas.controller.PersonaController;
import com.prueba.crudpersonas.exception.PersonaNotFoundException;
import com.prueba.crudpersonas.model.Persona;
import com.prueba.crudpersonas.service.PersonaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonaController.class)
public class PersonaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonaService personaService;

    @Test
    void listarPeresonas() throws Exception {
        when(personaService.buscarPorNombre(null)).thenReturn(List.of(new Persona("Kathy", 27, "Tajibos")));

        mockMvc.perform(get("/personas"))
                .andExpect(status().isOk())
                .andExpect(view().name("personas/lista"))
                .andExpect(model().attributeExists("personas"));
    }

    @Test
    void crearValido() throws Exception {
        mockMvc.perform(post("/personas")
                        .param("nombre", "Kathy")
                        .param("edad", "27")
                        .param("direccion", "Tajibos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/personas"))
                .andExpect(flash().attributeExists("mensaje"));

        verify(personaService).crear(any(Persona.class));
    }

    @Test
    void crearInvalido() throws Exception {
        mockMvc.perform(post("/personas")
                        .param("nombre", "")
                        .param("edad", "-1")
                        .param("direccion", "Inominada"))
                .andExpect(status().isOk())
                .andExpect(view().name("personas/formulario"))
                .andExpect(model().attributeHasFieldErrors("persona", "nombre", "edad"));

        verify(personaService, never()).crear(any());
    }

    @Test
    void editar404() throws Exception {
        when(personaService.obtenerPorId(99L)).thenThrow(new PersonaNotFoundException(2L));

        mockMvc.perform(get("/personas/99/editar"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("personas/no-encontrada"));
    }
}
