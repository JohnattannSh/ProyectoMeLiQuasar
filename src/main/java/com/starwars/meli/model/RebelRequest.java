package com.starwars.meli.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Clase que contiene la lista de los satélites, cada uno con su información (nombre, distancia y mensaje),
 * que será utilizada para determinar la posición del emisor y reconstruir el mensaje.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebelRequest {
    /**
     * Lista de satélites con la información necesaria para calcular la posición y el mensaje.
     */
    private List<Satellite> satellites;
}
