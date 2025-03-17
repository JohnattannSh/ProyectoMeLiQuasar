package com.starwars.meli.controller;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.*;
import com.starwars.meli.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST para el endpoint "/topsecret".
 * Este controlador recibe la información de tres satélites a través de un POST y retorna
 * la posición del emisor y el mensaje reconstruido.
 */
@Slf4j
@RestController
@RequestMapping("/topsecret")
public class TopSecretController {

    // Servicio que encapsula la lógica de negocio para calcular la posición y reconstruir el mensaje.
    private final OperationService operationService = new OperationService();

    /**
     * Endpoint para obtener la posición del emisor y el mensaje reconstruido.
     * Recibe un objeto JSON con la información de los satélites, valida que existan exactamente tres,
     * extrae sus datos y utiliza el servicio para calcular la posición y ensamblar el mensaje.
     *
     * @param request Objeto que contiene la lista de satélites con sus respectivas distancias y fragmentos del mensaje.
     * @return ResponseEntity que contiene:
     * - Código 200 y un objeto con la posición y mensaje en caso de éxito.
     * - Código 404 con mensaje de error si la información es insuficiente o ocurre algún error...
     */
    @PostMapping("/")
    @Operation(
            summary = "Servicio POST para obtener la posición desconocida y el mensaje secreto.",
            description = "Calcula la posición del emisor utilizando las distancias recibidas de los satélites y reconstruye el mensaje a partir de los fragmentos enviados.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TopSecretResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo de respuesta",
                                                    value = "{\n  \"position\": { \"x\": \"posicion de x en el plano 2d\", \"y\": \"posicion de y en el plano 2d\" },\n  \"message\": \"mensaje completo uniendo los objetos de los array que contiene cada satélite\"\n}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Información insuficiente o error en la operación",
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
    public ResponseEntity<?> getTopSecret(@RequestBody TopSecretRequest request) {
        // Verifica que se reciba la información de exactamente 3 satélites.
        if (request.getSatellites() == null || request.getSatellites().size() != 3) {
            // Lanza excepción personalizada si no se cumple la condición.
            throw new TopsecretException("Información insuficiente o error en la operación");
        }

        // Crea un mapa para acceder a cada satélite por su nombre en minúsculas.
        Map<String, Satellite> satelliteMap = request.getSatellites().stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s));

        // Extrae la información de cada satélite según su nombre.
        Satellite kenobi = satelliteMap.get("kenobi");
        Satellite skywalker = satelliteMap.get("skywalker");
        Satellite sato = satelliteMap.get("sato");

        // Valida que la información de los tres satélites esté presente.
        if (kenobi == null || skywalker == null || sato == null) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }

        // Agrupa las distancias recibidas de cada satélite en un arreglo.
        double[] distances = new double[]{kenobi.getDistance(), skywalker.getDistance(), sato.getDistance()};

        // Calcula la posición del emisor utilizando la lógica de trilateración.
        Location point = operationService.getLocation(distances);

        // Agrupa los fragmentos de mensaje de cada satélite.
        List<String[]> messages = Arrays.asList(kenobi.getMessage(), skywalker.getMessage(), sato.getMessage());

        // Reconstruye el mensaje original a partir de los fragmentos recibidos.
        String message = operationService.getMessage(messages);

        // Crea la respuesta que contiene la posición y el mensaje reconstruido.
        TopSecretResponse response = new TopSecretResponse(new Position(point.getX(), point.getY()), message);

        // Retorna la respuesta con un código HTTP 200 OK.
        return ResponseEntity.ok(response);
    }

    /**
     * Manejador de excepciones para TopSecretException.
     * <p>
     * Captura la TopSecretException y retorna un JSON con la clave "message" y el mensaje de error,
     * utilizando un Map sin necesidad de un modelo adicional.
     * </p>
     *
     * @param ex La excepción capturada.
     * @return ResponseEntity con un Map que contiene el mensaje de error.
     */
    @ExceptionHandler(TopsecretException.class)
    public ResponseEntity<Map<String, String>> handleTopSecretException(TopsecretException ex) {
        log.info("Mensaje de error: {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

