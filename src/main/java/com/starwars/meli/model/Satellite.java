package com.starwars.meli.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Clase que representa la información enviada por un satélite. (nombre, distancia, fragmento de mensaje)
 */
@Data
public class Satellite {
    /**
     * Nombre del satélite.
     */
    private String name;

    /**
     * Distancia desde el satélite hasta el emisor.
     */
    @NotNull(message = "La distancia es obligatoria")
    private double distance;

    /**
     * Array de fragmento del mensaje recibido por el satélite.
     */
    @NotNull(message = "El mensaje es obligatorio")
    @NotEmpty(message = "El mensaje no puede estar vacío")
    private String[] message;
}
