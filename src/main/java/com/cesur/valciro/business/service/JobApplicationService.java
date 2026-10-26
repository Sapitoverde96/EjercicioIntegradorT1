package com.cesur.valciro.business.service;
import com.cesur.valciro.data.entity.JobApplicationEntity;
import com.cesur.valciro.data.entity.JobApplicationStatus;
import com.cesur.valciro.data.repository.JobApplicationRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;


public class JobApplicationService {
        private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final String filename;
    private final JobApplicationRepository repository;

    /**
     *  <code>filename</code> es el nombre del fichero CSV con los datos.
     * @author Ciro Álvarez
     * @since v1.0
     */

    public JobApplicationService(String filename) {
        this.filename = filename;
        this.repository = new JobApplicationRepository(filename);
    }


    /**
     * Lista las candidaturas que se filtran por estado o empresa.
     *
     * @param  estado para filtrar por si puede ser null.
     * @param empresa texto que debe contener la empresa.
     * @return lista de candidaturas que cumplen los filtros.
     * @author Ciro Álvarez
     * @since v1.0
     */


    public List<JobApplicationEntity> list(String estado, String empresa) {
        JobApplicationStatus status = (estado == null) ? null : parseStatus(estado);

        return repository.readFromCSV().stream()
                .filter(e -> status == null || e.status() == status)
                .filter(e -> empresa == null || e.company().toLowerCase().contains(empresa.toLowerCase()))
                .toList();
    }

    private JobApplicationStatus parseStatus(String text) {
        try {
            return JobApplicationStatus.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Estado no válido: " + text + ". Valores aceptados: ACCEPTED, REJECTED, PENDING");
        }
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

    /**
     * muestra la candidatura con el ID indicado.
     *
     * @throws IllegalArgumentException en el caso de que no exista.
     */

    public JobApplicationEntity view(long id) {
        return repository.readFromCSV().stream()
                .filter(e -> e.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe ninguna candidatura con el ID " + id));
    }


}
