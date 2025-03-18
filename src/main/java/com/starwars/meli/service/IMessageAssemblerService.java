package com.starwars.meli.service;

import java.util.List;

/**
 * Interfaz para ensamblar el mensaje completo a partir de fragmentos recibidos.
 */
public interface IMessageAssemblerService {
    /**
     * Ensambla el mensaje completo a partir de una lista de arreglos de cadenas.
     *
     * @param messages Lista de arreglos de cadenas, donde cada arreglo representa el mensaje parcial de un satélite.
     * @return El mensaje completo ensamblado.
     */
    String assembleMessage(List<String[]> messages);
}
