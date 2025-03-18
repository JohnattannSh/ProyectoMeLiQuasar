package com.starwars.meli.service.impl;

import com.starwars.meli.model.Position;
import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;
import com.starwars.meli.service.ILocationService;
import com.starwars.meli.service.IMessageAssemblerService;
import com.starwars.meli.service.ITopSecretService;
import com.starwars.meli.validation.ValidationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementación de ITopSecretService que procesa la solicitud para calcular la ubicación
 * y ensamblar el mensaje a partir de la información de los satélites.
 */
@Service
public class TopSecretServiceImpl implements ITopSecretService {

    private final ILocationService locationService;
    private final IMessageAssemblerService messageAssemblerService;

    public TopSecretServiceImpl(ILocationService locationService, IMessageAssemblerService messageAssemblerService) {
        this.locationService = locationService;
        this.messageAssemblerService = messageAssemblerService;
    }

    @Override
    public RebelResponse processTopSecretRequest(RebelRequest request) {
        // Valida y mapea la solicitud.
        Map<String, Satellite> satelliteMap = ValidationUtil.validateAndMapRebelRequest(request);

        List<Satellite> satellites = ValidationUtil.extractRequiredSatellites(satelliteMap);

        // Calcula la ubicación usando las distancias de los satélites.
        double[] distances = new double[]{
                satellites.get(0).getDistance(),
                satellites.get(1).getDistance(),
                satellites.get(2).getDistance()
        };
        Position coordinate = locationService.calculateLocation(distances);

        // Ensambla el mensaje a partir de los fragmentos de los satélites.
        String message = messageAssemblerService.assembleMessage(
                java.util.Arrays.asList(
                        satellites.get(0).getMessage(),
                        satellites.get(1).getMessage(),
                        satellites.get(2).getMessage()
                )
        );

        return new RebelResponse(coordinate, message);
    }
}
