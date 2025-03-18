package com.starwars.meli.service.impl;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.starwars.meli.model.Position;
import com.starwars.meli.service.ILocationService;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.springframework.stereotype.Service;

/**
 * Implementación de {@link ILocationService} que utiliza trilateración para calcular la ubicación.
 * <p>
 * Usa la librería de trilateración de lemmingapex junto con el optimizador Levenberg-Marquardt para
 * resolver el sistema de ecuaciones basado en las distancias medidas desde los satélites.
 * </p>
 */
@Service
public class LocationServiceImpl implements ILocationService {

    /**
     * Posiciones fijas de los satélites.
     * Cada subarreglo contiene las coordenadas [x, y] de un satélite.
     */
    private static final double[][] SATELLITE_POSITIONS = {
            {-500, -200},  // Kenobi
            {100, -100},   // Skywalker
            {500, 100}     // Sato
    };

    /**
     * Calcula las coordenadas del emisor basándose en las distancias.
     *
     * @param distances Arreglo de distancias desde el emisor a cada satélite.
     * @return Un objeto {@link Position} con las coordenadas (x, y) calculadas.
     */
    @Override
    public Position calculateLocation(double[] distances) {
        // Crea la función de trilateración utilizando las posiciones de los satélites y las distancias proporcionadas.
        TrilaterationFunction function = new TrilaterationFunction(SATELLITE_POSITIONS, distances);
        // Configura el solver utilizando el optimizador Levenberg-Marquardt para minimizar el error.
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(
                function,
                new LevenbergMarquardtOptimizer()
        );
        // Resuelve el sistema de ecuaciones y obtiene las coordenadas óptimas.
        Optimum optimum = solver.solve();
        double[] calculatedPosition = optimum.getPoint().toArray();
        // Retorna las coordenadas encapsuladas en un objeto Coordinates.
        return new Position(calculatedPosition[0], calculatedPosition[1]);
    }
}
