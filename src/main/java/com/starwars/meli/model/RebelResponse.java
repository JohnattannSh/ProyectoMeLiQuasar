package com.starwars.meli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa la respuesta, la cual contiene posicion del emisor y el mensaje que emite completo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebelResponse {
    /**
     * Objeto que contiene las coordenadas de la posición del emisor.
     */
    private Coordinates position;

    /**
     * Mensaje completo reconstruido a partir de los fragmentos recibidos.
     */
    private String message;
}
