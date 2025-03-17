package com.starwars.meli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un punto en el pano cartesiano 2D.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    /**
     * Posicion X en el plano.
     */
    private double x;

    /**
     * Posicion y en el plano.
     */
    private double y;
}
