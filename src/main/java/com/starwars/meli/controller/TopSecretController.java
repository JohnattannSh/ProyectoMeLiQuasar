package com.starwars.meli.controller;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.service.ITopSecretService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para el endpoint "/topsecret".
 * <p>
 * Recibe la solicitud que contiene la información de los satélites y delega la lógica de negocio en el servicio.
 * </p>
 */
@RestController
@RequestMapping("/topsecret")
public class TopSecretController {

    private final ITopSecretService topSecretService;

    public TopSecretController(ITopSecretService topSecretService) {
        this.topSecretService = topSecretService;
    }

    /**
     * Endpoint POST para obtener la posición y mensaje reconstruido.
     *
     * @param request Objeto RebelRequest con la información de los satélites.
     * @return ResponseEntity con RebelResponse en caso de éxito o con un error en caso de fallo.
     */
    @PostMapping("/")
    @Operation(
            summary = "Servicio POST para obtener la posición desconocida y el mensaje secreto.",
            description = "Calcula la posición del emisor y reconstruye el mensaje a partir de la información de los satélites.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RebelResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = "{\n  \"position\": { \"x\": \"valor x\", \"y\": \"valor y\" },\n  \"message\": \"mensaje completo obtenido de los satélites\"\n}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error Ejemplo",
                                                    value = "{\n  \"message\": \"Información insuficiente o error en la operación\"\n}"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<RebelResponse> getTopSecret(@Valid @RequestBody RebelRequest request) {
        return ResponseEntity.ok(topSecretService.processTopSecretRequest(request));
    }

    /**
     * Manejador de excepciones para TopsecretException.
     *
     * @param ex La excepción capturada.
     * @return ResponseEntity con un Map que contiene el mensaje de error.
     */
    @ExceptionHandler(TopsecretException.class)
    public ResponseEntity<Map<String, String>> handleTopSecretException(TopsecretException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
