package com.prueba.crudpersonas.controller;

import com.prueba.crudpersonas.exception.PersonaNotFoundException;
import com.prueba.crudpersonas.model.Persona;
import com.prueba.crudpersonas.service.PersonaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/personas")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre, Model model) {
        model.addAttribute("personas", personaService.buscarPorNombre(nombre));
        model.addAttribute("nombre", nombre);
        return "personas/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("persona", new Persona());
        return "personas/formulario";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("persona") Persona persona,
                        BindingResult result,
                        RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "personas/formulario";
        }
        personaService.crear(persona);
        redirect.addFlashAttribute("mensaje", "Persona creada correctamente");
        return "redirect:/personas";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("persona", personaService.obtenerPorId(id));
        return "personas/formulario";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("persona") Persona persona,
                             BindingResult result,
                             RedirectAttributes redirect) {
        if (result.hasErrors()) {
            persona.setId(id);
            return "personas/formulario";
        }
        personaService.actualizar(id, persona);
        redirect.addFlashAttribute("mensaje", "Persona actualizada correctamente");
        return "redirect:/personas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        personaService.eliminar(id);
        redirect.addFlashAttribute("mensaje", "Persona eliminada correctamente");
        return "redirect:/personas";
    }

    @ExceptionHandler(PersonaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String personaNoEncontrada(PersonaNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "personas/no-encontrada";
    }
}
