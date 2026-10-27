package com.cesur.valciro.business.service;

import com.cesur.valciro.business.exception.IdNotFoundException;
import com.cesur.valciro.business.exception.InvalidDateException;
import com.cesur.valciro.data.entity.JobApplicationEntity;
import com.cesur.valciro.data.entity.JobApplicationStatus;
import com.cesur.valciro.data.repository.JobApplicationRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.cesur.valciro.data.entity.JobApplicationStatus.parseStatus;

public class JobApplicationService {
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
     * Crea una nueva candidatura con un identificador único y las propiedades especificadas. Después la guarda en el fichero CSV.
     * @param title el título de la candidatura.
     * @param status el estado de la candidatura.
     * @param company la empresa a la que se postula.
     * @param date la fecha de la candidatura.
     * @return la candidatura creada con su id y sus propiedades.
     * @author Ciro Alvarez
     * @since v1.0
     */
    public JobApplicationEntity add(String title, String status, String company, String date) {
        checkField("title", title);
        checkField("status", status);
        checkField("company", company);
        checkDate(date);
        JobApplicationEntity entity = new JobApplicationEntity(
                generateNewId(),
                title,
                parseStatus(status),
                company,
                date);
        repository.saveToCSV(entity);
        return entity;
    }

    /**
     * Muestra una candidatura concreta con el identificador indicado.
     *
     * @param id el id de la candidatura a mostrar.
     * @return la candidatura con el identificador indicado.
     * @throws IdNotFoundException en el caso de que no exista una candidatura con ese identificador.
     * @author Ciro Alvarez
     * @since v1.0
     */
    public JobApplicationEntity find(long id) {
        List<JobApplicationEntity> entities = repository.readFromCSV();
        for (JobApplicationEntity entity : entities) {
            if (entity.id() == id) {
                return entity;
            }
        }
        throw new IdNotFoundException("No existe ninguna candidatura con el ID " + id);
    }

    /**
     * Elimina una candidatura concreta con el identificador indicado.
     *
     * @param id el id de la candidatura a eliminar.
     * @throws IdNotFoundException en el caso de que no exista una candidatura con ese identificador.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public void delete(long id) {
        find(id);
        List<JobApplicationEntity> entities = repository.readFromCSV();
        repository.clearFile();
        for (JobApplicationEntity entity : entities) {
            if (entity.id() != id) {
                repository.saveToCSV(entity);
            }
        }
    }

    /**
     * Importa un fichero CSV con las candidaturas.
     *
     * @param externalCSV el fichero CSV a importar.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public void importFile(String externalCSV) {
        checkField("externalCSV", externalCSV);
        List<JobApplicationEntity> importedEntities = repository.readFromCSV(externalCSV);
        repository.clearFile();
        for (JobApplicationEntity entity : importedEntities) {
            this.add(
                    entity.title(),
                    entity.status().name(),
                    entity.company(),
                    entity.dateCreated()
            );
        }
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

    /**
     * Comprueba si el campo indicado es válido.
     *
     * @param field campo a comprobar, sirve para mostrar el nombre del campo en el mensaje de error, de haberlo.
     * @param value valor del campo a comprobar.
     * @throws IllegalArgumentException si el campo es nulo o está vacío.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private void checkField(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El campo " + field + " es nulo o está vacío");
        }
    }

    /**
     * Comprueba si la fecha indicada es válida.
     *
     * @param date fecha a comprobar.
     * @throws InvalidDateException si la fecha es nula o está vacía, o no cumple con el formato DD/MM/YYYY.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private void checkDate(String date) {
        checkField("date", date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(date.trim(), formatter);
        } catch (DateTimeParseException exception) {
            throw new InvalidDateException("La fecha indicada no cumple con el formato DD/MM/YYYY");
        }
    }

    /**
     * Aumenta el identificador de la siguiente candidatura a crear.
     *
     * @return id único para la candidatura creada.
     * @author Ciro Alvarez
     * @since v1.0
     */
    private long generateNewId() {
        List<JobApplicationEntity> entities = repository.readFromCSV();
        if (entities.isEmpty()) {
            return 1;
        }
        long maxId = 0;
        for (JobApplicationEntity entity : entities) {
            maxId = Math.max(maxId, entity.id());
        }
        return maxId + 1;
    }
}
