package com.starwars.meli.service;

import com.starwars.meli.model.Position;

/**
 * Interfaz para calcular la ubicación del emisor a partir de un arreglo de distancias.
 */
public interface ILocationService {
    /**
     * Calcula las coordenadas del emisor basándose en las distancias medidas desde cada satélite.
     *
     * @param distances Arreglo de distancias (en la misma unidad de medida que las posiciones de los satélites).
     * @return Un objeto {@link Position} que contiene las coordenadas (x, y) calculadas.
     */
    Position calculateLocation(double[] distances);
}
