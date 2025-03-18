package com.starwars.meli.controller;

import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.service.ITopSecretSplitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para el endpoint "/topsecret_split".
 * <p>
 * Permite recibir datos de cada satélite de forma individual mediante POST y consolidarlos mediante un GET.
 * La lógica de negocio se delega al servicio, respetando el principio de responsabilidad única.
 * </p>
 */
@RestController
@RequestMapping("/topsecret_split")
public class TopSecretSplitController {

    private final ITopSecretSplitService topSecretSplitService;

    public TopSecretSplitController(ITopSecretSplitService topSecretSplitService) {
        this.topSecretSplitService = topSecretSplitService;
    }

    /**
     * Endpoint POST para recibir la información de un satélite individual.
     *
     * @param satelliteName El nombre del satélite (por ejemplo, "kenobi", "skywalker" o "sato").
     * @param satellite     Objeto Satellite con la distancia y los fragmentos del mensaje.
     * @return ResponseEntity con un mensaje de éxito en caso de guardar correctamente los datos.
     */
    @PostMapping("/{satellite_name}")
    @Operation(
            summary = "Servicio POST para enviar datos de un satélite individual.",
            description = "Recibe el nombre del satélite en la URL y su información en el cuerpo de la petición, almacenándola para consolidar el mensaje.",
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
                                               @Valid @RequestBody com.starwars.meli.model.Satellite satellite) {
        topSecretSplitService.storeSatellite(satelliteName, satellite);
        Map<String, String> response = new HashMap<>();
        response.put("message", "fragmento guardado exitosamente");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET para consolidar la información de los tres satélites y calcular la posición y el mensaje.
     *
     * @return ResponseEntity con RebelResponse en caso de éxito, o un error en caso de información insuficiente.
     */
    @GetMapping
    @Operation(
            summary = "Servicio GET para consultar el mensaje secreto consolidado.",
            description = "Consolida la información de los tres satélites para calcular la posición del emisor y reconstruir el mensaje.",
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
    public ResponseEntity<RebelResponse> getTopSecretSplit() {
        // Llama al método que procesa la información almacenada, sin requerir un request
        RebelResponse response = topSecretSplitService.processStoredData();
        return ResponseEntity.ok(response);
    }
}
