package com.starwars.meli.service;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.starwars.meli.model.Location;
import com.lemmingapex.trilateration.TrilaterationFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.springframework.stereotype.Service;

/**
 * Clase donde se implementa la lógica del caso del reto tecnico.
 * Utiliza la librería de trilateración para resolver el sistema de ecuaciones a partir de las distancias medidas
 * desde los satélites, aplicando un optimizador de mínimos cuadrados (Levenberg-Marquardt) para obtener una solución.
 * Además, contiene la lógica para reconstruir el mensaje original a partir de fragmentos recibidos por cada satélite.
 */
@Service
@Slf4j
public class OperationService {

    private static final double[][] SATELLITE_POSITIONS = {
            {-500, -200},  // Kenobi
            {100, -100},   // Skywalker
            {500, 100}     // Sato
    };

    /**
     * Calcula la posición del emisor usando trilateración.
     * Se utiliza un solver no lineal con el algoritmo Levenberg-Marquardt para ajustar
     * el modelo de distancias a partir de las posiciones fijas de los satélites.
     *
     * @param distances Arreglo de tres distancias medidas.
     * @return Un objeto Location con las coordenadas X e Y calculadas.
     */
    public Location getLocation(double[] distances) {

        // Crea la función de trilateración con las posiciones conocidas de los satélites y las distancias proporcionadas.
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(SATELLITE_POSITIONS, distances);

        // Configura el solver de mínimos cuadrados no lineales utilizando el optimizador Levenberg-Marquardt (Se usa par minimizar errores entre las distancias medidas).
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(
                trilaterationFunction,
                new LevenbergMarquardtOptimizer()
        );

        // Resuelve el sistema de ecuaciones utilizando el metodo .solve de NonLinearLeastSquaresSolver.
        Optimum optimum = solver.solve();

        // Extrae las coordenadas resultantes en forma de arreglo.
        double[] calculatedPosition = optimum.getPoint().toArray();

        log.info("Array resultante posiciones {}", calculatedPosition);
        log.info("Posicion en x del satelite emisor {}", calculatedPosition[0]);
        log.info("Posicion en y del satelite emisor {}", calculatedPosition[1]);

        // Retorna la posición encapsulada en un objeto Location.
        return new Location(calculatedPosition[0], calculatedPosition[1]);
    }

    /**
     * Reconstruye el mensaje original a partir de los fragmentos recibidos por cada satélite.
     * Cada satélite envía un arreglo de cadenas que puede contener elementos vacíos en posiciones donde no se recibió la palabra.
     * Este método itera sobre cada índice de los arreglos de mensajes y selecciona la primera palabra no vacía encontrada,
     * concatenándolas para formar el mensaje completo.
     * @param messages Lista de arreglos de cadenas, donde cada arreglo representa el mensaje parcial recibido por un satélite.
     * @return El mensaje completo reconstruido como una cadena de texto.
     */
    public String getMessage(java.util.List<String[]> messages) {
        int maxLength = 0;

        // Determina la longitud máxima del mensaje entre todos los satélites.
        for (String[] msg : messages) {
            if (msg.length > maxLength) {
                maxLength = msg.length;
            }
        }

        // Inicializa un arreglo para almacenar las palabras del mensaje final.
        String[] result = new String[maxLength];

        // Recorre cada posición del mensaje.
        for (int i = 0; i < maxLength; i++) {
            // Itera sobre cada arreglo de mensajes para obtener la primera palabra no vacía en la posición i.
            for (String[] msg : messages) {
                if (i < msg.length && msg[i] != null && !msg[i].isEmpty()) {
                    result[i] = msg[i];
                    break;
                }
            }
            // Si en una posición no se encontró ninguna palabra, se asigna una cadena vacía.
            if (result[i] == null) {
                result[i] = "";
            }
        }

        // Une las palabras no vacías con un espacio para formar el mensaje completo y lo retorna.
        return java.util.Arrays.stream(result)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.joining(" "));
    }
}
