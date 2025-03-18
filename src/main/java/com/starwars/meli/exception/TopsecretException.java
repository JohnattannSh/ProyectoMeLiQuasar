package com.starwars.meli.exception;

/**
 * Excepción personalizada para el proyecto "Operación Fuego de Quásar".
 * <p>
 * Se lanza cuando ocurre un error relacionado con la validación de datos o
 * el procesamiento de la información de los satélites.
 * </p>
 */
public class TopsecretException extends RuntimeException {

    /**
     * Crea una nueva instancia de TopsecretException con el mensaje de error proporcionado.
     *
     * @param message El mensaje que describe la causa del error.
     */
    public TopsecretException(String message) {
        super(message);
    }
}
