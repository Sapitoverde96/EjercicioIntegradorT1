package com.cesur.valciro.business.service;

import com.cesur.valciro.data.entity.JobApplicationEntity;
import com.cesur.valciro.data.entity.JobApplicationStatus;
import com.cesur.valciro.data.repository.JobApplicationRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.cesur.valciro.data.entity.JobApplicationStatus.parseStatus;

public class JobApplicationService {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final JobApplicationRepository repository;

    /**
     * Constructor de la clase <code>JobApplicationService</code>.
     *
     * @param repository repositorio de candidaturas que se utiliza para leer y escribir datos.
     * @author Ciro Álvarez
     * @since v1.0
     */
    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    /**
     * Filtra la lista las candidaturas mediante el <code>status</code> de la candidatura o la <code>company</code> a la que se postula.
     *
     * @param status filtra por el estado de la candidatura. Si es <code>null</code> no se filtra por estado.
     * @param company filtra por la empresa a la que se postula. Si es <code>null</code> no se filtra por empresa.
     * @return la lista de candidaturas que cumplen los filtros.
     * @author Ciro Álvarez
     * @since v1.0
     */
    public List<JobApplicationEntity> list(String status, String company) {
        JobApplicationStatus parsedStatus = parseStatus(status);
        List<JobApplicationEntity> allJobApplications = repository.readFromCSV();
        List<JobApplicationEntity> filteredJobApplications = new ArrayList<>();
        for (JobApplicationEntity entity : allJobApplications) {
            if (matchesStatus(entity, parsedStatus) && matchesCompany(entity, company)) {
                filteredJobApplications.add(entity);
            }
        }
        return filteredJobApplications;
    }

    /**
     * añade id a las candidaturas.
     *
     * @return la candidatura creada con su id.
     * @author Ciro Alvarez
     */
    public JobApplicationEntity add(String titulo, String empresa, String estado, String fecha) {
        if (titulo == null || empresa == null || estado == null || fecha == null) {
            throw new IllegalArgumentException(
                    "Faltan argumentos. Formato de uso: añadir --titulo X --empresa Y --estado Z --fecha dd/MM/yyyy");
        }
        if (titulo.contains(";") || empresa.contains(";")) {
            throw new IllegalArgumentException("El título y la empresa no pueden contener ; ");
        }
        JobApplicationStatus status = parseStatus(estado);
        validateDate(fecha);

        JobApplicationEntity entity = new JobApplicationEntity(nextId(), titulo, status, empresa, fecha);
        repository.saveToCSV(entity);
        return entity;
    }

    /**
     * Compara si el estado de la candidatura coincide con el estado que se está filtrando.
     *
     * @param entity candidatura a filtrar
     * @param status estado a comparar
     * @return true si coinciden, false en caso contrario.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private boolean matchesStatus(JobApplicationEntity entity, JobApplicationStatus status) {
        return status == null || entity.status() == status;
    }

    /**
     * Compara si la empresa de la candidatura coincide con la empresa que se está filtrando.
     *
     * @param entity candidatura a filtrar
     * @param company empresa a comparar
     * @return true si coinciden, false en caso contrario.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private boolean matchesCompany(JobApplicationEntity entity, String company) {
        return company == null || entity.company() != null && entity.company().toLowerCase().contains(company.toLowerCase());
    }

    private void validateDate(String date) {
        try {
            LocalDate.parse(date, FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha no válida: " + date + ". Formato de fecha: dd/MM/yyyy");
        }
    }

    /**
     * metodo que pasa al siguiente id, asegurandose que no se esta creando uno que ya existia antes.
     *
     * @return id unico para la candidatura creada
     * @author Ciro Alvarez
     */
    private long nextId() {
        if (!Files.exists(Path.of(filename))) {
            return 1;
        }
        return repository.readFromCSV().stream()
                .mapToLong(JobApplicationEntity::id)
                .max()
                .orElse(0) + 1;
    }
}
