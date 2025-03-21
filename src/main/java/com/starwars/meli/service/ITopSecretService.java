package com.starwars.meli.service;

import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;

import java.util.Map;

/**
 * Define el contrato para procesar la solicitud topsecret, calculando la ubicación
 * del emisor y ensamblando el mensaje secreto a partir de los datos de los satélites.
 */
public interface ITopSecretService {

    /**
     * Procesa la solicitud topsecret a partir de un objeto RebelRequest.
     *
     * @param request Objeto que contiene la información de los satélites.
     * @return Un objeto RebelResponse con la posición y el mensaje reconstruido.
     */
    RebelResponse processTopSecretRequest(RebelRequest request);

    /**
     * Procesa la solicitud topsecret a partir de un mapa de satélites previamente almacenados.
     *
     * @param satelliteMap Mapa con los satélites y sus respectivas distancias y mensajes.
     * @return Un objeto RebelResponse con la posición y el mensaje reconstruido.
     */
    RebelResponse processTopSecretRequest(Map<String, Satellite> satelliteMap);
}
