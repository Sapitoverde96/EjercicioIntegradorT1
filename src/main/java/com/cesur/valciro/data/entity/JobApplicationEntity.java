package com.cesur.valciro.data.entity;

/**
 * Entidad que representa una candidatura.
 * @version v1.0
 * @author Valentin Leyton Golla
 */
public record JobApplicationEntity(
        Long id,
        String title,
        JobApplicationStatus status,
        String company,
        String dateCreated
) {
    /**
     * Crea una instancia de candidatura.
     *
     * @param id identificador de la candidatura.
     * @param title título de la candidatura.
     * @param status estado de la candidatura.
     * @param company empresa a la que se postula.
     * @param dateCreated fecha de creación de la candidatura.
     * @version v1.0
     * @author Valentin Leyton Golla
     */
    public JobApplicationEntity(Long id, String title, JobApplicationStatus status, String company, String dateCreated) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.company = company;
        this.dateCreated = dateCreated;
    }
}
