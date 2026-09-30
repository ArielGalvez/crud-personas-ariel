# CRUD de Personas

Prueba técnica: aplicación web para administrar personas (crear, consultar, actualizar y eliminar). Cada persona tiene un identificador generado por la aplicación, nombre, edad y dirección.

Los registros se guardan **solo en memoria**, sin base de datos ni archivos, y se pierden al reiniciar la aplicación.

**Tecnologías:** Java 11, Spring Boot 2.7.5, Maven, Spring MVC, Thymeleaf y Bean Validation. Empaquetado como WAR, desplegable en WebLogic 14.1.1.

## Arquitectura

Arquitectura en capas, donde cada una tiene una única responsabilidad:

```
Navegador → Controller → Service → Repository (memoria)
               ↓
         Vistas Thymeleaf
```

| Capa | Clase | Responsabilidad |
|---|---|---|
| Controller | `PersonaController` | Recibe las peticiones web, valida el formulario y elige la vista |
| Service | `PersonaService` | Lógica de negocio (crear, actualizar, eliminar, buscar) |
| Repository | `PersonaRepository` / `InMemoryPersonaRepository` | Almacenamiento en memoria con un `ConcurrentHashMap` |
| Model | `Persona` | Datos de la persona y sus validaciones |
| Vista | `templates/personas/*.html` | Páginas renderizadas en el servidor con Thymeleaf |


## Compilar y generar el WAR

```
mvn clean package
```

Genera `target/crud-personas-ariel.war` (también ejecuta los tests).

## Desplegar en WebLogic 14.1.1

1. Entrar a la consola de administración (`http://<host>:7001/console`).
2. **Deployments → Install** → seleccionar `crud-personas-ariel.war` → *Install this deployment as an application* → **Finish**.
3. Iniciar el despliegue (**Start → Servicing all requests**) si no queda activo.
4. Abrir `http://<host>:7001/crud-personas`

## Ejecución local (sin servidor)

```
java -jar target/crud-personas-ariel.war
```

Abrir http://localhost:8080
