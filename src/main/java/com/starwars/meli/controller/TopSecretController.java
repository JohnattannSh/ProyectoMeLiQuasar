package com.starwars.meli.controller;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.Coordinates;
import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;
import com.starwars.meli.service.ILocationService;
import com.starwars.meli.service.IMessageAssemblerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador REST para el endpoint "/topsecret".
 */
@Slf4j
@RestController
@RequestMapping("/topsecret")
public class TopSecretController {

    // Inyección de dependencias usando interfaces.
    private final ILocationService locationCalculator;
    private final IMessageAssemblerService messageAssembler;

    public TopSecretController(ILocationService locationCalculator, IMessageAssemblerService messageAssembler) {
        this.locationCalculator = locationCalculator;
        this.messageAssembler = messageAssembler;
    }

    /**
     * Endpoint para obtener la posición y mensaje reconstruido.
     *
     * @param request Objeto con la información de los satélites.
     * @return ResponseEntity con la respuesta o error.
     */
    @PostMapping("/")
    @Operation(
            summary = "Servicio POST para obtener la posición desconocida y el mensaje secreto.",
            description = "Calcula la posición del emisor utilizando las distancias y reconstruye el mensaje a partir de los fragmentos enviados.",
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
    public ResponseEntity<?> getTopSecret(@RequestBody RebelRequest request) {
        // Verifica que se reciba la información de 3 satélites.
        if (request.getSatellites() == null || request.getSatellites().size() != 3) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }

        // Crea un mapa para acceder a cada satélite en minúsculas.
        Map<String, Satellite> satelliteMap = request.getSatellites().stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s));

        // Extrae la información de cada satélite.
        Satellite kenobi = satelliteMap.get("kenobi");
        Satellite skywalker = satelliteMap.get("skywalker");
        Satellite sato = satelliteMap.get("sato");

        if (kenobi == null || skywalker == null || sato == null) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }

        // Obtiene las distancias.
        double[] distances = new double[]{kenobi.getDistance(), skywalker.getDistance(), sato.getDistance()};
        // Calcula la ubicación.
        Coordinates coordinates = locationCalculator.calculateLocation(distances);
        // Reconstruye el mensaje.
        List<String[]> messages = Arrays.asList(kenobi.getMessage(), skywalker.getMessage(), sato.getMessage());
        String message = messageAssembler.assembleMessage(messages);

        RebelResponse response = new RebelResponse(coordinates, message);
        return ResponseEntity.ok(response);
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
