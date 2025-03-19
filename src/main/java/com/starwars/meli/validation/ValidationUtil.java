package com.starwars.meli.validation;

import com.starwars.meli.exception.TopsecretException;
import com.starwars.meli.model.RebelRequest;
import com.starwars.meli.model.Satellite;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase de utilidades para validar y mapear la información de los satélites.
 */
public class ValidationUtil {

    /**
     * Valida que el RebelRequest contenga exactamente 3 satélites y que estén presentes
     * los satélites "kenobi", "skywalker" y "sato". Luego, mapea los satélites usando sus nombres en minúsculas.
     *
     * @param request Objeto RebelRequest con la información de los satélites.
     * @return Un mapa con los satélites.
     * @throws TopsecretException Si la solicitud no contiene exactamente 3 satélites o si falta alguno de los satélites requeridos.
     */
    public static Map<String, Satellite> validateAndMapRebelRequest(RebelRequest request) {
        if (request.getSatellites() == null || request.getSatellites().size() != 3) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }
        Map<String, Satellite> satelliteMap = request.getSatellites().stream()
                .collect(Collectors.toMap(s -> s.getName().toLowerCase(), s -> s));
        if (!(satelliteMap.containsKey("kenobi") &&
                satelliteMap.containsKey("skywalker") &&
                satelliteMap.containsKey("sato"))) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }
        return satelliteMap;
    }

    /**
     * Extrae los satélites requeridos ("kenobi", "skywalker" y "sato") del mapa validado.
     *
     * @param satelliteMap Mapa que contiene los satélites.
     * @return Una lista de satélites en el orden: kenobi, skywalker y sato.
     * @throws TopsecretException Si alguno de los satélites requeridos es nulo.
     */
    public static List<Satellite> extractRequiredSatellites(Map<String, Satellite> satelliteMap) {
        Satellite kenobi = satelliteMap.get("kenobi");
        Satellite skywalker = satelliteMap.get("skywalker");
        Satellite sato = satelliteMap.get("sato");
        if (kenobi == null || skywalker == null || sato == null) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }
        return Arrays.asList(kenobi, skywalker, sato);
    }

    public static Map<String, Satellite> validateSatelliteData(Map<String, Satellite> satelliteData) {
        if (!(satelliteData.containsKey("kenobi") &&
                satelliteData.containsKey("skywalker") &&
                satelliteData.containsKey("sato"))) {
            throw new TopsecretException("Información insuficiente o error en la operación");
        }
        return satelliteData;
    }

    private static final Set<String> VALID_NAMES = Set.of("kenobi", "skywalker", "sato");

    public static void validateSatelliteName(String name) {
        if (name == null || !VALID_NAMES.contains(name.toLowerCase())) {
            throw new TopsecretException("Nombre de satélite inválido: " + name);
        }
    }


}
