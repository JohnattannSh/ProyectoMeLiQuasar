package com.starwars.meli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa la posición resultante en el plano.
 * Se utiliza en la respuesta del endpoint para indicar las coordenadas X e Y calculadas mediante trilateración.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    /**
     * Posicion X en el plano.
     */
    private double x;

    /**
     * Posicion y en el plano.
     */
    private double y;
}
