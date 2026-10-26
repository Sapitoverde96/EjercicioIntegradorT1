package com.cesur.valciro.data.entity;

public enum JobApplicationStatus {
    ACCEPTED,
    REJECTED,
    PENDING;

    /**
     * Convierte una cadena de texto de tipo <code>String</code> a un estado válido del enum <code>JobApplicationStatus</code>.
     *
     * @param status la cadena de texto a convertir.
     * @return el estado correspondiente al valor de la cadena de texto.
     */
    public static JobApplicationStatus parseStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return JobApplicationStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(status + " no coincide con los estados aceptados: ACCEPTED, REJECTED, PENDING");
        }
    }
}
