package com.starwars.meli.service.impl;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.Position;
import com.starwars.meli.model.RebelResponse;
import com.starwars.meli.model.Satellite;
import com.starwars.meli.service.ILocationService;
import com.starwars.meli.service.IMessageAssemblerService;
import com.starwars.meli.service.ITopSecretService;
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

    private final Map<String, Satellite> satelliteData = new ConcurrentHashMap<>();
    private final ITopSecretService topSecretService;

    public TopSecretSplitServiceImpl(ILocationService locationService,
                                     IMessageAssemblerService messageAssemblerService,
                                     ITopSecretService topSecretService) {
        this.topSecretService = topSecretService;
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
        // Usa el servicio compartido
        return topSecretService.processTopSecretRequest(validatedData);
    }
}
