package com.cesur.valciro.business.exception;

/**
 * Es una excepción personalizada que sirve para gestionar fechas que no cumplen los criterios requeridos.
 */
public class InvalidDateException extends RuntimeException {
    /**
     * Construye una excepción de fecha inválida con un mensaje de error personalizado.
     *
     * @param message El mensaje de error que describe el problema que tiene la fecha.
     */
    public InvalidDateException(String message) {
        super(message);
    }
}
