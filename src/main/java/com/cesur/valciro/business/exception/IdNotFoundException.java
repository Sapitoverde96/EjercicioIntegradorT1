package com.cesur.valciro.business.exception;

/**
 * Es una excepción personalizada que sirve para gestionar identificadores que no existen.
 */
public class IdNotFoundException extends RuntimeException {
    /**
     * Construye una excepción de identificador no encontrado con un mensaje de error personalizado.
     *
     * @param message El mensaje de error que describe el problema que tiene el identificador.
     */
    public IdNotFoundException(String message) {
        super(message);
    }
}
