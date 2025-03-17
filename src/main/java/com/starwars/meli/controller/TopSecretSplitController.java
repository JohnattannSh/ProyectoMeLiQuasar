package com.starwars.meli.controller;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.*;
import com.starwars.meli.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

/**
 * Controlador REST para el endpoint "/topsecret_split".
 * <p>
 * Permite recibir datos de cada satélite de forma individual mediante POST y luego consolidarlos
 * mediante un GET para calcular la posición del emisor y reconstruir el mensaje completo.
 * </p>
 */
@RestController
@RequestMapping("/topsecret_split")
public class TopSecretSplitController {

    /**
     * Almacenamiento en memoria de la información recibida de cada satélite.
     * Se usa un mapa concurrente donde la clave es el nombre del satélite (en minúsculas)
     * y el valor es el objeto Satellite con sus datos.
     */
    private static final Map<String, Satellite> satelliteData = new ConcurrentHashMap<>();

    /**
     * Servicio que contiene la lógica de negocio para calcular la posición y reconstruir el mensaje.
     */
    private final OperationService operationService = new OperationService();

    /**
     * Endpoint para recibir la información de un satélite individual.
     * <p>
     * Recibe el nombre del satélite en la URL y, en el cuerpo de la petición, la distancia y
     * los fragmentos del mensaje. Si el request está mal formado (por ejemplo, el objeto es nulo),
     * se lanza una excepción para indicar un mal request.
     * </p>
     *
     * @param satelliteName El nombre del satélite (por ejemplo, "kenobi", "skywalker" o "sato").
     * @param satellite     Objeto que contiene la distancia y el mensaje recibido del satélite.
     * @return ResponseEntity con código 200 OK si se almacenó correctamente.
     */
    @PostMapping("/{satellite_name}")
    @Operation(
            summary = "Servicio POST para enviar datos de un satélite individual.",
            description = "Recibe el nombre del satélite en la URL y su información (distancia y fragmentos del mensaje) en el cuerpo de la petición. Almacena estos datos para consolidarlos luego.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Datos almacenados correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Respuesta exitosa",
                                                    value = "{\n  \"message\": \"fragmento guardado exitosamente\"\n}"
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Error Ejemplo",
                                                    value = "{\n  \"message\": \"Datos inválidos\"\n}"
                                            )
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<?> postSatelliteData(@PathVariable("satellite_name") String satelliteName,
                                               @Valid @RequestBody Satellite satellite) {
        // Si el objeto Satellite es nulo, se lanza una excepción (la validación con @Valid se encargará de ello).
        if (satellite == null) {
            throw new TopsecretException("Datos inválidos");
        }
        // Se normaliza el nombre a minúsculas para mantener consistencia en la clave del mapa.
        satellite.setName(satelliteName.toLowerCase());
        // Almacena la información del satélite en el mapa.
        satelliteData.put(satelliteName.toLowerCase(), satellite);

        // Crea la respuesta de éxito con la clave "message".
        Map<String, String> response = new HashMap<>();
        response.put("message", "fragmento guardado exitosamente");

        // Retorna la respuesta con código HTTP 200 OK.
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint para consolidar la información de los tres satélites y calcular la posición y el mensaje.
     * <p>
     * Verifica que se haya recibido la información de "kenobi", "skywalker" y "sato". Si falta alguno,
     * lanza una excepción. Si se tienen los datos completos, extrae las distancias y los mensajes,
     * utiliza el servicio para calcular la posición mediante trilateración y reconstruye el mensaje.
     * </p>
     *
     * @return ResponseEntity con código 200 OK y un objeto que contiene la posición y el mensaje,
     * o 404 Not Found si la información es insuficiente o ocurre algún error.
     */
    @GetMapping
    @Operation(
            summary = "Servicio GET para consultar el mensaje secreto consolidado.",
            description = "Consolida la información de los tres satélites (kenobi, skywalker y sato) para calcular la posición del emisor y reconstruir el mensaje.",
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
                                                    value = "{\n  \"position\": { \"x\": \"valor de x\", \"y\": \"valor de y\" },\n  \"message\": \"mensaje completo obtenido de los satélites\"\n}"
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
    public ResponseEntity<?> getTopSecretSplit() {
        // Verifica que se hayan recibido los datos de los tres satélites.
        if (!(satelliteData.containsKey("kenobi") &&
                satelliteData.containsKey("skywalker") &&
                satelliteData.containsKey("sato"))) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }
        // Extrae los objetos Satellite del mapa.
        Satellite kenobi = satelliteData.get("kenobi");
        Satellite skywalker = satelliteData.get("skywalker");
        Satellite sato = satelliteData.get("sato");

        // Agrupa las distancias en un arreglo para pasarlas al servicio.
        double[] distances = new double[]{kenobi.getDistance(), skywalker.getDistance(), sato.getDistance()};

        // Calcula la posición del emisor utilizando la lógica de trilateración.
        Location point = operationService.getLocation(distances);

        // Agrupa los fragmentos de mensaje de cada satélite.
        List<String[]> messages = Arrays.asList(kenobi.getMessage(), skywalker.getMessage(), sato.getMessage());

        // Reconstruye el mensaje original a partir de los fragmentos recibidos.
        String message = operationService.getMessage(messages);

        // Crea la respuesta que contiene la posición y el mensaje.
        TopSecretResponse response = new TopSecretResponse(new Position(point.getX(), point.getY()), message);
        return ResponseEntity.ok(response);
    }

    /**
     * Manejador de excepciones para TopsecretException.
     * <p>
     * Captura la TopsecretException y retorna un JSON con la clave "message" y el mensaje de error,
     * utilizando un Map sin necesidad de un modelo adicional.
     * </p>
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
