package com.starwars.meli.service;

import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;

public interface ITopSecretSplitService {
    /**
     * Almacena la información de un satélite individual.
     *
     * @param satelliteName Nombre del satélite (ej: "kenobi", "skywalker" o "sato").
     * @param satellite Objeto Satellite con la distancia y los fragmentos del mensaje.
     */
    void storeSatellite(String satelliteName, Satellite satellite);

    /**
     * Consolida la información de los satélites y procesa la solicitud para calcular la posición y ensamblar el mensaje.
     *
     * @return RebelResponse con la posición y el mensaje reconstruido.
     */

    RebelResponse processStoredData();
}
