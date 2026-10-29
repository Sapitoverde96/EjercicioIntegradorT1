package com.cesur.valciro.data.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigRepository {
    private static final String CONFIG_FILE = "config.txt";
    private final String defaultFile;

    public ConfigRepository(String defaultFile) {
        this.defaultFile = defaultFile;
    }

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

    public void setCSV(String filename) {
        try {
            Files.writeString(Path.of(CONFIG_FILE), filename.trim());
        } catch (IOException exception) {
            throw new RuntimeException("Error al guardar el fichero CSV", exception);
        }
    }
}
