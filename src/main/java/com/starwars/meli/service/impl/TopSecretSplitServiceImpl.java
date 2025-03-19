package com.starwars.meli.service.impl;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.Position;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;
import com.starwars.meli.service.ILocationService;
import com.starwars.meli.service.IMessageAssemblerService;
import com.starwars.meli.service.ITopSecretSplitService;
import com.starwars.meli.validation.ValidationUtil;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de ITopSecretSplitService que almacena datos de satélites y procesa la información consolidada.
 */
@Service
public class TopSecretSplitServiceImpl implements ITopSecretSplitService {

    // Almacenamiento en memoria de los datos de los satélites.
    private final Map<String, Satellite> satelliteData = new ConcurrentHashMap<>();
    private final ILocationService locationService;
    private final IMessageAssemblerService messageAssemblerService;

    public TopSecretSplitServiceImpl(ILocationService locationService, IMessageAssemblerService messageAssemblerService) {
        this.locationService = locationService;
        this.messageAssemblerService = messageAssemblerService;
    }

    @Override
    public void storeSatellite(String satelliteName, Satellite satellite) {
        if (satellite == null) {
            throw new TopsecretException("Datos inválidos");
        }
        satellite.setName(satelliteName.toLowerCase());
        satelliteData.put(satelliteName.toLowerCase(), satellite);

        ValidationUtil.validateSatelliteName(satelliteName);

    }

    @Override
    public RebelResponse processStoredData() {

        // Valida que existan los datos de los tres satélites.
        Map<String, Satellite> validatedData = ValidationUtil.validateSatelliteData(satelliteData);
        // Extrae los satélites requeridos mediante ValidationUtil.
        List<Satellite> satellites = ValidationUtil.extractRequiredSatellites(validatedData);

        double[] distances = new double[]{
                satellites.get(0).getDistance(),
                satellites.get(1).getDistance(),
                satellites.get(2).getDistance()
        };
        Position coordinate = locationService.calculateLocation(distances);

        List<String[]> messages = Arrays.asList(
                satellites.get(0).getMessage(),
                satellites.get(1).getMessage(),
                satellites.get(2).getMessage()
        );
        String message = messageAssemblerService.assembleMessage(messages);

        return new RebelResponse(coordinate, message);
    }
}
