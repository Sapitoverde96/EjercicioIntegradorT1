package com.cesur.valciro.business.exception;

/**
 * Es una excepción personalizada que sirve para gestionar ficheros que no cumplen los criterios requeridos.
 */
public class InvalidFileException extends RuntimeException {
    /**
     * Construye una excepción de fichero inválido con un mensaje de error personalizado.
     *
     * @param message El mensaje de error que describe el problema que tiene el fichero.
     */
    public InvalidFileException(String message) {
        super(message);
    }
}
