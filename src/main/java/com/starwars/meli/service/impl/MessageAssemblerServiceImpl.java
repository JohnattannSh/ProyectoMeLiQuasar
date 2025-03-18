package com.starwars.meli.service.impl;

import com.starwars.meli.service.IMessageAssemblerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Implementación de {@link IMessageAssemblerService} que ensambla el mensaje completo a partir de fragmentos.
 * <p>
 * Recorre cada posición de los arreglos de mensajes y selecciona la primera palabra no vacía encontrada,
 * concatenándolas para formar el mensaje final.
 * </p>
 */
@Service
public class MessageAssemblerServiceImpl implements IMessageAssemblerService {

    /**
     * Ensambla el mensaje completo a partir de una lista de arreglos de cadenas.
     *
     * @param messages Lista de arreglos de cadenas, donde cada arreglo representa el mensaje parcial recibido de un satélite.
     * @return El mensaje completo ensamblado como una cadena de texto.
     */
    @Override
    public String assembleMessage(List<String[]> messages) {
        int maxLength = 0;
        // Determina la longitud máxima entre todos los arreglos de mensajes.
        for (String[] msg : messages) {
            if (msg.length > maxLength) {
                maxLength = msg.length;
            }
        }
        String[] result = new String[maxLength];
        // Para cada posición, selecciona la primera palabra no vacía.
        for (int i = 0; i < maxLength; i++) {
            for (String[] msg : messages) {
                if (i < msg.length && msg[i] != null && !msg[i].isEmpty()) {
                    result[i] = msg[i];
                    break;
                }
            }
            if (result[i] == null) {
                result[i] = "";
            }
        }
        // Une las palabras no vacías con un espacio y retorna el mensaje completo.
        return Arrays.stream(result)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
