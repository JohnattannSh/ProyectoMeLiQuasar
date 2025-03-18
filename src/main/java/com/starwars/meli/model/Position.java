package com.starwars.meli.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Clase que representa un punto en el pano cartesiano 2D.
 */

@Data
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
