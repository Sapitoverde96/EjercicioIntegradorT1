package com.cesur.valciro.business.service;
import com.cesur.valciro.data.entity.JobApplicationEntity;
import com.cesur.valciro.data.entity.JobApplicationStatus;
import com.cesur.valciro.data.repository.JobApplicationRepository;
import java.time.format.DateTimeFormatter;
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


}
