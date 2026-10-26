package com.cesur.valciro.data.repository;

import com.cesur.valciro.business.exceptions.InvalidFileException;
import com.cesur.valciro.data.entity.JobApplicationEntity;
import com.cesur.valciro.data.entity.JobApplicationStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationRepository {
    private final String filename;

    /**
     * Constructor de la clase <code>JobApplicationRepository</code>.
     *
     * @param filename nombre del fichero que se va a leer.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public JobApplicationRepository(String filename) {
        this.filename = filename;
    }

    /**
     * Guarda un objeto JobApplicationEntity en el archivo CSV.
     * @param entity entidad de la clase <code>JobApplicationEntity</code> a añadir en el fichero CSV.
     * @since v1.0
     * @author Ciro Álvarez Garrido
     * */
    public void saveToCSV(JobApplicationEntity entity) {
        filenameChecker(filename);
        Path path = Path.of(filename);
        String newLine = jobApplicationToCSV(entity) + System.lineSeparator();
        try {
            Files.writeString(path, newLine, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new RuntimeException("Ha ocurrido un error al leer el fichero");
        }
    }

    /**
     * Lee un fichero CSV y devuelve una lista de objetos de clase <code>JobApplicationEntity</code> con los datos de cada línea.
     *
     * @return lista de objetos de clase <code>JobApplicationEntity</code> con los datos de cada línea del fichero CSV.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public List<JobApplicationEntity> readFromCSV() {
        filenameChecker(filename);
        Path path = Path.of(filename);
        List<JobApplicationEntity> output = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> output.add(jobApplicationFromCSV(line)));
        } catch (IOException exception) {
            throw new RuntimeException("Ha ocurrido un error al leer el fichero");
        }
        return output;
    }

    /**
     * Comprueba que el nombre del fichero que se va a leer cumple con los requisitos
     * @param filename nombre del fichero a leer.
     * @throws InvalidFileException si el fichero no cumple con los requisitos, el mensaje depende del motivo del error.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private void filenameChecker(String filename) {
        if (filename == null) {
            throw new InvalidFileException("El fichero no puede ser nulo");
        }
        if (!filename.endsWith(".csv")) {
            throw new InvalidFileException("El fichero debe terminar en .csv");
        }
    }

    /**
     * Convierte un objeto de clase <code>JobApplicationEntity</code> a una línea de texto en formato CSV
     *
     * @param entity objeto de JobApplicationEntity a convertir.
     * @return línea CSV con los datos del objeto.
     * @author Ciro Álvarez Garrido
     * @since v1.0
     */
    private String jobApplicationToCSV(JobApplicationEntity entity) {
        return entity.id() + "," + entity.title() + "," + entity.status() + "," + entity.company() + "," + entity.dateCreated();
    }

    /**
     * Convierte una línea de un archivo CSV en un objeto de clase <code>JobApplicationEntity</code>
     *
     * @param line línea del archivo CSV a convertir.
     * @return objeto de clase <code>JobApplicationEntity</code> con los datos de la línea.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    private JobApplicationEntity jobApplicationFromCSV(String line) {
        String[] fields = line.split(",");
        return new JobApplicationEntity(
                Long.parseLong(fields[0]),
                fields[1],
                JobApplicationStatus.valueOf(fields[2].trim()),
                fields[3],
                fields[4]
        );
    }
}
