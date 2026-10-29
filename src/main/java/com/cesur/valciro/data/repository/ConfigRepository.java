package com.cesur.valciro.data.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Repositorio de configuración que permite leer y escribir el nombre del archivo CSV en un archivo de configuración.
 *
 * @author Valentin Leyton Golla
 * @since v1.0
 */
public class ConfigRepository {
    private static final String CONFIG_FILE = "config.txt";
    private final String defaultFile;

    /**
     * Constructor de la clase <code>ConfigRepository</code>.
     *
     * @param defaultFile el nombre del archivo CSV por defecto.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public ConfigRepository(String defaultFile) {
        this.defaultFile = defaultFile;
    }

    /**
     * Obtiene el nombre del fichero CSV desde el archivo config.txt.
     *
     * @return el archivo CSV que se utilizará para leer y escribir datos.
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public String getCSV() {
        Path path = Path.of(defaultFile);
        if (!Files.exists(path)) {
            return defaultFile;
        }
        try {
            String file = Files.readString(path).trim();
            if (file.isEmpty()) {
                return defaultFile;
            }
            return file;
        } catch (IOException exception) {
            return defaultFile;
        }
    }

    /**
     * Establece el nombre del fichero CSV en el archivo config.txt.
     *
     * @param filename
     * @author Valentin Leyton Golla
     * @since v1.0
     */
    public void setCSV(String filename) {
        try {
            Files.writeString(Path.of(CONFIG_FILE), filename.trim());
        } catch (IOException exception) {
            throw new RuntimeException("Error al guardar el fichero CSV", exception);
        }
    }
}
