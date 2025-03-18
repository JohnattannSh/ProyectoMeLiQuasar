package com.starwars.meli.service;

import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.RebelResponse;

/**
 * Define el contrato para procesar la solicitud topsecret.
 */
public interface ITopSecretService {
    /**
     * Procesa la solicitud para calcular la ubicación y ensamblar el mensaje.
     *
     * @param request Objeto con la información de los satélites.
     * @return Un objeto RebelResponse con la posición y el mensaje reconstruido.
     */
    RebelResponse processTopSecretRequest(RebelRequest request);
}
